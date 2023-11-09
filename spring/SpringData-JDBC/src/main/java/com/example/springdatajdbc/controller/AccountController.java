package com.example.springdatajdbc.controller;

import com.example.springdatajdbc.dto.TransferRequest;
import com.example.springdatajdbc.models.Account;
import com.example.springdatajdbc.services.TransferService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountController {
    private final TransferService transferService;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public void transferMoney(
            @RequestBody TransferRequest transferRequest
    )
    {
        transferService.transferMoney(transferRequest.getSenderAccountId(),
                                      transferRequest.getReceiverAccountId(),
                                      transferRequest.getAmount());
    }

    @GetMapping("/accounts")
    public Iterable<Account> getAllAccounts(
            @RequestParam(required = false) String name
    )
    {
        if (name == null) {
            return transferService.getAllAccounts();
        }

        return transferService.findAccountsByName(name);

    }
}
