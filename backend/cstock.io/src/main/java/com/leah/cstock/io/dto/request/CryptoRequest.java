package com.leah.cstock.io.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoRequest {
    private String cryptoName;
    private String cryptoSymbol;
    private double doubleCryptoAmount;
}
