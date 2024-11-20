package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class RocketChannel {

    @Bean
    public Consumer<Message<String>> testConsumerChannel()
    {
        return message -> {
            System.out.println("testChannel payload: " + message.getPayload());
            System.out.println("testChannel header: " + message.getHeaders().get("ROCKET_MQ_TOPIC"));
        };
    }

    @Bean
    public Supplier<Message<String>> testProducerChannel()
    {
        return () -> {
            System.out.println("testChannel Producer do.");
            return MessageBuilder.withPayload("hello world").build();
        };
    }

}
