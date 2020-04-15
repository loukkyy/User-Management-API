package com.demo.userManagement.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.registration.model.VerificationToken;
import com.demo.registration.repository.VerificationTokenRepository;
import com.demo.registration.web.validation.EmailExistsException;
import com.demo.userManagement.model.Role;
import com.demo.userManagement.model.User;
import com.demo.userManagement.repository.RoleRepository;
import com.demo.userManagement.repository.UserRepository;

@Service
@Transactional
class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
	public User findById(final Long id) {
    	return userRepository.findById(id)
    			.orElseThrow(() -> new UsernameNotFoundException("User with id=" + id + " not found"));
    }
    
    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public List<User> findAll() {
    	return userRepository.findAll();
    }

    @Transactional
    @Override
    public User registerNewUser(final User user) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
		roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }
    
    @Override
    public String delete(final Long id) {
    	this.userRepository.findById(id)
        .ifPresent(user -> this.userRepository.delete(user));
    	return "deleted";
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(final String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    private boolean emailExist(final String email) {
        final User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public User updateExistingUser(User user) throws EmailExistsException {
        final Long id = user.getId();
        final String email = user.getEmail();
        final User emailOwner = userRepository.findByEmail(email);
   
        if (emailOwner != null && !id.equals(emailOwner.getId())) {
            throw new EmailExistsException("Email not available.");
        }   
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

}
