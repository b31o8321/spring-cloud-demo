package com.example.demo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.demo.config.NacosConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/")
@Slf4j
public class IndexController {
    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("health-check")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping(value = "/sentinel-test")
    @SentinelResource("sentinel-test")
    public String hello() {
        return "Hello Sentinel";
    }

    @GetMapping("redisson-test")
    public String redissonInfo() {
        // 设置一个redis key：test，value 为 "Hello, Redis!"
        log.info("begin time: {}", System.currentTimeMillis());
        String lockName = "redisson-test";
        RLock lock = redissonClient.getLock(lockName);
        log.info("get lock time: {}", System.currentTimeMillis());

        long startTime = System.currentTimeMillis();
        long endTime = startTime + 30 * 1000;

        while (System.currentTimeMillis() < endTime) {
            try {
                // 尝试获取锁，如果立即可用则获取锁并返回 true；否则返回 false
                log.info("begin try lock time: {}", System.currentTimeMillis());
                boolean isLocked = lock.tryLock(10, TimeUnit.SECONDS);
                log.info("end try lock time: {}", System.currentTimeMillis());
                if (isLocked) {
                    log.info("Acquired lock for task '{}' at {}", lockName, System.currentTimeMillis());

                    // 执行关键操作
                try {
                        Thread.sleep(3000); // 模拟操作执行时间
                        return "Task completed";
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                    log.info("Completed task '{}' at {}", lockName, System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while waiting for lock", e);
            } finally {
                try {
                    Thread.sleep(100); // 休眠 100 毫秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Interrupted while sleeping", e);
                }
            }
        }

        return "Lock released";
    }

    @GetMapping("redis-test")
    public String redisInfo() {
        String key = "testKey";
        String value = "Hello, Redis!";

        // 设置键值对
        redisTemplate.opsForValue().set(key, value);

        // 获取键值对
        String result = (String) redisTemplate.opsForValue().get(key);
        return "Result from Redis: " + result;
    }

    @GetMapping("nacos-test")
    public String config() {
        return nacosConfig.toString();
    }

    @GetMapping("service-info")
    public String serviceInfo(HttpServletRequest request) {

        // sleep 300s
//        try {
//            Thread.sleep(300000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return request.getRequestURI() + ":" + request.getServerPort();
    }


}
