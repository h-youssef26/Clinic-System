package com.clinic.system.service.impl;

import com.clinic.system.model.ClinicService;
import com.clinic.system.model.Department;
import com.clinic.system.repository.ClinicServiceRepository;
import com.clinic.system.repository.DepartmentRepository;
import com.clinic.system.service.ClinicServiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicServiceServiceImpl implements ClinicServiceService {

    private final ClinicServiceRepository clinicServiceRepository;
    private final DepartmentRepository departmentRepository;

    public ClinicServiceServiceImpl(ClinicServiceRepository clinicServiceRepository,
                                    DepartmentRepository departmentRepository) {
        this.clinicServiceRepository = clinicServiceRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public ClinicService createService(ClinicService service, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        service.setDepartment(department);
        return clinicServiceRepository.save(service);
    }

    @Override
    public List<ClinicService> getAllServices() {
        return clinicServiceRepository.findAll();
    }

    @Override
    public ClinicService getServiceById(Long id) {
        return clinicServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic service not found with id: " + id));
    }

    @Override
    public List<ClinicService> getServicesByDepartment(Long departmentId) {
        return clinicServiceRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    @Override
    public ClinicService updateService(Long id, ClinicService service, Long departmentId) {
        ClinicService existing = getServiceById(id);

        existing.setName(service.getName());
        existing.setCode(service.getCode());
        existing.setDescription(service.getDescription());
        existing.setPrice(service.getPrice());

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            existing.setDepartment(department);
        }

        return clinicServiceRepository.save(existing);
    }

    @Transactional
    @Override
    public void deleteService(Long id) {
        ClinicService existing = getServiceById(id);
        clinicServiceRepository.delete(existing);
    }
}
