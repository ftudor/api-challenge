package com.ftudor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Bootstraps the Spring Boot com.ftudor.Application
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EntityScan(basePackages = {"com.ftudor"})
@EnableJpaRepositories(basePackages = {"com.ftudor"})
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
