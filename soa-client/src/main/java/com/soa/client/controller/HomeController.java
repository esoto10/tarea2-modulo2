package com.soa.client.controller;

import com.soa.client.client.EmployeeProfileClient;
import com.soa.client.dto.EmployeeProfileResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final EmployeeProfileClient employeeProfileClient;

    public HomeController(EmployeeProfileClient employeeProfileClient) {
        this.employeeProfileClient = employeeProfileClient;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            try {
                EmployeeProfileResponse profile = employeeProfileClient.getEmployeeProfile(id);
                model.addAttribute("profile", profile);
                model.addAttribute("employeeId", id);
            } catch (Exception e) {
                model.addAttribute("error",
                        "No se encontró el empleado con ID: " + id + ". Verifique que el ESB esté disponible.");
                model.addAttribute("employeeId", id);
            }
        }
        return "index";
    }
}
