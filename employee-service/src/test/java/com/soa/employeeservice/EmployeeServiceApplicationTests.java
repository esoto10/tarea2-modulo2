package com.soa.employeeservice;

import com.soa.employeeservice.model.Employee;
import com.soa.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeServiceApplicationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void dataLoaderCargaEmpleados() {
        assertThat(employeeRepository.count()).isEqualTo(10);
    }

    @Test
    void buscarEmpleadoPorEmail() {
        assertThat(employeeRepository.findByEmail("carlos.ramirez@empresa.com")).isPresent();
    }

    @Test
    void crearEmpleado() {
        Employee emp = Employee.builder()
                .nombre("Test").apellido("User")
                .email("test.user@test.com")
                .cargo("Tester").departamento("QA")
                .salario(50000.00)
                .build();
        Employee saved = employeeRepository.save(emp);
        assertThat(saved.getId()).isNotNull();
    }
}
