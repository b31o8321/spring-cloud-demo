package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mq")
public class MqController {
    @Autowired
    private StreamBridge streamBridge;

    @GetMapping("send")
    public boolean send(String msg) {
        return streamBridge.send("testProducerChannel-out-0", MessageBuilder.withPayload("hello MQ: " + msg).build());
    }
}
