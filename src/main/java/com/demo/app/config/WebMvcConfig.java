package com.demo.app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.demo.userManagement.dto.DtoModelMapper;

//@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private DtoModelMapper dtoModelMapper;
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("loginPage");
		registry.addViewController("/profile");
		registry.addViewController("/403");

		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(dtoModelMapper);
    }
}
