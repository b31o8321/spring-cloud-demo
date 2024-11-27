package com.example.gateway.filter;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.example.gateway.WebClient.UserCenterWebClient;
import com.example.gateway.vo.ResponseVO;
import com.example.gateway.vo.ValidateTokenVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class jwtFilter implements GlobalFilter, Ordered {
    @Autowired
    private UserCenterWebClient userCenterWebClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        org.springframework.http.HttpHeaders headers = request.getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);


        if ((request.getURI().getPath().equals("/api/auth/login") || request.getURI().getPath().equals("/api/users")) && request.getMethod().equals(HttpMethod.POST)) {
            return chain.filter(exchange);
        }

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String token = authHeader.substring(7);

        WebClient.ResponseSpec responseSpec = userCenterWebClient.validateToken(new ValidateTokenVO(request.getURI().getPath(), token));
        return responseSpec .bodyToMono(ResponseVO.class)
                .flatMap(responseVO -> {
                    if (!"200000".equals(responseVO.getCode()) || StringUtils.isEmpty((String) responseVO.getData())) {
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.setComplete();
                    } else {
                        String newJwt = (String) responseVO.getData();
                        ServerHttpRequest newRequest = request.mutate()
                                .header("Authorization", "Bearer " + newJwt)
                                .build();
                        ServerWebExchange newExchange = exchange.mutate()
                                .request(newRequest)
                                .build();
                        return chain.filter(newExchange);
                    }
                });
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
