package com.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.persistence.model.Privilege;
import com.demo.persistence.repository.PrivilegeRepository;

@Service
@Transactional
class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public Privilege findByname(final String name) {
        return privilegeRepository.findByName(name);
    }
    
    public List<Privilege> findAll() {
    	return privilegeRepository.findAll();
    }

    public Privilege create(final Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

}