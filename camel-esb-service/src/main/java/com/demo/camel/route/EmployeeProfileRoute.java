package com.demo.camel.route;

import com.demo.camel.dto.DepartmentDTO;
import com.demo.camel.dto.EmployeeDTO;
import com.demo.camel.dto.EmployeeProfileResponse;
import com.demo.camel.dto.ExchangeRateDTO;
import com.demo.camel.processor.EmployeeProfileProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ruta compuesta: employee-profile-route
 *
 * Expone: GET /soa/employee-profile/{id}
 *
 * Flujo:
 *   1. Obtener empleado      → Employee Service   (http://localhost:8080)
 *   2. Obtener departamento  → Department Service (http://localhost:8083)
 *   3. Obtener tipo de cambio → Exchange Service  (http://localhost:8081)
 *   4. Calcular salarioUSD   → EmployeeProfileProcessor
 */
@Component
public class EmployeeProfileRoute extends RouteBuilder {

    @Value("${services.employee.url}")
    private String employeeServiceUrl;

    @Value("${services.exchange.url}")
    private String exchangeServiceUrl;

    @Value("${services.department.url}")
    private String departmentServiceUrl;

    private final EmployeeProfileProcessor employeeProfileProcessor;

    public EmployeeProfileRoute(EmployeeProfileProcessor employeeProfileProcessor) {
        this.employeeProfileProcessor = employeeProfileProcessor;
    }

    @Override
    public void configure() throws Exception {

        // Formatos de deserialización
        JacksonDataFormat employeeFormat     = new JacksonDataFormat(EmployeeDTO.class);
        JacksonDataFormat departmentFormat   = new JacksonDataFormat(DepartmentDTO.class);
        JacksonDataFormat exchangeRateFormat = new JacksonDataFormat(ExchangeRateDTO.class);
        JacksonDataFormat profileFormat      = new JacksonDataFormat(EmployeeProfileResponse.class);

        // ─── Definición REST ───────────────────────────────────────────────────
        rest("/soa")
                .get("/employee-profile/{id}")
                    .description("API compuesta: perfil completo del empleado con departamento y salario USD")
                    .outType(EmployeeProfileResponse.class)
                    .produces("application/json")
                    .to("direct:getEmployeeProfile");

        // ─── Ruta Camel ────────────────────────────────────────────────────────
        from("direct:getEmployeeProfile")
                .routeId("employee-profile-route")
                .log("ESB >>> [employee-profile-route] Iniciando perfil para ID: ${header.id}")

                // ── Paso 1: Obtener Empleado ───────────────────────────────────
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .toD(employeeServiceUrl + "/api/empleados/${header.id}?bridgeEndpoint=true")
                .unmarshal(employeeFormat)
                .process(exchange -> {
                    EmployeeDTO employee = exchange.getIn().getBody(EmployeeDTO.class);
                    exchange.setProperty("employee", employee);
                    exchange.setProperty("departmentId", employee.getDepartmentId());
                    exchange.getIn().setBody(null);
                })
                .log("ESB >>> [employee-profile-route] Empleado: ${exchangeProperty.employee}")

                // ── Paso 2: Obtener Departamento ──────────────────────────────
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .toD(departmentServiceUrl + "/api/departments/${exchangeProperty.departmentId}?bridgeEndpoint=true")
                .unmarshal(departmentFormat)
                .process(exchange -> {
                    DepartmentDTO department = exchange.getIn().getBody(DepartmentDTO.class);
                    exchange.setProperty("department", department);
                    exchange.getIn().setBody(null);
                })
                .log("ESB >>> [employee-profile-route] Departamento: ${exchangeProperty.department}")

                // ── Paso 3: Obtener Tipo de Cambio ────────────────────────────
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .to(exchangeServiceUrl + "/api/exchange/usd?bridgeEndpoint=true")
                .unmarshal(exchangeRateFormat)
                .log("ESB >>> [employee-profile-route] Tipo de cambio obtenido")

                // ── Paso 4: Construir respuesta compuesta ─────────────────────
                .process(employeeProfileProcessor)
                .marshal(profileFormat)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("ESB >>> [employee-profile-route] Perfil completado con éxito");
    }
}
