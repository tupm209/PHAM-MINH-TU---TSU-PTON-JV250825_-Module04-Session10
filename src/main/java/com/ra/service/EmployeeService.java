package com.ra.service;

import com.ra.model.dto.EmployeeCreateDto;
import com.ra.model.dto.EmployeeUpdateDto;
import com.ra.model.entity.Employee;
import com.ra.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UploadService uploadService;

    public Employee createEmployee(EmployeeCreateDto employeeCreateDTO) throws IOException {
        Employee employee = Employee.builder()
                .email(employeeCreateDTO.getEmail())
                .fullName(employeeCreateDTO.getFullName())
                .avatarUrl(uploadService.uploadFile(employeeCreateDTO.getAvatarFile()))
                .build();
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(EmployeeUpdateDto employeeUpdateDto, Integer id) throws IOException {
        Employee updateEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên khôn tồn tại"));

        updateEmployee.setEmail(employeeUpdateDto.getEmail());
        updateEmployee.setFullName(employeeUpdateDto.getFullName());

        if(employeeUpdateDto.getAvatarFile() != null && !employeeUpdateDto.getAvatarFile().isEmpty()){
            updateEmployee.setAvatarUrl(uploadService.uploadFile(employeeUpdateDto.getAvatarFile()));

        }

        return employeeRepository.save(updateEmployee);
    }
}
