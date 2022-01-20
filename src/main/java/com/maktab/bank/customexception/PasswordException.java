package com.maktab.bank.customexception;

public class PasswordException extends RuntimeException{

    public PasswordException(String message) {
        super(message);
    }
}
