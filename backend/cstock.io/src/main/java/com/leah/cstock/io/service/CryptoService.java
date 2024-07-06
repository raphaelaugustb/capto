package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.CryptoRequest;
import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.dto.response.Crypto.Data;
import com.leah.cstock.io.entity.Crypto;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.repository.CryptoRepository;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public double userCryptoValue(double cryptoAmount, String priceUsd){
        double userCryptoValuePerAmount = cryptoAmount * Double.parseDouble(priceUsd);
        return userCryptoValuePerAmount;
    }
    public CryptoResponse getCryptoByName(String cryptoId) {
     return coinCapService.getCrypto(cryptoId);
    }

    public void createNewCrypto(CryptoRequest cryptoRequest, UUID userId) {
        // Pegando as informaçoes da nossa request e da api externa
        String cryptoName = cryptoRequest.getCryptoName().toLowerCase();
        double cryptoAmount = cryptoRequest.getCryptoAmount();
        Data response = coinCapService.getCrypto(cryptoName).data();
        // Pegando nosso usuário da base de dados
        User userNotVerificated = userRepository.findById(userId).get();
        User userVerificated= userService.verifyUser(userNotVerificated);
        // Criando a entity crypto e setando os campos
        Crypto crypto = new Crypto();
        crypto.setCryptoName(response.name());
        crypto.setCryptoAmount(cryptoAmount);
        crypto.setCryptoPrice(response.priceUsd());
        crypto.setTotalUserCryptoValue(userCryptoValue(cryptoAmount,response.priceUsd()));
        crypto.setSymbol(response.symbol());
        //Adicionando a criptomoeda na lista do usuário e setando seu balanço
        userVerificated.getCryptoList().add(crypto);
        userVerificated.setUserBalance(userService.updateUserBalance(userVerificated.getCryptoList(), userVerificated.getStockList()));
        //Adicionando a criptomoeda no banco e atualizando o usuário
        cryptoRepository.save(crypto);
        userRepository.save(userVerificated);
    }
}
