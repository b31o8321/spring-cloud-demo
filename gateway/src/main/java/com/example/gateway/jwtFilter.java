package com.example.gateway;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class jwtFilter extends AbstractGatewayFilterFactory<jwtFilter.Config> {

    public jwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String jwtToken = request.getHeaders().getFirst("Authorization");

            if (jwtToken == null && !request.getURI().getPath().equals("/api/auth/login")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return Mono.empty();
            } else if (jwtToken != null) {
                // call user center to validate jwt token
                WebClient.create("http://user-center/api/validate-jwt")
                        .post()
                        .bodyValue(jwtToken)
                        .retrieve()
                        .bodyToMono(Boolean)
                        .flatMap(valid -> {
                            if (valid) {
                                return chain.filter(exchange);
                            } else {
                                ServerHttpResponse response = exchange.getResponse();
                                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                                return Mono.empty();
                            }
                        });
            }

            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private String jwtValidationEndpoint;
    }
}
