package com.soa.employeeservice.controller;

import com.soa.employeeservice.model.Employee;
import com.soa.employeeservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
@Tag(name = "Empleados", description = "API para la gestión de empleados")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Listar todos los empleados", description = "Retorna la lista completa de empleados registrados")
    @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente")
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empleado por ID", description = "Retorna un empleado dado su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                     content = @Content(schema = @Schema(implementation = Employee.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content)
    })
    public ResponseEntity<Employee> getById(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo empleado", description = "Registra un nuevo empleado en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente",
                     content = @Content(schema = @Schema(implementation = Employee.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "409", description = "El email ya existe", content = @Content)
    })
    public ResponseEntity<Employee> create(
            @Valid @RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(employee));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado", description = "Actualiza los datos de un empleado existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente",
                     content = @Content(schema = @Schema(implementation = Employee.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "El email ya existe", content = @Content)
    })
    public ResponseEntity<Employee> update(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Employee employeeDetails) {
        return ResponseEntity.ok(employeeService.update(id, employeeDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado del sistema por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
