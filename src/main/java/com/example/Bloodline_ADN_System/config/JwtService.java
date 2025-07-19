package com.example.Bloodline_ADN_System.config;

import com.example.Bloodline_ADN_System.Entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    // Use a strong secret key (at least 256 bits for HS256)
    @Value("${jwt.secret}")
    private String secret;


    private  Key key ;
    @PostConstruct
    public void init(){
       key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .claim("authority","ROLE_" + user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);
    }
    public String extractAuthority(String token) {
        return extractAllClaims(token).get("authority", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}