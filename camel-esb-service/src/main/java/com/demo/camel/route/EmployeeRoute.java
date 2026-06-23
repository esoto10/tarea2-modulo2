package com.demo.camel.route;

import com.demo.camel.dto.EmployeeDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ruta: employee-route
 *
 * Expone:  GET /soa/empleados/{id}
 * Delega:  ${services.employee.url}/api/empleados/{id}
 */
@Component
public class EmployeeRoute extends RouteBuilder {

    @Value("${services.employee.url}")
    private String employeeServiceUrl;

    @Override
    public void configure() throws Exception {

        // ─── Definición REST ───────────────────────────────────────────────────
        rest("/soa")
                .get("/empleados/{id}")
                    .description("Obtener empleado por ID desde Employee Service")
                    .outType(EmployeeDTO.class)
                    .produces("application/json")
                    .to("direct:getEmployee");

        // ─── Ruta Camel ────────────────────────────────────────────────────────
        from("direct:getEmployee")
                .routeId("employee-route")
                .log("ESB >>> [employee-route] Consultando Employee Service para ID: ${header.id}")
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .toD(employeeServiceUrl + "/api/empleados/${header.id}?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("ESB >>> [employee-route] Respuesta recibida");
    }
}
