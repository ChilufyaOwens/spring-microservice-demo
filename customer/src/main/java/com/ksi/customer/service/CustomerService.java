package com.ksi.customer.service;

import com.ksi.amqp.RabbitMQMessageProducer;
import com.ksi.clients.fraud.FraudCheckResponse;
import com.ksi.clients.fraud.FraudClient;
import com.ksi.clients.notification.NotificationRequest;
import com.ksi.customer.Customer;
import com.ksi.customer.dto.CustomerRegistrationRequest;
import com.ksi.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to KSI...", customer.getFirstName())
        );
        // notificationClient.sendNotification(notificationRequest); no longer sending notifications directly
        rabbitMQMessageProducer.publish(notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
