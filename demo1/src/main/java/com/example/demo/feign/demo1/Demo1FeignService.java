package com.example.demo.feign.demo1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "demo1")
public interface Demo1FeignService {

    @GetMapping("/api/config")
    String config();
    @GetMapping("/api/service-info")
    String serviceInfo();
}
