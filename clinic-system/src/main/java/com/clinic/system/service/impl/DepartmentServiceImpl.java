package com.clinic.system.service.impl;

import com.clinic.system.model.Department;
import com.clinic.system.repository.DepartmentRepository;
import com.clinic.system.service.DepartmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Transactional
    @Override
    public Department updateDepartment(Long id, Department department) {
        Department existing = getDepartmentById(id);
        existing.setName(department.getName());
        existing.setDescription(department.getDescription());
        return departmentRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteDepartment(Long id) {
        Department existing = getDepartmentById(id);
        departmentRepository.delete(existing);
    }

    
}



