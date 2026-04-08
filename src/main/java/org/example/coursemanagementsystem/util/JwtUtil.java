package org.example.coursemanagementsystem.util;
import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;   // ← Import này là quan trọng nhất
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    /**
     * Tạo Signing Key
     */
    private SecretKey getSigningKey() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret phải có ít nhất 32 ký tự để đảm bảo bảo mật!");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Tạo JWT Token
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", "ROLE_" + role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Trích xuất username từ token
     */
    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Kiểm tra token hợp lệ
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token đã hết hạn");
        } catch (JwtException e) {
            System.out.println("❌ Token không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Lỗi validate token");
        }
        return false;
    }
}