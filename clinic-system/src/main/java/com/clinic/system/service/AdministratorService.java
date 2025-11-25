package com.clinic.system.service;

import com.clinic.system.model.Administrator;

import java.util.List;

public interface AdministratorService {

    Administrator createAdministrator(Administrator admin);

    List<Administrator> getAllAdministrators();

    Administrator getAdministratorById(Long id);

    Administrator updateAdministrator(Long id, Administrator admin);

    void deleteAdministrator(Long id);
}
