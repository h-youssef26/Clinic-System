package com.clinic.system.service;

import com.clinic.system.model.Department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
    Department getDepartmentById(Long id);
    List<Department> getAllDepartments();
}
