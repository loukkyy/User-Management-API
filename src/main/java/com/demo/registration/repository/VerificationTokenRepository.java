package com.demo.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.registration.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}
