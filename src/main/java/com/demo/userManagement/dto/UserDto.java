package com.demo.userManagement.dto;

import java.util.Calendar;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
/**
 * 
 * TODO
 *
 * @author Servan Fichet
 * @date 14 abr. 2020
 * @time 14:33:58
 */
@Data
public class UserDto {
	
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

    private Calendar created;
    
    private Boolean enabled;
    
    private Set<RoleDto> roles;
    
    private String whoami = "I am the DTO";

}
