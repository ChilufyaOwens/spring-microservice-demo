package com.ksi.customer.service;

import com.ksi.clients.fraud.FraudCheckResponse;
import com.ksi.clients.fraud.FraudClient;
import com.ksi.clients.notification.NotificationClient;
import com.ksi.clients.notification.NotificationRequest;
import com.ksi.customer.Customer;
import com.ksi.customer.dto.CustomerRegistrationRequest;
import com.ksi.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository,
                              RestTemplate restTemplate,
                              FraudClient fraudClient,
                              NotificationClient notificationClient) {

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
        notificationClient.sendNotification(notificationRequest);
    }
}
