package com.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.jwt.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    		throws ServletException, IOException {
    	System.out.println("JWT FILTER TRIGGERED");
    	String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

    	// check if header contains authorization header
    	if (!StringUtils.hasText(authorizationHeader)) {
        	filterChain.doFilter(request, response);
        	return;
    	}
    	
    	// get token
    	String jwt = jwtTokenProvider.resolveToken(authorizationHeader);
    	String username = null;
		try {
			username = jwtTokenProvider.extractUsername(jwt);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write("Token has expired");
			return;
		}


		System.out.println("UserName: " + username);
		System.out.println("Hello");
		System.out.println("UserAuthenticated: ");
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		System.out.println(SecurityContextHolder.getContext().getAuthentication() == null);
		
		// if token is valid configure Spring Security to manually set authentication
    	if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    		UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			System.out.println("UserDetailsName: " + userDetails.getUsername());
    		if (jwtTokenProvider.validateToken(jwt, userDetails)) {
    			UsernamePasswordAuthenticationToken authRequest = 
    					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    			System.out.println("Authorities: " + userDetails.getAuthorities());
    			authRequest.setDetails(
    					new WebAuthenticationDetailsSource().buildDetails(request));

    			// Set user as authenticated to pass Spring Security Configurations successfully.
    			SecurityContextHolder.getContext().setAuthentication(authRequest);
    		}
    	}
    	filterChain.doFilter(request, response);
	}
}
