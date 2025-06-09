package com.sgs.tasktracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestRunner implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public RedisTestRunner(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            redisTemplate.opsForValue().set("connectivity-test", "ok");
            String result = redisTemplate.opsForValue().get("connectivity-test");

            if ("ok".equals(result)) {
                System.out.println("✅ Successfully connected to Redis and performed a read/write.");
            } else {
                System.out.println("⚠️ Connected to Redis, but unexpected result: " + result);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to Redis:");
            System.out.println("===============================");
            e.printStackTrace();
        }
    }
}