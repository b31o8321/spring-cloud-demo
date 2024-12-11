package com.demo.common.service.user.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {
    @Bean
    public Snowflake snowflake() {
        long workerId = 0;
        long dataCenterId = 0;
        try {
            // 根据机器的IP地址获取workerId
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()) & 31L;
        } catch (Exception e) {
            // 如果获取失败，使用默认值0
            workerId = 0L;
        }
        return IdUtil.getSnowflake(workerId, dataCenterId);
    }
}
