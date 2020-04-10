package com.demo.service;

import java.util.List;

import com.demo.persistence.model.User;
import com.demo.persistence.model.VerificationToken;
import com.demo.validation.EmailExistsException;

public interface IUserService {

    List<User> findAll();
    
    User findUserByEmail(final String email);

    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;
    
    String delete(final Long id);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);
}
