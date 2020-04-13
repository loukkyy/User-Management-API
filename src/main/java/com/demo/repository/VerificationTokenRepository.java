package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}
