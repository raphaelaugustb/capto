package com.leah.cstock.io.dto.exceptions.user;

public class InvalidUserRequest extends RuntimeException {
    public InvalidUserRequest(String message) {
        super(message);
    }
}
