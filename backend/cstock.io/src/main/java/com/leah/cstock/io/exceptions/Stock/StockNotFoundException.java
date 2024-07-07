package com.leah.cstock.io.exceptions.Stock;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
