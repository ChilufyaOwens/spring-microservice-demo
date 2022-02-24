package com.ksi.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(
        scanBasePackages = {
                "com.ksi.notification",
                "com.ksi.amqp"
        }
)
@EnableEurekaClient
public class NotificationDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationDemoApplication.class, args);
    }

   /* @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer,
                                        NotificationConfig notificationConfig) {
        return args -> {
            producer.publish(
                    new Person("Chilufya Owens", 34 ),
                    notificationConfig.getInternalExchange(),
                    notificationConfig.getInternalNotificationRoutingKey());
        };
    }*/
}
