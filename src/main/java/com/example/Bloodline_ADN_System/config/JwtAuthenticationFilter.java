package com.example.Bloodline_ADN_System.config;

import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Comment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader.substring(7);
        String email;
        try {
            email = jwtService.extractEmail(jwtToken);
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("Token: " + jwtToken);
        System.out.println("Email from token: " + email);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                System.out.println("Found user: " + user.getEmail() + ", Role: " + user.getRole());

                // Tạo UserDetails để validate
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                );

                if (jwtService.isValid(jwtToken, userDetails)) {
                    // Tạo authority từ role
                    String authority = "ROLE_" + user.getRole();
                    SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);

                    System.out.println("Creating authentication with authority: " + authority);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            List.of(grantedAuthority)
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Authentication set successfully");
                    System.out.println("Authorities: " + auth.getAuthorities());
                } else {
                    System.out.println("Token validation failed");
                }
            } else {
                System.out.println("User not found for email: " + email);
            }
        }

        filterChain.doFilter(request, response);
    }
}

