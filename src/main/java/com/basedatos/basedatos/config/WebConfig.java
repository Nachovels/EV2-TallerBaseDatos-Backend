package com.basedatos.basedatos.config; // (Revisa que tu paquete sea el correcto)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Permite CORS para todas tus rutas /api
            .allowedOrigins("http://localhost:3000") // Permite SOLO desde tu React app
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite estos verbos HTTP
            .allowedHeaders("*"); // Permite cualquier cabecera
    }
}