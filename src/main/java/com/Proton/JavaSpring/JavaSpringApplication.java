package com.Proton.JavaSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.Proton.JavaSpring")
public class JavaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringApplication.class, args);
    }

}
