package com.souvik.ems.service;

import com.souvik.ems.model.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleService {
    Optional<Role> findByName(String name);
    void save(Role role);
}
