package com.demo.camel.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Hawtio 4.x para Spring Boot 3.5.x
 *
 * Hawtio 4.x incluye auto-configuración nativa para Spring Boot 3.x.
 * La consola queda disponible en: http://localhost:8082/hawtio
 * (actuator con base-path=/)
 */
@Configuration
public class HawtioConfig {
    // Hawtio 4.x se auto-configura. No se requiere configuración manual.
}

