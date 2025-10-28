package com.basedatos.basedatos.config; // (Revisa que tu paquete sea el correcto)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // En WebConfig.java
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            // Añade tu URL de Vercel aquí (asegúrate de que sea HTTPS)
            .allowedOrigins("http://localhost:3000", "https://ev-2-taller-base-datos-front-8ywi6ck87-joses-projects-2bc45c11.vercel.app/")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*");
    }
}