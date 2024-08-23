package com.leah.cstock.io.handler;

import com.leah.cstock.io.exceptions.crypto.CryptoNotFoundException;
import com.leah.cstock.io.exceptions.crypto.InvalidCryptoRequest;
import com.leah.cstock.io.model.ModelHandler;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandleCryptoExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ModelHandler> handleInternalServerError(FeignException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ModelHandler(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(CryptoNotFoundException.class)
    public ResponseEntity<ModelHandler> handleCryptoNotFound(CryptoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ModelHandler(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }
    @ExceptionHandler(InvalidCryptoRequest.class)
    public ResponseEntity<ModelHandler> handleInvalidCryptoRequest(InvalidCryptoRequest e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ModelHandler(HttpStatus.BAD_REQUEST, e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}