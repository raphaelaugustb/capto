package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.CryptoRequest;
import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.dto.response.Crypto.Data;
import com.leah.cstock.io.entity.Crypto;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.repository.CryptoRepository;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CryptoService {

  private final UserRepository  userRepository;
  private final CryptoRepository cryptoRepository;
  private final UserService userService;
  private final CoinCapService coinCapService;

    public CryptoService(CoinCapService coinCapService, UserService userService, CryptoRepository cryptoRepository, UserRepository userRepository) {
        this.coinCapService = coinCapService;
        this.userService = userService;
        this.cryptoRepository = cryptoRepository;
        this.userRepository = userRepository;
    }
    public double userCryptoValue(double cryptoAmount, String priceUsd){
        return cryptoAmount * Double.parseDouble(priceUsd);
    }
    public CryptoResponse getCryptoByName(String cryptoId) {
     return coinCapService.getCrypto(cryptoId);
    }
    public Crypto verificateCrypto(long cryptoId){
        Crypto crypto = cryptoRepository.findById(cryptoId).get();
        if (crypto != null){
            return  crypto;
        } else {
            throw new NullPointerException("Crypto not found");
        }
    }

    public void createNewCrypto(CryptoRequest cryptoRequest, UUID userId) {

        // Pegando as informaçoes da nossa request e da api externa
        String cryptoName = cryptoRequest.getCryptoName().toLowerCase();
        double cryptoAmount = cryptoRequest.getCryptoAmount();
        Data response = coinCapService.getCrypto(cryptoName).data();
        // Pegando nosso usuário da base de dados
        User userVerified= userService.verifyUser(userId);

        // Criando a entity crypto e setando os campos
        Crypto crypto = new Crypto();
        crypto.setCryptoName(response.name());
        crypto.setCryptoAmount(cryptoAmount);
        crypto.setCryptoPrice(response.priceUsd());
        crypto.setTotalUserCryptoValue(userCryptoValue(cryptoAmount,response.priceUsd()));
        crypto.setSymbol(response.symbol());

        //Verificacao para determinar se a criptomeda ja está listada no repo do usuário
        boolean isCreateOnUserList = false;
        for (Crypto cryptoOnList :userVerified.getCryptoList() ) {
            if (cryptoOnList.getCryptoName().equals(response.name())) {
                isCreateOnUserList = true;
                break;
            }
        }

        // Verificando se ela está listada e executando o método
        if (isCreateOnUserList){
            for (Crypto cryptoOnList :userVerified.getCryptoList() ) {
                if (cryptoOnList.getCryptoName().equals(response.name())) {
                    // Atualizando a quantidade que o usuário e tem e o valor total
                    cryptoOnList.setCryptoAmount( cryptoOnList.getCryptoAmount() + cryptoAmount );
                   cryptoOnList.setTotalUserCryptoValue( cryptoOnList.getTotalUserCryptoValue() + userCryptoValue(cryptoAmount,response.priceUsd()));
                   cryptoOnList.setCryptoPrice(response.priceUsd());
                    //Atualizando o saldo do usuário
                    userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
                    //Atualizando a criptomoeda no banco e atualizando o usuário
                    cryptoRepository.save(cryptoOnList);
                    userRepository.save(userVerified);
                    break;
                }
            }
        } else {
            //Adicionando a criptomoeda na lista do usuário e setando seu balanço
            userVerified.getCryptoList().add(crypto);
            userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
            //Adicionando a criptomoeda no banco e atualizando o usuário
            cryptoRepository.save(crypto);
            userRepository.save(userVerified);
        }

    }
    public void deleteCryptoById(UUID userId, String cryptoName) {
        long cryptoId = 0;
        String formatedCryptoName = String.valueOf(cryptoName.charAt(0)).toUpperCase() + cryptoName.substring(1);
        User userVerified = userService.verifyUser(userId);
        for (Crypto c: userVerified.getCryptoList()){
            if (c.getCryptoName().equals(formatedCryptoName)){
                cryptoId = c.getIdCrypto();
                userVerified.getCryptoList().remove(c);
                userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
                userRepository.save(userVerified);
                break;
            }
        }
        Crypto cryptoDelete = verificateCrypto(cryptoId);
        cryptoRepository.delete(cryptoDelete);
    }
    public void updateCrypto(UUID userId, Double cryptoAmount, String cryptoName) {
        long cryptoId = 0;
        User userVerified = userService.verifyUser(userId);
        String actualCryptoPrice = coinCapService.getCrypto(cryptoName).data().priceUsd();
        String formatedCryptoName = String.valueOf(cryptoName.charAt(0)).toUpperCase() + cryptoName.substring(1);
        for (Crypto c: userVerified.getCryptoList()){
            if (c.getCryptoName().equals(formatedCryptoName)){
                cryptoId = c.getIdCrypto();
                c.setCryptoAmount(cryptoAmount);
                c.setTotalUserCryptoValue(userCryptoValue(cryptoAmount, actualCryptoPrice));
                userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
                userRepository.save(userVerified);
                break;
            }
        }
        Crypto crypto = verificateCrypto(cryptoId);

        crypto.setCryptoAmount(cryptoAmount);

        crypto.setTotalUserCryptoValue(userCryptoValue(cryptoAmount, actualCryptoPrice));
        cryptoRepository.save(crypto);
    }
    public List<Crypto> getUserCryptoList(UUID userId){
        User userVerified = userService.verifyUser(userId);
        return  userVerified.getCryptoList();
    }
    public Crypto getUserCryptoByName(UUID userId, String cryptoName) {
        String formatedCryptoName = String.valueOf(cryptoName.charAt(0)).toUpperCase() + cryptoName.substring(1);
        User userVerified = userService.verifyUser(userId);
        Crypto userCrypto = null;
        for(Crypto c: userVerified.getCryptoList()){
            if (c.getCryptoName().equals(formatedCryptoName)) {
                userCrypto = c;
                break;
            }
        }
        return userCrypto;
    }
}
