package com.ra.security;

import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/// Lấy thông tin người dùng từ DB trả về cho UserDetails
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // phương thúc duy nhất của UserDetailsService là loadUserByUsername(String userName)
    // nhận userName từ form đăng nhập
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        // lấy thông tin người dùng từ userRepository và tìm bản ghi
        User user = userRepository.findByUserName(userName).orElse(null);

        // kiểm tra thông tin người dùng
        if(user == null){
            throw new UsernameNotFoundException(userName);
        }

        // dịch role từ DB trả về cho Spring Security hiểu được
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRoleName().getRole()));

        // trả về cho UserDetails
        return new UserPrincipal(user, grantedAuthorities);
    }
}
