package com.demo.userManagement.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.userManagement.model.Role;
import com.demo.userManagement.repository.RoleRepository;

@Service
@Transactional
class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByname(final String name) {
        return roleRepository.findByName(name);
    }
    
    public List<Role> findAll() {
    	return roleRepository.findAll();
    }

    public Role create(final Role role) {
        return roleRepository.save(role);
    }

}
