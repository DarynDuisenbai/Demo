package com.example.demo.utils;

import com.example.demo.dtos.responces.TokenInfo;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import java.security.SecureRandom;
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
        claims.put("role", user.getRole());

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

        return new TokenInfo(
                safeToString(claims.get("name")),
                safeToString(claims.get("secondName")),
                safeToString(claims.get("email")),
                safeToString(claims.get("profileImage")),
                safeToString(claims.get("registrationDate")),
                safeToString(claims.get("department")),
                safeToString(claims.get("IIN")),
                safeToString(claims.get("job")),
                safeToString(claims.get("role"))
        );
    }

    private String safeToString(Object claim) {
        if (claim instanceof String) {
            return (String) claim;
        } else if (claim != null) {
            return claim.toString(); // Fallback to calling toString for non-string types
        } else {
            return null;
        }
    }

}
