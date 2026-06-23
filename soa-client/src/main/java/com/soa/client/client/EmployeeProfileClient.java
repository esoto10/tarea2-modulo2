package com.soa.client.client;

import com.soa.client.dto.EmployeeProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP que consume únicamente el ESB.
 * NO se conecta directamente a employee-service, exchange-service ni department-service.
 * Endpoint ESB: http://localhost:8082/soa/employee-profile/{id}
 */
@Component
public class EmployeeProfileClient {

    private final RestClient restClient;

    public EmployeeProfileClient(
            @Value("${esb.base-url}") String baseUrl,
            RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public EmployeeProfileResponse getEmployeeProfile(Long id) {
        return restClient.get()
                .uri("/soa/employee-profile/{id}", id)
                .retrieve()
                .body(EmployeeProfileResponse.class);
    }
}
