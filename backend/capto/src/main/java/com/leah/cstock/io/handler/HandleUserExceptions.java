
package com.leah.cstock.io.handler;
import com.leah.cstock.io.exceptions.stock.InvalidStockRequest;
import com.leah.cstock.io.exceptions.stock.StockNotFoundException;
import com.leah.cstock.io.exceptions.user.InvalidUserRequest;
import com.leah.cstock.io.exceptions.user.UserAlreadyExistException;
import com.leah.cstock.io.exceptions.user.UserNotFoundException;
import com.leah.cstock.io.model.ModelHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandleUserExceptions extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ModelHandler> handlerUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ModelHandler(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }
    @ExceptionHandler(InvalidUserRequest.class)
    public ResponseEntity<ModelHandler> handleInvalidUserRequest(InvalidUserRequest e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ModelHandler(HttpStatus.BAD_REQUEST, e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ModelHandler> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ModelHandler(HttpStatus.CONFLICT, e.getMessage(), HttpStatus.CONFLICT.value()));
    }
}
