package com.example.springdatajdbc.exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException() {
        super("The Account is not found!");
    }
}
