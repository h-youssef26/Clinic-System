package com.clinic.system.service;

import com.clinic.system.model.Receptionist;

import java.util.List;

public interface ReceptionistService {

    Receptionist createReceptionist(Receptionist receptionist);

    List<Receptionist> getAllReceptionists();

    Receptionist getReceptionistById(Long id);

    Receptionist updateReceptionist(Long id, Receptionist receptionist);

    void deleteReceptionist(Long id);
}
