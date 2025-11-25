package com.clinic.system.service;

import com.clinic.system.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);
}
