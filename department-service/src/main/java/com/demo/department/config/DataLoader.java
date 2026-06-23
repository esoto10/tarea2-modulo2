package com.demo.department.config;

import com.demo.department.entity.Department;
import com.demo.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (departmentRepository.count() == 0) {
            List<Department> departments = List.of(
                Department.builder().nombre("Tecnología").build(),
                Department.builder().nombre("Business Intelligence").build(),
                Department.builder().nombre("Infraestructura").build(),
                Department.builder().nombre("Producto").build(),
                Department.builder().nombre("Calidad").build(),
                Department.builder().nombre("Gestión de Proyectos").build(),
                Department.builder().nombre("Datos").build(),
                Department.builder().nombre("Diseño").build()
            );
            departmentRepository.saveAll(departments);
            log.info("DataLoader: {} departamentos cargados exitosamente.", departments.size());
        }
    }
}
