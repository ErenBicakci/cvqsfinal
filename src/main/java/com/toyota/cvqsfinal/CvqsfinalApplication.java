package com.toyota.cvqsfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CvqsfinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvqsfinalApplication.class, args);
    }

}
