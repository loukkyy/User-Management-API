package com.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.jwt.JwtTokenProvider;
import com.demo.web.model.AuthenticationRequest;
import com.demo.web.model.AuthenticationResponse;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/api/hello")
    public String helloWorld() {
    	return "Hello World!";
    }
    
    @PostMapping("/api/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

    	// authenticate user via username and password
        try {
            authenticationManager.authenticate(
            		new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            		);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        // get user details
        final UserDetails userDetails = userDetailsService
        		.loadUserByUsername(authenticationRequest.getUsername());
        System.out.println(userDetails.getAuthorities());
        // generate token
        final String jwt = jwtTokenProvider.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setJwt(jwt);
        
        // send token in response
        return ResponseEntity.ok(authenticationResponse);
    }
}
