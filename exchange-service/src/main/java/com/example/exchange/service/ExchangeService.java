package com.example.exchange.service;

import com.example.exchange.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    public ExchangeRateResponse getUsdRate() {
        return ExchangeRateResponse.builder()
                .base("PEN")
                .target("USD")
                .rate(0.2805)
                .build();
    }
}
