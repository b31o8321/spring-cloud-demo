package com.example.gateway.WebClient;

import com.example.gateway.vo.ValidateTokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserCenterWebClient {
    // 常量
    private final String BASE_URL = "lb://user-center";
    private final String VALIDATE_TOKEN_URL = "/api/user-center/auth/validate";

    @Autowired
    private LoadBalancedExchangeFilterFunction loadBalancedExchangeFilterFunction;

    public WebClient.ResponseSpec validateToken(ValidateTokenVO validateTokenVO) {
        return WebClient.builder()
                .baseUrl(BASE_URL + VALIDATE_TOKEN_URL)
                .filter(loadBalancedExchangeFilterFunction)
                .build()
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(validateTokenVO), ValidateTokenVO.class)
                .retrieve();
    }
}
