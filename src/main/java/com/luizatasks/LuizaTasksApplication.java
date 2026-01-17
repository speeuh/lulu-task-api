package com.luizatasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LuizaTasksApplication {
    public static void main(String[] args) {
        SpringApplication.run(LuizaTasksApplication.class, args);
    }
}

