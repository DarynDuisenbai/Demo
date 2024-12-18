package com.example.demo.util;

import com.example.demo.dto.responce.TokenInfo;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.domain.Department;
import com.example.demo.domain.JobTitle;
import com.example.demo.domain.User;
import com.example.demo.repository.spec.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Getter
    @Value("${jwt.access.token.expiration}")
    private long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public String generateRefreshToken(UserDetails userDetails) throws UserNotFoundException {
        return createToken(userDetails.getUsername(), refreshTokenExpiration);
    }

    public String generateToken(UserDetails userDetails) throws UserNotFoundException {
        return createToken(userDetails.getUsername(), accessTokenExpiration);
    }

    private String createToken(String username, long expiration) throws UserNotFoundException {
        User user = userRepository.findByEmail(username).get();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("secondName", user.getSecondName());
        claims.put("password", user.getPassword());
        claims.put("email", user.getEmail());
        claims.put("profileImage", user.getProfileImage());
        claims.put("registrationDate", user.getRegistrationDate().toString());
        claims.put("department", user.getDepartment());
        claims.put("IIN", user.getIIN());
        claims.put("job", user.getJob());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(secretKey))
                .compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public TokenInfo getTokenInfo(String token) {
        Claims claims = extractAllClaims(token);


        Department department = mapToDepartment((Map<String, Object>) claims.get("department"));
        JobTitle job = mapToJobTitle((Map<String, Object>) claims.get("job"));

        return new TokenInfo(
                safeToString(claims.get("name")),
                safeToString(claims.get("secondName")),
                safeToString(claims.get("email")),
                safeToString(claims.get("profileImage")),
                safeToString(claims.get("registrationDate")),
                department,
                safeToString(claims.get("IIN")),
                job
        );
    }
    private String safeToString(Object claim) {
        if (claim instanceof String) {
            return (String) claim;
        } else if (claim != null) {
            return claim.toString();
        } else {
            return null;
        }
    }

    private Department mapToDepartment(Map<String, Object> map) {
        if (map == null) return null;
        Department department = new Department();
        department.set_id(safeToString(map.get("_id")));
        department.setName(safeToString(map.get("name")));
        department.setRegion(safeToString(map.get("region")));
        return department;
    }

    private JobTitle mapToJobTitle(Map<String, Object> map) {
        if (map == null) return null;
        JobTitle jobTitle = new JobTitle();
        jobTitle.set_id(safeToString(map.get("_id")));
        jobTitle.setName(safeToString(map.get("name")));
        return jobTitle;
    }
}
