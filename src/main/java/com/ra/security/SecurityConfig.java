package com.ra.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/// Lớp security đầu vào. Qua được lớp này thì mới vào được trong
// Cấu hình bảo mật
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    // Dùng @bean để có thể tiêm vào các lớp khác
    @Bean
    // Khai báo thuật toán dùng để băm (hash) mật khẩu
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Định nghĩa "luật chơi" cho các Request đi vào hệ thống
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
                // Tắt bảo vệ chống giả mạo yêu cầu từ phía trình duyệt (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // Không tạo và không sử dụng HttpSession để lưu trữ thông tin người dùng
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Phân quyền URL ()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll() // Chấp nhận mọi request chứa endpoint "/api/v1/auth"
                        .requestMatchers("/api/v1/employees/**").permitAll()
                        .anyRequest().authenticated()); // các đường dẫn khác phải có token mới vào được

        // BƯỚC QUAN TRỌNG NHẤT: Đăng ký Filter
        // Lọc token qua filter trước rồi mới xác thực tài khoản người dùng
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    /// Gọi tới UserDetailsService
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }
}
