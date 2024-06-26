package com.example.jdbctemplatepersisteddata.controllers;

import com.example.jdbctemplatepersisteddata.models.Purchase;
import com.example.jdbctemplatepersisteddata.repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseController(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    // We implement an endpoint a client calls to store a purchase record in the database. We use the
    // repository storePurchase() method to persist the data the controller’s action gets from the HTTP request body
    @PostMapping
    public void storePurchases(@RequestBody Purchase purchase) {
        purchaseRepository.storePurchase(purchase);
    }

    // We implement an endpoint the client calls to get all the records from the purchase table. The controller’s
    // action uses the repository’s method to get the data from the database and returns the data to the client in the
    // HTTP response body.
    @GetMapping
    public List<Purchase> findPurchases() {
        return purchaseRepository.findAllPurchases();
    }
}
