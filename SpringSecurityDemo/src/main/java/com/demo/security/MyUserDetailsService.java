package com.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.persistence.model.Role;
import com.demo.persistence.model.User;
import com.demo.persistence.repository.UserRepository;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), 
				user.getPassword(), 
				user.getEnabled(), 
				true, 
				true, 
				true, 
				getAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(
		  Collection<Role> roles) {
		    List<GrantedAuthority> authorities
		      = new ArrayList<>();
		    for (Role role: roles) {
		        authorities.add(new SimpleGrantedAuthority(role.getName()));
		        role.getPrivileges().stream()
		         .map(p -> new SimpleGrantedAuthority(p.getName()))
		         .forEach(authorities::add);
		    }
		    System.out.println(authorities);
		    return authorities;
		}
}
