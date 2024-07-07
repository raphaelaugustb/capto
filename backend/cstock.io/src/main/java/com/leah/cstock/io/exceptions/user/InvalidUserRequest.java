package com.leah.cstock.io.exceptions.user;

public class InvalidUserRequest extends RuntimeException {
    public InvalidUserRequest(String message) {
        super(message);
    }
}
