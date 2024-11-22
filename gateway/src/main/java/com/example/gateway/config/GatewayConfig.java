package com.example.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.example.gateway.route.NacosRouteLocator;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {
    @Bean
    RouteDefinitionLocator nacosRouteLocator(NacosConfigManager nacosConfigManager) {
        return new NacosRouteLocator(nacosConfigManager, "routes", "DEFAULT_GROUP");
    }
}
