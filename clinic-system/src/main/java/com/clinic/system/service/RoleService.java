package com.clinic.system.service;

import com.clinic.system.model.Role;

import java.util.List;

public interface RoleService {

    Role createRole(Role role);

    List<Role> getAllRoles();

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    void deleteRole(Long id);

    void assignRoleToUser(Long userId, String roleName);
}
