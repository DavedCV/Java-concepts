package com.example.springdatajdbc.services;

import com.example.springdatajdbc.exceptions.AccountNotFoundException;
import com.example.springdatajdbc.models.Account;
import com.example.springdatajdbc.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(
            int idSender,
            int idReceiver,
            BigDecimal amount
    )
    {
        // We get the sender and receiver’s account details.
        Account sender = accountRepository.findById(idSender).orElseThrow(AccountNotFoundException::new);
        Account receiver = accountRepository.findById(idReceiver).orElseThrow(AccountNotFoundException::new);

        // We calculate the new account amounts
        // by subtracting the transferred value
        // from the sender account and adding it
        // to the destination account.
        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAmount().add(amount);

        // We change the accounts’ amounts in the database.
        accountRepository.changeAmount(idSender, senderNewAmount);
        accountRepository.changeAmount(idReceiver, receiverNewAmount);
    }

    // AccountRepository inherits this method from the Spring Data CrudRepository interface.
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountsByName(String name) {
        return accountRepository.findAccountsByName(name);
    }
}
