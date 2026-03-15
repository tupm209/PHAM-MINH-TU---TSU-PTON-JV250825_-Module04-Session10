package com.ra.service;

import com.ra.model.entity.Employee;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Getter
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService(){
        employees.add(new Employee(1, "Nguyễn Văn A", 5000.0));
        employees.add(new Employee(2, "Trần Văn B", 6000.0));
        employees.add(new Employee(3, "Lê Văn C", 7000.0));
    }
}
