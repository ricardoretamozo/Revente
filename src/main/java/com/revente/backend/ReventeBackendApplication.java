package com.revente.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReventeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReventeBackendApplication.class, args);
    }

}
