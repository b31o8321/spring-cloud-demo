package com.example.demo.controller;

import com.example.demo.feign.demo1.Demo1FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class CallDemo1Controller {
    @Autowired
    private Demo1FeignService demo1FeignService;
    @GetMapping("demo1/config")
    public String demo1Config() {
        return demo1FeignService.config();
    }

    @GetMapping("demo1/service-info")
    public String demo1ServiceInfo() {
        return demo1FeignService.serviceInfo();
    }
}
