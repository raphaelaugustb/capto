package com.leah.cstock.io.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ModelHandler {
    private HttpStatus status;
    private String message;
    private int code;
}
