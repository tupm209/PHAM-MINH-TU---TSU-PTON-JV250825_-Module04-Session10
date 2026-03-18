package com.ra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private String getJwtFromRequest(HttpServletRequest request){
        // Lấy giá trị từ Header có tên là "Authorization"
        String authHeader = request.getHeader("Authorization");

        // Kiểm tra xem Header có tồn tại và bắt đầu bằng "Bearer " không
        if(authHeader != null && authHeader.startsWith("Bearer ")){

            // Cắt bỏ 7 ký tự đầu ("Bearer ") để lấy nguyên chuỗi Token
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Lấy chuỗi JWT từ request (thường nằm ở header Authorization)
            String jwt = getJwtFromRequest(request);

            // Kiểm tra tính hợp lệ của Token
            if(jwt != null && jwtProvider.validateToken(jwt)){

                // Lấy username từ chuỗi jwt
                String userName = jwtProvider.getUserNameFromJwtToken(jwt);

                // Lấy thông tin người dùng từ DB dựa vào username
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);


                // Nếu user hợp lệ, set thông tin cho Security Context
                // Tạo đối tượng Authentication (Chứng minh thư)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, // thông tin chi tiết về người dùng
                        null, // dùng token nên mật khẩu để là null
                        userDetails.getAuthorities() // Đây là danh sách các quyền (Roles) của user
                );

                // Bổ sung thông tin chi tiết từ Request
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu vào "Bộ nhớ tạm" của hệ thống
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            logger.error("Không thể thiết lập xác thực người dùng: {}", e);
        }

        // Cho phép request đi tiếp đến Filter tiếp theo hoặc đến Controller
        filterChain.doFilter(request, response);
    }


}
