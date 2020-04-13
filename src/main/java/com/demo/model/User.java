package com.demo.model;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.demo.validation.PasswordMatches;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@PasswordMatches
public class User {

	public User(String s) {
		this.email = s;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

    @Transient
    @NotEmpty(message = "Password confirmation is required.")
    private String passwordConfirmation;
    
    private Calendar created = Calendar.getInstance();
    
    @Column
    private Boolean enabled;
    
    @ManyToMany
    private Set<Role> roles;
}