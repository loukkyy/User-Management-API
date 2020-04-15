package com.demo.app.config;

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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ComponentScan("org.demo.security")
public class MultipleEntryPointsSecurityConfig {
	
	@Order(1)
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
        .inMemoryAuthentication().passwordEncoder(passwordEncoder()) // in memory authentication 
        .withUser("user").password(passwordEncoder().encode("pass")).roles("USER")
        .and()
        .withUser("admin").password(passwordEncoder().encode("pass")).roles("USER", "ADMIN")
        ;
	}
	
	@Order(2)
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
		auth
		.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()) // authentication by username and password
        ;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder( ) {
		return new BCryptPasswordEncoder();
	}
	
	
	// API SECURITY CONFIG
	
	@Configuration
	@Order(1)
	public static class ApiConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private JwtRequestFilter jwtRequestFilter;
		
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
		http
        	.csrf().disable()
			.antMatcher("/api/**")
	        .authorizeRequests()
				.antMatchers("/api/authenticate").permitAll()
				.antMatchers("/api/**").authenticated()
		.and()
			.antMatcher("/api/**")
        	.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	    	.httpBasic()
    	.and()
	        .headers().frameOptions().disable();
	        ;
	    }

	    @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
		
		/**
		 * Register JwtRequestFilter only for /api/* urls
		 * @return
		 */
		@Bean
		public FilterRegistrationBean<JwtRequestFilter> someFilterRegistration() {
		    FilterRegistrationBean<JwtRequestFilter> registration = new FilterRegistrationBean<>();
		    registration.setFilter(jwtRequestFilter);
		    registration.addUrlPatterns("/api/*"); // activate filter only for specific url
		    registration.setName("jwtRequestFilter");
		    registration.setOrder(1);
		    return registration;
		}
	}

	// MVC SECURITY CONFIG
	
	@Configuration
	@Order(2)
	public static class MvcConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/signup", "/users/register", 
						"/registrationConfirm*", "badUser*",
						"/static/**").permitAll()
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
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        .maximumSessions(1)
	        .sessionRegistry(sessionRegistry())
	        .and().sessionFixation().migrateSession()
        .and()
	        .exceptionHandling()
	        	.accessDeniedPage("/403")
		.and()
	        .headers().frameOptions().disable()
		;
	    }
		
		@Override
		public void configure(WebSecurity web) throws Exception {
			web
			.ignoring()
			.antMatchers("/resources/**", "/static/**");
		}
		
	    @Bean
	    public SessionRegistry sessionRegistry() {
	    	return new SessionRegistryImpl();
	    }
	}
}
