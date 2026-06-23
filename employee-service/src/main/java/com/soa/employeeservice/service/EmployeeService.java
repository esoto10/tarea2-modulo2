package com.soa.employeeservice.service;

import com.soa.employeeservice.exception.EmployeeNotFoundException;
import com.soa.employeeservice.model.Employee;
import com.soa.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee create(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    public Employee update(Long id, Employee employeeDetails) {
        Employee existing = findById(id);

        if (!existing.getEmail().equals(employeeDetails.getEmail())
                && employeeRepository.existsByEmail(employeeDetails.getEmail())) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + employeeDetails.getEmail());
        }

        existing.setNombre(employeeDetails.getNombre());
        existing.setApellido(employeeDetails.getApellido());
        existing.setEmail(employeeDetails.getEmail());
        existing.setCargo(employeeDetails.getCargo());
        existing.setDepartamento(employeeDetails.getDepartamento());
        existing.setSalario(employeeDetails.getSalario());

        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        Employee existing = findById(id);
        employeeRepository.delete(existing);
    }
}
