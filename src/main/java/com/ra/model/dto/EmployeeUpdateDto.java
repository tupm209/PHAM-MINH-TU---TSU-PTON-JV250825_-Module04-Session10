package com.ra.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {
    @NotBlank(message = "Tên không được trống")
    @Size(min = 5, message = "Tên trên 5 ký tự")
    private String fullName;
    @Email (message = "email không đúng định dạng")
    private String email;
    private MultipartFile avatarFile;
}
