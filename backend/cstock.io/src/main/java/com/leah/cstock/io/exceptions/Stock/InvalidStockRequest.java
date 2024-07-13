package com.leah.cstock.io.exceptions.Stock;

public class InvalidStockRequest extends  RuntimeException {
    public InvalidStockRequest(String message) {
        super(message);
    }
}
