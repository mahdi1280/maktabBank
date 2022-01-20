package com.maktab.bank.customexception;

public class TryPasswordException extends RuntimeException{

    public TryPasswordException(String message) {
        super(message);
    }
}
