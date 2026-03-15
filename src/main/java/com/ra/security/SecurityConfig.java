package com.ra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/// Lớp security đầu vào. Qua được lớp này thì mới vào được trong
// Cấu hình bảo mật
@Configuration
public class SecurityConfig {

    // Dùng @bean để có thể tiêm vào các lớp khác
    @Bean
    // Khai báo thuật toán dùng để băm (hash) mật khẩu
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Định nghĩa "luật chơi" cho các Request đi vào hệ thống
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                // Tắt bảo vệ chống giả mạo yêu cầu từ phía trình duyệt (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)

                // Không tạo và không sử dụng HttpSession để lưu trữ thông tin người dùng
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Phân quyền URL ()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll() // Chấp nhận mọi request chứa endpoint "/api/v1/auth"
                        .anyRequest().authenticated()); // Các đường dẫn còn lại đều bị khóa
        return http.build();
    }


    @Bean
    /// Gọi tới UserDetailsService
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
