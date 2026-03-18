package com.ra.controller;

import com.ra.model.dto.EmployeeCreateDto;
import com.ra.model.dto.EmployeeUpdateDto;
import com.ra.model.entity.Employee;
import com.ra.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

//    @GetMapping
//    public ResponseEntity<List<Employee>> listEmployees(){
//        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployees());
//    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@ModelAttribute EmployeeCreateDto employeeCreateDTO) throws IOException {
        Employee newEmployee = employeeService.createEmployee(employeeCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeeById(@Valid @ModelAttribute EmployeeUpdateDto employeeUpdateDto, @PathVariable Integer id) throws IOException {
        Employee updateEmployee = employeeService.updateEmployee(employeeUpdateDto, id);
        if(updateEmployee == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhân viên");
        }
        return ResponseEntity.status(HttpStatus.OK).body(updateEmployee);
    }
}
