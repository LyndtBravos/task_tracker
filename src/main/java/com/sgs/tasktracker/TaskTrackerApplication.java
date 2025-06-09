package com.sgs.tasktracker;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Arrays;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableMethodSecurity
public class TaskTrackerApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerApplication.class, args);
	}

	@PostConstruct
	public void logActiveProfile() {
		System.out.println("Redis host: " + env.getProperty("spring.redis.host"));
		System.out.println("Redis port: " + env.getProperty("spring.redis.port"));
		System.out.println("🔧 Active Spring profile: " + Arrays.toString(env.getActiveProfiles()));
	}
}