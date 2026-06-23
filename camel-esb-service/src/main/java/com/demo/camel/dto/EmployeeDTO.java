package com.demo.camel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa un empleado del Employee Service (http://localhost:8080).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String cargo;
    private Integer departmentId;
    @JsonProperty("salario")
    private Double salarioPEN;

    public EmployeeDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public Double getSalarioPEN() { return salarioPEN; }
    public void setSalarioPEN(Double salarioPEN) { this.salarioPEN = salarioPEN; }

    @Override
    public String toString() {
        return "EmployeeDTO{id=" + id + ", nombre='" + nombre + "', apellido='" + apellido
                + "', cargo='" + cargo + "', departmentId=" + departmentId
                + ", salarioPEN=" + salarioPEN + "}";
    }
}
