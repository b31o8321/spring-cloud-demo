package com.example.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NacosConfigParse {
    public static List<RouteDefinition> parse(String config) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<NacosRouteConfig> routeConfigList = objectMapper.readValue(config, objectMapper.getTypeFactory().constructCollectionType(List.class, NacosRouteConfig.class));
            List<RouteDefinition> routeDefinitionList = new ArrayList<>();
            for (NacosRouteConfig routeConfig : routeConfigList) {
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.setId(routeConfig.getName());
                routeDefinition.setUri(URI.create("lb://" + routeConfig.getServiceName()));

                String pathPatten = "/api/" + routeConfig.getServiceName() + routeConfig.getPath().substring(4);
                PredicateDefinition path = new PredicateDefinition("Path=" + pathPatten);
                PredicateDefinition method = new PredicateDefinition("Method=" + routeConfig.getAlias().substring(routeConfig.getAlias().lastIndexOf(".") + 1).toUpperCase());
                routeDefinition.setPredicates(List.of(path, method));

                FilterDefinition rewritePath = new FilterDefinition();
                rewritePath.setName("RewritePath");
                rewritePath.getArgs().put(RewritePathGatewayFilterFactory.REGEXP_KEY, pathPatten);
                rewritePath.getArgs().put(RewritePathGatewayFilterFactory.REPLACEMENT_KEY, routeConfig.getPath());
                routeDefinition.setFilters(List.of(rewritePath));

                routeDefinitionList.add(routeDefinition);

                ArrayList<Object> objects = new ArrayList<>();
                objects.clear();
            }

            return routeDefinitionList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    private static class NacosRouteConfig {
        private String name;
        private String path;
        private String serviceName;
        private String alias;
    }
}
