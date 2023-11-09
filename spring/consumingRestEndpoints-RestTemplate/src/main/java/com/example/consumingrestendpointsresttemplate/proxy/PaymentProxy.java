package com.example.consumingrestendpointsresttemplate.proxy;

import com.example.consumingrestendpointsresttemplate.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@Component
public class PaymentProxy {
    private final RestTemplate rest;

    // We take the URL to the payment
    // service from the properties file.
    @Value("${name.service.url}")
    private String paymentsServiceURL;

    // We inject the RestTemplate from the
    // Spring context using constructor DI.
    @Autowired
    public PaymentProxy(RestTemplate rest) {
        this.rest = rest;
    }

    public Payment createPayment(Payment payment) {
        String uri = paymentsServiceURL + "/payment";

        // We build the HttpHeaders object to
        // define the HTTP request headers.
        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", UUID.randomUUID().toString());

        // We build the HttpEntity object
        // to define the request data.
        HttpEntity<Payment> httpEntity = new HttpEntity<>(payment, headers);

        // We send the HTTP request
        // and retrieve the data on
        // the HTTP response.
        ResponseEntity<Payment> response = rest.exchange(uri, HttpMethod.POST, httpEntity, Payment.class);

        // We return the HTTP response body.
        return response.getBody();
    }
}
