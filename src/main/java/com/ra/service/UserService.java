package com.ra.service;

import com.ra.model.dto.JwtResponse;
import com.ra.security.RegisterRequest;
import com.ra.model.dto.UserDto;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.security.JwtProvider;
import com.ra.security.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    public UserDto userRegister(RegisterRequest registerRequest){
        Optional<User> exitedUserName = userRepository.findByUserName(registerRequest.getUserName());
        if(exitedUserName.isPresent()){
            throw new RuntimeException("Tên người dùng đã tồn tại");
        }

        // tìm kiếm xem role người dùng nhập có tồn tại không
        Role exitingRole = roleRepository.findByRole(registerRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Nhập chức vụ tương ứng"));

        // chuyển sang string
        String roleName = exitingRole.getRole();

        if (!("ROLE_ADMIN").equals(roleName) && !("ROLE_USER").equals(roleName)) {
            throw new RuntimeException("Chức vụ không tồn tại!");
        }

        User user = User.builder()
                .userName(registerRequest.getUserName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .roleName(exitingRole)
                .build();
        userRepository.save(user);

        return UserDto.builder()
                .userName(user.getUserName())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .roleName(roleName)
                .build();
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest){
        try {
            // tạo đối tượng token chưa xác thực từ dữ liệu client gửi lên
            // Spring so sánh với User trong DB
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                    )
            );

            // Nếu không có lỗi, lưu phiên đăng nhập vào Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Gọi JwtProvider để tạo token
            String jwt = jwtProvider.generateToken(authentication);

            // Lấy thông tin username (từ UserDetails)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return new JwtResponse(userDetails.getUsername(), userDetails.getAuthorities().toString(), jwt);
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Không tìm thấy user");
        } catch (Exception e){
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
