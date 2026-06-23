package com.demo.department.controller;

import com.demo.department.entity.Department;
import com.demo.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "API para la gestión de departamentos")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "Obtener todos los departamentos")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos obtenida exitosamente")
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un departamento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Departamento encontrado"),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    })
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo departamento")
    @ApiResponse(responseCode = "201", description = "Departamento creado exitosamente")
    public ResponseEntity<Department> create(@RequestBody Department department) {
        Department saved = departmentService.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un departamento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Departamento actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    })
    public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.update(id, department)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un departamento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Departamento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (departmentService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
