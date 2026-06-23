package com.example.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exchange rate response between two currencies")
public class ExchangeRateResponse {

    @Schema(description = "Base currency", example = "PEN")
    private String base;

    @Schema(description = "Target currency", example = "USD")
    private String target;

    @Schema(description = "Exchange rate from base to target", example = "0.2805")
    private double rate;
}
