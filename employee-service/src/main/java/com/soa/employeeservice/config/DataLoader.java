package com.soa.employeeservice.config;

import com.soa.employeeservice.model.Employee;
import com.soa.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            List<Employee> empleados = List.of(
                Employee.builder()
                    .nombre("Carlos").apellido("Ramírez")
                    .email("carlos.ramirez@empresa.com")
                    .cargo("Desarrollador Backend").departamento("Tecnología").departmentId(1L)
                    .salario(75000.00).build(),
                Employee.builder()
                    .nombre("María").apellido("López")
                    .email("maria.lopez@empresa.com")
                    .cargo("Analista de Datos").departamento("Business Intelligence").departmentId(2L)
                    .salario(68000.00).build(),
                Employee.builder()
                    .nombre("Jorge").apellido("Martínez")
                    .email("jorge.martinez@empresa.com")
                    .cargo("Arquitecto de Software").departamento("Tecnología").departmentId(1L)
                    .salario(95000.00).build(),
                Employee.builder()
                    .nombre("Ana").apellido("García")
                    .email("ana.garcia@empresa.com")
                    .cargo("Desarrolladora Frontend").departamento("Tecnología").departmentId(1L)
                    .salario(72000.00).build(),
                Employee.builder()
                    .nombre("Luis").apellido("Hernández")
                    .email("luis.hernandez@empresa.com")
                    .cargo("DevOps Engineer").departamento("Infraestructura").departmentId(3L)
                    .salario(85000.00).build(),
                Employee.builder()
                    .nombre("Sofía").apellido("Torres")
                    .email("sofia.torres@empresa.com")
                    .cargo("Product Manager").departamento("Producto").departmentId(4L)
                    .salario(90000.00).build(),
                Employee.builder()
                    .nombre("Miguel").apellido("Sánchez")
                    .email("miguel.sanchez@empresa.com")
                    .cargo("QA Engineer").departamento("Calidad").departmentId(5L)
                    .salario(65000.00).build(),
                Employee.builder()
                    .nombre("Laura").apellido("Flores")
                    .email("laura.flores@empresa.com")
                    .cargo("Scrum Master").departamento("Gestión de Proyectos").departmentId(6L)
                    .salario(80000.00).build(),
                Employee.builder()
                    .nombre("Pedro").apellido("Díaz")
                    .email("pedro.diaz@empresa.com")
                    .cargo("DBA").departamento("Datos").departmentId(7L)
                    .salario(78000.00).build(),
                Employee.builder()
                    .nombre("Valentina").apellido("Morales")
                    .email("valentina.morales@empresa.com")
                    .cargo("UX Designer").departamento("Diseño").departmentId(8L)
                    .salario(70000.00).build()
            );

            employeeRepository.saveAll(empleados);
            log.info(">>> DataLoader: {} empleados cargados exitosamente.", empleados.size());
        } else {
            log.info(">>> DataLoader: La base de datos ya contiene empleados, no se cargaron datos.");
        }
    }
}
