package com.ra.controller;

import com.ra.model.dto.RegisterRequest;
import com.ra.model.dto.UserDto;
import com.ra.model.entity.User;
import com.ra.security.LoginRequest;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterRequest registerRequest){
        User user = userService.userRegister(registerRequest);

        UserDto newUser = new UserDto();
        newUser.setUserName(registerRequest.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(registerRequest.getFullName());
        newUser.setRoleName(registerRequest.getRole().toUpperCase());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest){
        try {

            // tạo đối tượng token chưa xác thực từ dữ liệu client gửi lên
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            );

            // Spring so sánh với User trong DB
            Authentication authentication = authenticationManager.authenticate(token);

            // Nếu không có lỗi, lưu phiên đăng nhập vào Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.status(HttpStatus.OK).body("Đăng nhập thành công");
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đăng nhập thất bại");
        }
    }
}
