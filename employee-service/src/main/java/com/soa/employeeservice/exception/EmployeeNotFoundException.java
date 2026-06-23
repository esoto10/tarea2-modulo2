package com.soa.employeeservice.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Empleado no encontrado con id: " + id);
    }
}
