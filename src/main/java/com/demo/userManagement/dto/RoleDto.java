package com.demo.userManagement.dto;

import java.util.Set;

import lombok.Data;

@Data
public class RoleDto {
	private Long id;
	private String name;
	private Set<PrivilegeDto> privileges;
}
