package com.demo.camel.route;

import com.demo.camel.dto.ExchangeRateDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Ruta: exchange-route
 *
 * Expone:  GET /soa/exchange/usd
 * Delega:  ${services.exchange.url}/api/exchange/usd
 */
@Component
public class ExchangeRoute extends RouteBuilder {

    @Value("${services.exchange.url}")
    private String exchangeServiceUrl;

    @Override
    public void configure() throws Exception {

        // ─── Definición REST ───────────────────────────────────────────────────
        rest("/soa")
                .get("/exchange/usd")
                    .description("Obtener tipo de cambio USD desde Exchange Service")
                    .outType(ExchangeRateDTO.class)
                    .produces("application/json")
                    .to("direct:getExchangeRate");

        // ─── Ruta Camel ────────────────────────────────────────────────────────
        from("direct:getExchangeRate")
                .routeId("exchange-route")
                .log("ESB >>> [exchange-route] Consultando Exchange Service")
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .to(exchangeServiceUrl + "/api/exchange/usd?bridgeEndpoint=true")
                .convertBodyTo(String.class)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .log("ESB >>> [exchange-route] Respuesta recibida");
    }
}
