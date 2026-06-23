package com.demo.camel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Respuesta compuesta del API: GET /soa/employee-profile/{id}.
 * Combina datos de Employee Service, Department Service y Exchange Service.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProfileResponse {

    private Integer id;
    private String nombre;
    private String apellido;
    private String cargo;
    private String departamento;
    private Double salarioPEN;
    private Double tipoCambio;
    private Double salarioUSD;

    public EmployeeProfileResponse() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Double getSalarioPEN() { return salarioPEN; }
    public void setSalarioPEN(Double salarioPEN) { this.salarioPEN = salarioPEN; }

    public Double getTipoCambio() { return tipoCambio; }
    public void setTipoCambio(Double tipoCambio) { this.tipoCambio = tipoCambio; }

    public Double getSalarioUSD() { return salarioUSD; }
    public void setSalarioUSD(Double salarioUSD) { this.salarioUSD = salarioUSD; }
}
