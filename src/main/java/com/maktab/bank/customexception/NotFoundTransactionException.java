package com.maktab.bank.customexception;

public class NotFoundTransactionException extends RuntimeException{

    public NotFoundTransactionException(String message) {
        super(message);
    }
}
