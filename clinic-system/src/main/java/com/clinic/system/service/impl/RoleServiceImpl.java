package com.clinic.system.service.impl;

import com.clinic.system.model.Role;
import com.clinic.system.model.User;
import com.clinic.system.repository.RoleRepository;
import com.clinic.system.repository.UserRepository;
import com.clinic.system.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository,
                           UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        Role existing = getRoleById(id);
        roleRepository.delete(existing);
    }

    @Transactional
    @Override
    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        user.getRoles().add(role);
        role.getUsers().add(user);

        userRepository.save(user);
        roleRepository.save(role);
    }
}
