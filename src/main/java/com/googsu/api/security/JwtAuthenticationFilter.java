package com.googsu.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("\n=== Request Details ===");
            log.info("Request URI: {}", request.getRequestURI());
            log.info("Request Method: {}", request.getMethod());

            // 모든 헤더 출력
            log.info("\n=== All Request Headers ===");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info("Header - {}: {}", headerName, headerValue);
            }
            log.info("=== End All Headers ===\n");

            // Authorization 헤더 확인
            String authHeader = request.getHeader("Authorization");
            log.info("Authorization Header: {}", authHeader != null ? "Present" : "Missing");

            if (authHeader != null) {
                log.info("Full Authorization Header: {}", authHeader);
                if (authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    log.info("Token Length: {} characters", token.length());
                    log.info("Token Preview: {}...", token.substring(0, Math.min(20, token.length())));

                    if (jwtTokenProvider.validateToken(token)) {
                        log.info("Token Validation: SUCCESS");
                        String email = jwtTokenProvider.getEmailFromToken(token);
                        log.info("Extracted Email: {}", email);

                        try {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                            log.info("User details loaded successfully for email: {}", email);
                            log.info("User authorities: {}", userDetails.getAuthorities());

                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            log.info("Authentication token created successfully");

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.info("Security context updated with authentication");
                            log.info("Current authentication: {}",
                                    SecurityContextHolder.getContext().getAuthentication());
                        } catch (Exception e) {
                            log.error("Error loading user details: {}", e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        log.error("Token Validation: FAILED");
                    }
                } else {
                    log.error("Invalid Token Format: Should start with 'Bearer '");
                }
            } else {
                log.error("No Authorization Header Found");
            }
            log.info("=== End Request Details ===\n");
        } catch (Exception e) {
            log.error("Error in JWT Authentication Filter", e);
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/members/signup") || path.equals("/api/members/login");
    }
}