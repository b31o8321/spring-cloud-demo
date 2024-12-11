package com.demo.common.service.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.spring.webflux.exception.SentinelBlockExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class SentinelConfig {
//    public SentinelConfig() {
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        GatewayFlowRule rule = new GatewayFlowRule("Login");
//        rule.setCount(1.0); // 设置流量限制数量，例如每秒允许通过1个请求
//        rule.setIntervalSec(30); // 设置统计间隔时间，单位为秒
//        rules.add(rule);
//        GatewayRuleManager.loadRules(rules);
//    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelBlockExceptionHandler sentinelBlockExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        return new SentinelBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }
}
