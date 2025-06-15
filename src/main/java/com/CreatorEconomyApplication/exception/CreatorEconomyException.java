package com.CreatorEconomyApplication.exception;


public class CreatorEconomyException extends RuntimeException {
    public CreatorEconomyException(String message) {
        super(message);
    }
    
    public CreatorEconomyException(String message, Throwable cause) {
        super(message, cause);
    }
}

