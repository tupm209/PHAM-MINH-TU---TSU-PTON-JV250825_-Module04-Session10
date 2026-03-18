package com.ra.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {
    private String fullName;
    private String email;
    private MultipartFile avatarFile;
}
