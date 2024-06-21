package com.plataform.courses.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

    public void addCorsMappings(@NonNull CorsRegistry registry){
        registry.addMapping("/**");
    }


    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {     
        registry
            .addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/css/");
        registry
            .addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/");
        registry
            .addResourceHandler("/img/**")
            .addResourceLocations("classpath:/static/img");
    }
}

