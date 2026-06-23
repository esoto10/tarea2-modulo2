package com.soa.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeProfileResponse(
        String nombre,
        String apellido,
        String cargo,
        String departamento,
        Double salarioPEN,
        Double tipoCambio,
        Double salarioUSD
) {}
