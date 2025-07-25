package com.ventinc.futbol_ranker.config;

import com.ventinc.futbol_ranker.service.JWTService;
import com.ventinc.futbol_ranker.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ApplicationContext context;

    public JwtFilter(JWTService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract the JWT token from the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the Authorization header is present and starts with "Bearer"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);    // Remove "Bearer " prefix
            username = jwtService.extractUsername(token); // Extract the username from the token
        }

        // If we have a username and no authentication is set in the security context,
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from the database using the MyUserDetailService
            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(username);

            // Validate token against the user details
            if (jwtService.validateToken(token, userDetails)){
                // Create an authentication token and set in a security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continue the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
