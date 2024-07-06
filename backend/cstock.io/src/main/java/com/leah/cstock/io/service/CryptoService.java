package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.repository.CryptoRepository;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CryptoService {

  private UserRepository userRepository;
  private CryptoRepository cryptoRepository;
  private UserService userService;
  private CoinCapService coinCapService;

    public CryptoService(CoinCapService coinCapService, UserService userService, CryptoRepository cryptoRepository, UserRepository userRepository) {
        this.coinCapService = coinCapService;
        this.userService = userService;
        this.cryptoRepository = cryptoRepository;
        this.userRepository = userRepository;
    }

    public CryptoResponse getCryptoByName(String cryptoId) {
     return coinCapService.getCrypto(cryptoId);
    }
}
