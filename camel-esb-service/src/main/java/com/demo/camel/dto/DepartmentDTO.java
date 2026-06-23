package com.demo.camel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO que representa un departamento del Department Service (http://localhost:8083).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDTO {

    private Integer id;
    private String nombre;

    public DepartmentDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "DepartmentDTO{id=" + id + ", nombre='" + nombre + "'}";
    }
}
