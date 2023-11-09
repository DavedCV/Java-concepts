package com.example.consumingrestendpointsresttemplate.controller;

import com.example.consumingrestendpointsresttemplate.models.Payment;
import com.example.consumingrestendpointsresttemplate.proxy.PaymentProxy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentProxy paymentsProxy;

    public PaymentController(PaymentProxy paymentsProxy) {
        this.paymentsProxy = paymentsProxy;
    }

    @PostMapping("/payment")
    public Payment createPayment(
            @RequestBody Payment payment
    )
    {
        return paymentsProxy.createPayment(payment);
    }
}
