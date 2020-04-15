package com.demo.userManagement.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Role {
	@Id
    @GeneratedValue
	private Long id;
	@NotNull
	private String name;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Privilege> privileges;
}
