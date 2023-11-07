package com.example.restservices.services;

import com.example.restservices.exceptions.NotEnoughMoneyException;
import com.example.restservices.models.PaymentDetails;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public PaymentDetails processPayment() {
        throw new NotEnoughMoneyException("Not enough money to make the payment");
    }
}
