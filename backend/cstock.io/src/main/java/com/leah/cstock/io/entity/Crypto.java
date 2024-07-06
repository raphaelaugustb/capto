package com.leah.cstock.io.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Crypto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idStock;
    private String cryptoName;
    private String cryptoPrice;
    private double cryptoAmount;
    private double totalUserCryptoValue;
    private String symbol;

    public Crypto(long idStock, String cryptoName, String cryptoPrice, double cryptoAmount, double totalUserCryptoValue, String symbol) {
        this.idStock = idStock;
        this.cryptoName = cryptoName;
        this.cryptoPrice = cryptoPrice;
        this.cryptoAmount = cryptoAmount;
        this.totalUserCryptoValue = totalUserCryptoValue;
        this.symbol = symbol;
    }

    public Crypto() {
    }
}
