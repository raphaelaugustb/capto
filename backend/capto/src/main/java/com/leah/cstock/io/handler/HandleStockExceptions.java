package com.leah.cstock.io.handler;

import com.leah.cstock.io.exceptions.crypto.CryptoNotFoundException;
import com.leah.cstock.io.exceptions.crypto.InvalidCryptoRequest;
import com.leah.cstock.io.exceptions.stock.InvalidStockRequest;
import com.leah.cstock.io.exceptions.stock.StockNotFoundException;
import com.leah.cstock.io.model.ModelHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandleStockExceptions extends ResponseEntityExceptionHandler {
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ModelHandler> handleStockNotFound(StockNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ModelHandler(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }
    @ExceptionHandler(InvalidStockRequest.class)
    public ResponseEntity<ModelHandler> handleInvalidStockRequest(InvalidStockRequest e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ModelHandler(HttpStatus.BAD_REQUEST, e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
