package com.leah.cstock.io.exceptions.stock;

public class InvalidStockRequest extends  RuntimeException {
    public InvalidStockRequest(String message) {
        super(message);
    }
}
