package com.ra.security;

import com.ra.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@AllArgsConstructor

/// UserDetails phiên dịch cấu trúc bảng "users" sang ngôn ngữ của spring
// Spring Security sẽ so sánh password của người dùng được lấy ra từ đây
// Khớp: tạo JWT, cho đi qua FilterChain
// Không khớp: trả về lỗi 401 Unauthorized
public class UserPrincipal implements UserDetails {
    private User user;
    private Collection<? extends  GrantedAuthority> authorities;


    @Override
    // Collection<? extends GrantedAuthority> là kiểu trả về bắt buộc của phương thức
    // Spring Security không quản lý "một quyền" duy nhất mà quản lý một danh sách các quyền (Collection).
    // Một người dùng có thể vừa là USER, vừa là EDITOR, vừa là ADMIN
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
