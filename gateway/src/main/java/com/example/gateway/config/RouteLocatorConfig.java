package com.example.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteLocatorConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes().route("demo1-health-check", r -> r.path("/api/demo1-health-check")
                .filters(f -> f.rewritePath("/api/demo1-health-check", "/api/health-check"))
                .uri("http://localhost:8090")).build();
    }

    @Bean
    RouteDefinitionLocator nacosRouteLocator(NacosConfigManager nacosConfigManager) {
        return new NacosRouteLocator(nacosConfigManager, "routes", "DEFAULT_GROUP");
    }
}
