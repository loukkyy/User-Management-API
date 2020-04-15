package com.demo.userManagement.service;

import java.util.List;

import com.demo.registration.model.VerificationToken;
import com.demo.registration.web.validation.EmailExistsException;
import com.demo.userManagement.model.User;

public interface IUserService {

    List<User> findAll();
    
    User findById(final Long id);
    
    User findUserByEmail(final String email);

    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;
    
    String delete(final Long id);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);
}
