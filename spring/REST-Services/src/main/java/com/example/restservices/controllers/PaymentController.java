package com.example.restservices.controllers;

import com.example.restservices.models.PaymentDetails;
import com.example.restservices.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private static Logger logger = Logger.getLogger(PaymentController.class.getName());

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    // We get the payment details from the HTTP request body.
    public ResponseEntity<PaymentDetails> makePayment(@RequestBody PaymentDetails paymentDetails) {

        // We log the amount of the payment in the server’s console.
        logger.info("Received payment: " + paymentDetails.getAmount());

        // PaymentDetails paymentDetails = paymentService.processPayment();

        // We send back the payment details object
        // in the HTTP response body, and we set the
        // HTTP response status to 202 ACCEPTED
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(paymentDetails);
    }
}
