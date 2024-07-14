package com.leah.cstock.io.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long idStock;
    private String stockName;
    private String stockPrice;
    private double stockAmount;
    private double totalUserStockValue;
    private String stockSymbol;
    private double userPnlValueStock;

}
