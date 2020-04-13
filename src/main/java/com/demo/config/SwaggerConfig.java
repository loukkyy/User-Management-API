package com.demo.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Import({BeanValidatorPluginsConfiguration.class, SpringDataRestConfiguration.class})
public class SwaggerConfig {
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.demo.web.controller"))
          .paths(PathSelectors.any())
          .build()
          .securitySchemes(Arrays.asList(apiKey()))
          .apiInfo(apiInfo());                                           
    }
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "User-Management REST API", 
	      "CRUD Web Service for User-Management.", 
	      "API TOS", 
	      "Terms of service", 
	      new Contact("Servan Fichet", "", "servan.fichet@gmail.com"), 
	      "License of API", "API license URL", Collections.emptyList());
	}
	
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}
