package com.ra.service;

import com.ra.model.dto.RegisterRequest;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public User userRegister(RegisterRequest registerRequest){
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

        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setRoleName(exitingRole);
        return userRepository.save(user);
    }
}
