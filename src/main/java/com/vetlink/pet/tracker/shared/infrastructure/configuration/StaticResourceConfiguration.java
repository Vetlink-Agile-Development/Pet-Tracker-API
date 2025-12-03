package com.vetlink.pet.tracker.shared.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtener la ruta absoluta del directorio uploads
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
        String uploadDir = uploadPath.toUri().toString();

        // Configurar el manejador de recursos estáticos
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadDir);
    }
}
