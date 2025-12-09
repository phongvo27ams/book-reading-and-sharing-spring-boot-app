package com.be.java.foxbase.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * FRONTEND_ORIGIN can contain a single origin or multiple origins separated by comma.
     * Use '*' to allow all origins (not recommended for production if credentials are used).
     */
    @Value("${FRONTEND_ORIGIN:}")
    private String frontendOrigin;

    // fallback to CLIENT_DOMAIN if FRONTEND_ORIGIN not provided
    @Value("${CLIENT_DOMAIN:}")
    private String clientDomain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String value = (frontendOrigin != null && !frontendOrigin.isBlank()) ? frontendOrigin.trim() : null;
        if (value == null || value.isBlank()) {
            value = (clientDomain != null && !clientDomain.isBlank()) ? clientDomain.trim() : "*";
        }

        if ("*".equals(value)) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(false)
                    .maxAge(3600);
        } else {
            String[] origins = Arrays.stream(value.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
            registry.addMapping("/**")
                    .allowedOrigins(origins)
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
        }
    }
}
