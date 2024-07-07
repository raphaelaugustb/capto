package com.leah.cstock.io.dto.exceptions.Stock;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
