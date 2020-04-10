package com.demo.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableWebMvc
@Configuration
public class MyWebMvcConfiguration implements WebMvcConfigurer {

   @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login")
            .setViewName("loginPage");
        registry.addViewController("/profile");
        registry.addViewController("/403");

        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
    
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
          .addResourceLocations("classpath:/META-INF/resources/");
     
        registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/
}
