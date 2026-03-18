package com.ra.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Logger là một công cụ ghi lại nhật ký hoạt động của ứng dụng
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // Tạo SecretKey từ chuỗi secret trong properties
    private Key getSignInKey(){
        // Chuyển chuỗi secret từ properties thành mảng byte
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        // ép buộc nó luôn đủ độ dài hoặc băm nó ra
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // tạo token
    public String generateToken(Authentication authentication){
        // lấy thông tin user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // xây dựng token
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // username
                .claim("role", userDetails.getAuthorities().toString()) // custom token
                .setIssuedAt(new Date()) // thời điểm tạo
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration)) // thời điểm hết hạn
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // ký tên với thuật toán HS256
                .compact(); // nén chuỗi thành công
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        }catch (Exception e){
            logger.error("Token không hợp lệ: {}", e.getMessage());
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
