package com.example.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

import java.util.List;

public class NacosRouteLocator implements RouteDefinitionLocator {
    private NacosConfigManager nacosConfigManager;
    private String dataId;
    private String group;

    public NacosRouteLocator(NacosConfigManager nacosConfigManager, String dataId, String group) {
        this.nacosConfigManager = nacosConfigManager;
        this.dataId = dataId;
        this.group = group;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        ConfigService configService = nacosConfigManager.getConfigService();
        try {
            String config = configService.getConfig(dataId, group, 5000);
            List<RouteDefinition> nacosRouteDefinitions = NacosConfigParse.parse(config);
            return Flux.fromIterable(nacosRouteDefinitions);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
