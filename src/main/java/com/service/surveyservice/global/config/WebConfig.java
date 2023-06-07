package com.service.surveyservice.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://172.16.214.110")   // Nginx LB ip 추가
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
