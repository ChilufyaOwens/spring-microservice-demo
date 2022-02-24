package com.ksi.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NotificationDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationDemoApplication.class, args);
    }
}
