package com.maktab.bank.customexception;

public class NotFoundWalletException extends RuntimeException{

    public NotFoundWalletException(String message) {
        super(message);
    }
}
