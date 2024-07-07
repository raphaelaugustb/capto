package com.leah.cstock.io.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID userID;
    private String username;
    private String password;
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Crypto> cryptoList;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Stock> stockList;
    private double userBalance;

}
