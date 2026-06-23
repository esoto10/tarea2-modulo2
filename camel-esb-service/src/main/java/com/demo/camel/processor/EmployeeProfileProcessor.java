package com.demo.camel.processor;

import com.demo.camel.dto.DepartmentDTO;
import com.demo.camel.dto.EmployeeDTO;
import com.demo.camel.dto.EmployeeProfileResponse;
import com.demo.camel.dto.ExchangeRateDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Processor que construye la respuesta compuesta del endpoint employee-profile.
 *
 * Espera en el Exchange:
 *   - Body         : ExchangeRateDTO  (último paso de la ruta)
 *   - Property "employee"   : EmployeeDTO
 *   - Property "department" : DepartmentDTO
 *
 * Calcula: salarioUSD = salarioPEN * rate
 */
@Component
public class EmployeeProfileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        ExchangeRateDTO exchangeRate = exchange.getIn().getBody(ExchangeRateDTO.class);
        EmployeeDTO     employee     = exchange.getProperty("employee", EmployeeDTO.class);
        DepartmentDTO   department   = exchange.getProperty("department", DepartmentDTO.class);

        double salarioUSD = Math.round(employee.getSalarioPEN() * exchangeRate.getRate() * 100.0) / 100.0;

        EmployeeProfileResponse response = new EmployeeProfileResponse();
        response.setId(employee.getId());
        response.setNombre(employee.getNombre());
        response.setApellido(employee.getApellido());
        response.setCargo(employee.getCargo());
        response.setDepartamento(department.getNombre());
        response.setSalarioPEN(employee.getSalarioPEN());
        response.setTipoCambio(exchangeRate.getRate());
        response.setSalarioUSD(salarioUSD);

        exchange.getIn().setBody(response);
    }
}
