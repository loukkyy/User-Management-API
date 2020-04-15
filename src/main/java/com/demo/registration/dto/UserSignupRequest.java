package com.demo.registration.dto;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.demo.registration.web.validation.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class UserSignupRequest {

	private Long id;

	@NotEmpty(message = "First name is required.")
	private String firstName;

	@NotEmpty(message = "Last name is required.")
	private String lastName;
	
    @Email
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

    @Transient
    @NotEmpty(message = "Password confirmation is required.")
    private String passwordConfirmation;
    
}
