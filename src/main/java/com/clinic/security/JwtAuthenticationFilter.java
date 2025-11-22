package com.clinic.security;

import com.clinic.controller.dto.SignInDto;
import com.clinic.exception.NotAuthenticatedException;
import com.clinic.service.implementation.ClinicUserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final ClinicUserServiceImpl userService;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   ClinicUserServiceImpl userService,
                                   RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader(AUTHENTICATION_HEADER);

        try {
            // If header is missing, just continue (security will handle requirements)
            if (authenticationHeader == null || authenticationHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Accept "Bearer " prefix case-insensitively and trim
            if (!authenticationHeader.toLowerCase().startsWith(BEARER_PREFIX.toLowerCase())) {
                // Not a bearer token — let other mechanisms (or anon) handle it
                filterChain.doFilter(request, response);
                return;
            }

            String token = authenticationHeader.substring(BEARER_PREFIX.length()).trim();
            if (token.isEmpty()) {
                throw new NotAuthenticatedException("Empty Bearer token");
            }

            // quick debug logging (replace with logger)
            System.out.println("[JWT FILTER] token length: " + token.length());

            // Validate format and extract username
            if (!jwtUtil.isValidFormat(token)) {
                throw new NotAuthenticatedException("Invalid token format");
            }

            String username = jwtUtil.extractUsername(token);
            if (username == null || username.isBlank()) {
                throw new NotAuthenticatedException("Cannot extract username from token");
            }

            // load user and validate token
            SignInDto signInDto = new SignInDto(username, null);
            UserDetailsImpl userDetails = userService.loadUserByUsername(signInDto);

            if (!jwtUtil.validateToken(token, userDetails.getUsername())) {
                throw new NotAuthenticatedException("Token validation failed or token expired");
            }

            // Build authentication and set security context
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            System.out.println("[JWT FILTER] authorities = " + userDetails.getAuthorities());


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // debug
            System.out.println("[JWT FILTER] authenticated user: " + userDetails.getUsername());

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Log and respond with 401
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Use your whitelist from SecurityConfig
        String path = request.getServletPath();
        List<String> excludeUrl = List.of(SecurityConfig.AUTH_WHITELIST); // ensure AUTH_WHITELIST is public static String[]
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : excludeUrl) {
            if (matcher.match(pattern, path)) {
                return true; // should not filter
            }
        }
        return false; // should filter
    }
}
