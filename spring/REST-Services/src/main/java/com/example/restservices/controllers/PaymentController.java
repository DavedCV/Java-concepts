package com.example.restservices.controllers;

import com.example.restservices.exceptions.NotEnoughMoneyException;
import com.example.restservices.models.ErrorDetails;
import com.example.restservices.models.PaymentDetails;
import com.example.restservices.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> makePayment() {
        try {
            PaymentDetails paymentDetails = paymentService.processPayment();

            // If calling the service method succeeds,
            // we return an HTTP response with status
            // Accepted and the PaymentDetails
            // instance as a response body.
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(paymentDetails);
        } catch (NotEnoughMoneyException e) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setMessage(e.getMessage());

            // If an exception of type NotEnoughMoneyException is
            // thrown, we return an HTTP response with status Bad
            // Request and an ErrorDetails instance as a body.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorDetails);
        }
    }
}
