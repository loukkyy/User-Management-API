package com.demo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.persistence.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}
