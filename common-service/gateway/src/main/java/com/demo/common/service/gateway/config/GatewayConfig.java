package com.demo.common.service.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.demo.common.service.gateway.route.NacosRouteLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    RouteDefinitionLocator nacosRouteLocator(NacosConfigManager nacosConfigManager) {
        return new NacosRouteLocator(nacosConfigManager, "routes", "DEFAULT_GROUP");
    }
}
