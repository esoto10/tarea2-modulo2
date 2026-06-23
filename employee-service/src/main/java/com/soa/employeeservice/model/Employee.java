package com.soa.employeeservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El cargo es obligatorio")
    @Column(nullable = false)
    private String cargo;

    @NotBlank(message = "El departamento es obligatorio")
    @Column(nullable = false)
    private String departamento;

    @Column(name = "department_id")
    private Long departmentId;

    @Positive(message = "El salario debe ser mayor a cero")
    @Column(nullable = false)
    private Double salario;
}
