package com.example.elib.common.exception;

public class ReferentialIntegrityException extends RuntimeException {
    public ReferentialIntegrityException(String message) {
        super(message);
    }
    public ReferentialIntegrityException(String message, Throwable cause){
        super(message, cause);
    }

}
