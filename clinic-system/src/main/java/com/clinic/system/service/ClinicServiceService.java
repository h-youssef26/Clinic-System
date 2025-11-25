package com.clinic.system.service;

import com.clinic.system.model.ClinicService;

import java.util.List;

public interface ClinicServiceService {
    ClinicService createService(ClinicService service, Long departmentId);
    ClinicService updateService(Long id, ClinicService service, Long departmentId);
    void deleteService(Long id);
    ClinicService getServiceById(Long id);
    List<ClinicService> getAllServices();
    List<ClinicService> getServicesByDepartment(Long departmentId);
}
