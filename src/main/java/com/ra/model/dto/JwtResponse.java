package com.ra.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String userName;
    private String role;

    public JwtResponse(String userName, String role, String accessToken){
        this.userName = userName;
        this.role = role;
        this.accessToken = accessToken;
    }
}
