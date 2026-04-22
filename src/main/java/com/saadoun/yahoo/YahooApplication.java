package com.saadoun.yahoo;

import com.saadoun.yahoo.service.RedisTestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YahooApplication {

	public static void main(String[] args) {
        SpringApplication.run(YahooApplication.class, args);
	}

    @Bean
    CommandLineRunner run(RedisTestService redisTestService) {
        return args -> {
            redisTestService.test();
        };
    }

}
