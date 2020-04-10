package com.demo.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.demo.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ComponentScan("org.demo.security")
public class MultipleEntryPointsSecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
		.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()) // authentication by username and password
		.and()
        //.authenticationProvider(jwtAuthenticationProvider) // authentication by jwt token
        .inMemoryAuthentication().passwordEncoder(passwordEncoder()) // in memory authentication 
        .withUser("user").password(passwordEncoder().encode("pass")).roles("USER")
        .and()
        .withUser("admin").password(passwordEncoder().encode("pass")).roles("USER", "ADMIN")
        ;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder( ) {
		return new BCryptPasswordEncoder();
	}

	// MVC SECURITY CONFIG
	
	@Configuration
	@Order(1)
	public static class MvcConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private JwtRequestFilter jwtRequestFilter;
		
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
		http
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
	        .csrf().disable()
	        .headers().frameOptions().disable()
		.and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.and()
			.authorizeRequests()
				.antMatchers("/signup", "/users/register", "/registrationConfirm*", "badUser*").permitAll()
				.antMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
				.antMatchers("/h2-console/**").hasRole("ADMIN")
				.antMatchers("/users/delete/*").hasRole("ADMIN")
				.antMatchers("/users/**").authenticated()
	    .and()
			.formLogin()
	            .loginPage("/login").permitAll()
	            .loginProcessingUrl("/doLogin")
        .and()
        	.logout().permitAll().logoutUrl("/logout")
		.and()
			.rememberMe()
			.tokenValiditySeconds(604800)
			.key("cookey")
			//.useSecureCookie(true)
			.rememberMeCookieName("sticky-cookie")
			.rememberMeParameter("remember")
        .and()
	        .exceptionHandling()
	        	.accessDeniedPage("/403")
		;
	    }
	}
	
	
	// API SECURITY CONFIG
	
	@Configuration
	@Order(2)
	public static class ApiConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/api/**")
	        .csrf().disable()
	        .headers().frameOptions().disable()
	    .and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	        .authorizeRequests()
				.antMatchers("/api/authenticate").permitAll()
				.antMatchers("/api/**").authenticated()
	    .and()
	    	.httpBasic();
	        ;
	    }

	    @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
	}
	
	@Bean
	public FilterRegistrationBean<JwtRequestFilter> someFilterRegistration() {
	    FilterRegistrationBean<JwtRequestFilter> registration = new FilterRegistrationBean<>();
	    registration.setFilter(jwtRequestFilter);
	    registration.addUrlPatterns("/api/*"); // activate filter only for specific url
	    registration.setName("jwtRequestFilter");
	    registration.setOrder(1);
	    return registration;
	}
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        BasicAuthenticationEntryPoint entryPoint = 
          new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("admin realm");
        return entryPoint;
    }

	// TEST SECURITY CONFIG
	
	/*@Configuration
	@Order(3)
	public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter {
	 
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.antMatcher("/admin/**")
	            .authorizeRequests().anyRequest().hasRole("ADMIN")
	            .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint());
	    }
	 
	    @Bean
	    public AuthenticationEntryPoint authenticationEntryPoint(){
	        BasicAuthenticationEntryPoint entryPoint = 
	          new BasicAuthenticationEntryPoint();
	        entryPoint.setRealmName("admin realm");
	        return entryPoint;
	    }
	}*/
}
