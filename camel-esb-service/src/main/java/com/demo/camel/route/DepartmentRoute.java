package com.demo.camel.route;

import com.demo.camel.dto.DepartmentDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ruta: department-route
 *
 * Expone:  GET /soa/departments/{id}
 * Delega:  ${services.department.url}/api/departments/{id}
 */
@Component
public class DepartmentRoute extends RouteBuilder {

    @Value("${services.department.url}")
    private String departmentServiceUrl;

    @Override
    public void configure() throws Exception {

        // ─── Definición REST ───────────────────────────────────────────────────
        rest("/soa")
                .get("/departments/{id}")
                    .description("Obtener departamento por ID desde Department Service")
                    .outType(DepartmentDTO.class)
                    .produces("application/json")
                    .to("direct:getDepartment");

        // ─── Ruta Camel ────────────────────────────────────────────────────────
        from("direct:getDepartment")
                .routeId("department-route")
                .log("ESB >>> [department-route] Consultando Department Service para ID: ${header.id}")
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .toD(departmentServiceUrl + "/api/departments/${header.id}?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("ESB >>> [department-route] Respuesta recibida");
    }
}
