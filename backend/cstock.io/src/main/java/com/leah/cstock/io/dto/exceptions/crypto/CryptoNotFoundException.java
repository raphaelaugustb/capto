package com.leah.cstock.io.dto.exceptions.crypto;

public class CryptoNotFoundException extends RuntimeException {
    public CryptoNotFoundException(String message) {
        super(message);
    }
}
