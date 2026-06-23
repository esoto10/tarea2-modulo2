package com.demo.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * Configuración global del REST DSL de Camel.
 *
 * - Componente     : platform-http  (integrado con el servidor web de Spring Boot)
 * - Binding mode   : off            (el ESB actúa como proxy; sin marshaling automático)
 * - API doc        : /api-doc       (OpenAPI JSON generado por camel-openapi-java)
 * - Swagger UI     : /swagger-ui.html (servido por springdoc apuntando a /api-doc)
 */
@Component
public class RestConfigurationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.off)
                .dataFormatProperty("prettyPrint", "true")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel ESB Service API")
                .apiProperty("api.version", "1.0.0")
                .apiProperty("api.description",
                        "Enterprise Service Bus que agrega Employee, Department y Exchange services")
                .apiProperty("cors", "true")
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*")
                .corsHeaderProperty("Access-Control-Allow-Methods", "GET, OPTIONS")
                .corsHeaderProperty("Access-Control-Allow-Headers",
                        "Origin, Accept, Content-Type, Authorization");
    }
}
