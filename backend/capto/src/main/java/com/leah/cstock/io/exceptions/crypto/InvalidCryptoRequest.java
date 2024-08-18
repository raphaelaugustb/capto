package com.leah.cstock.io.exceptions.crypto;

public class InvalidCryptoRequest extends  RuntimeException {
    public InvalidCryptoRequest(String message) {
        super(message);
    }
}
