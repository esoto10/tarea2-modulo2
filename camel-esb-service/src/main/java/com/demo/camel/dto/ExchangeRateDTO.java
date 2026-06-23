package com.demo.camel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO que representa el tipo de cambio del Exchange Service (http://localhost:8081).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDTO {

    private String currency;
    private Double rate;

    public ExchangeRateDTO() {}

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    @Override
    public String toString() {
        return "ExchangeRateDTO{currency='" + currency + "', rate=" + rate + "}";
    }
}
