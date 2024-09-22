package org.example.pongguel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PongguelApplication {

    public static void main(String[] args) {
        SpringApplication.run(PongguelApplication.class, args);
    }

}
