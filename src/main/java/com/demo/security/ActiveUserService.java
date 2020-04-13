package com.demo.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author Servan Fichet
 * @date 13 abr. 2020
 * @time 14:07:02
 */
@Service
public class ActiveUserService {
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	public List<String> getAllActiveUsers() {
		List<Object> principals = sessionRegistry.getAllPrincipals();
		User[] users = principals.toArray(new User[principals.size()]);
		
		return Arrays.stream(users)
			.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
			.map(u -> u.getUsername())
			.collect(Collectors.toList());
	}
}
