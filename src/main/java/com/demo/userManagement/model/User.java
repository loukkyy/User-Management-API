package com.demo.userManagement.model;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {

	public User(String s) {
		this.email = s;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotEmpty(message = "First name is required.")
	private String firstName;

	@NotEmpty(message = "Last name is required.")
	private String lastName;
	
    @Email
    @NotEmpty(message = "Email is required.")
    @NotNull
    private String email;

    @NotEmpty(message = "Password is required.")
    @NotNull
    private String password;
    
    private Calendar created = Calendar.getInstance();
    
    @Column
    private Boolean enabled;
    
    @ManyToMany
    private Set<Role> roles;
}
