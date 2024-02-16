package com.jxx.ca.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApiConfiguration implements WebMvcConfigurer {
    @Value("${front.origin.domain}")
    private String originDomain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(originDomain)
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}
