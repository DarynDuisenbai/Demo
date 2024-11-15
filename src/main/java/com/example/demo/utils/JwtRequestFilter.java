package com.example.demo.utils;

import com.example.demo.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Set<String> PERMITTED_ENDPOINTS = Set.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/login",
            "/register",
            "/reset-password",
            "/allUD",
            "/allDepartments",
            "/allRegions",
            "/allStatus",
            "/regInDep"
    );
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(@Lazy UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isPermittedEndpoint(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    LOGGER.info("Found accessToken cookie with JWT.");
                    break;
                }
            }
        }
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtTokenUtil.extractUsername(token);

            if (username != null) {
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    LOGGER.info("User authenticated with JWT token.");
                } else {
                    LOGGER.error("Invalid JWT token.");
                }
            }
        }

        chain.doFilter(request, response);
    }
    private boolean isPermittedEndpoint(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return PERMITTED_ENDPOINTS.stream().anyMatch(requestURI::startsWith);
    }
}
