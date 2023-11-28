package com.example.springbootbigquery.exceptions;

public class BigQueryManualException extends Exception {
    public BigQueryManualException(String message) {
        super(message);
    }

    public BigQueryManualException(String message, Exception exception) {
        super(message, exception);
    }
}
