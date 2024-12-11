package com.demo.common.service.gateway.route;

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
import java.util.Map;

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

                String predicationPath = routeConfig.getPath().replaceAll("\\{\\w+\\}", "*");
                PredicateDefinition pathPredicateDefinition = new PredicateDefinition("Path=" + predicationPath);

                String method = routeConfig.getAlias().substring(routeConfig.getAlias().lastIndexOf(".") + 1).toUpperCase();
                if ("LIST".equals(method)) {
                    method = "GET";
                }
                PredicateDefinition methodPredicateDefinition = new PredicateDefinition("Method=" + method);

                routeDefinition.setPredicates(List.of(pathPredicateDefinition, methodPredicateDefinition));

                FilterDefinition rewritePathFilterDefinition = new FilterDefinition();
                rewritePathFilterDefinition.setName("RewritePath");
                Map<String, String> args = rewritePathFilterDefinition.getArgs();

                String frontendPath = "/api/(?<path>.*)";
                String backendPath = "/api/" + routeConfig.getServiceName() + "/${path}";
                args.put(RewritePathGatewayFilterFactory.REGEXP_KEY, frontendPath);
                args.put(RewritePathGatewayFilterFactory.REPLACEMENT_KEY, backendPath);
                routeDefinition.setFilters(List.of(rewritePathFilterDefinition));

                routeDefinitionList.add(routeDefinition);
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
