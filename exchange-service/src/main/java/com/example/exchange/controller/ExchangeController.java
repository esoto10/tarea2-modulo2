package com.example.exchange.controller;

import com.example.exchange.dto.ExchangeRateResponse;
import com.example.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
@Tag(name = "Exchange Rate", description = "Currency exchange rate operations")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/usd")
    @Operation(
        summary = "Get PEN to USD exchange rate",
        description = "Returns the current exchange rate from Peruvian Sol (PEN) to US Dollar (USD)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Exchange rate retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExchangeRateResponse.class)
                )
            )
        }
    )
    public ResponseEntity<ExchangeRateResponse> getUsdRate() {
        return ResponseEntity.ok(exchangeService.getUsdRate());
    }
}
