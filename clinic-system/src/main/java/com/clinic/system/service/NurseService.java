package com.clinic.system.service;

import com.clinic.system.model.Nurse;

import java.util.List;

public interface NurseService {
    Nurse createNurse(Nurse nurse);
    Nurse updateNurse(Long id, Nurse nurse);
    void deleteNurse(Long id);
    Nurse getNurseById(Long id);
    List<Nurse> getAllNurses();
}
