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
    public Crypto verificateCrypto(long cryptoId){
        Crypto cryptoNotVerificated = cryptoRepository.findById(cryptoId).get();
        if (cryptoNotVerificated != null){
            Crypto cryptoVerificated = cryptoNotVerificated;
            return  cryptoVerificated;
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
        User userNotVerificated = userRepository.findById(userId).get();
        User userVerificated= userService.verifyUser(userNotVerificated);

        // Criando a entity crypto e setando os campos
        Crypto crypto = new Crypto();
        crypto.setCryptoName(response.name());
        crypto.setCryptoAmount(cryptoAmount);
        crypto.setCryptoPrice(response.priceUsd());
        crypto.setTotalUserCryptoValue(userCryptoValue(cryptoAmount,response.priceUsd()));
        crypto.setSymbol(response.symbol());

        //Verificacao para determinar se a criptomeda ja está listada no repo do usuário
        boolean isCreateOnUserList = false;
        for (Crypto cryptoOnList :userVerificated.getCryptoList() ) {
            if (cryptoOnList.getCryptoName().equals(response.name())) {
                isCreateOnUserList = true;
                break;
            }
        }

        // Verificando se ela está listada e executando o método
        if (isCreateOnUserList == true){
            for (Crypto cryptoOnList :userVerificated.getCryptoList() ) {
                if (cryptoOnList.getCryptoName().equals(response.name())) {
                    // Atualizando a quantidade que o usuário e tem e o valor total
                    cryptoOnList.setCryptoAmount( cryptoOnList.getCryptoAmount() + cryptoAmount );
                   cryptoOnList.setTotalUserCryptoValue( cryptoOnList.getTotalUserCryptoValue() + userCryptoValue(cryptoAmount,response.priceUsd()));
                   cryptoOnList.setCryptoPrice(response.priceUsd());
                    //Atualizando o saldo do usuário
                    userVerificated.setUserBalance(userService.updateUserBalance(userVerificated.getCryptoList(), userVerificated.getStockList()));
                    //Atualizando a criptomoeda no banco e atualizando o usuário
                    cryptoRepository.save(cryptoOnList);
                    userRepository.save(userVerificated);
                }
            }
        } else {
            //Adicionando a criptomoeda na lista do usuário e setando seu balanço
            userVerificated.getCryptoList().add(crypto);
            userVerificated.setUserBalance(userService.updateUserBalance(userVerificated.getCryptoList(), userVerificated.getStockList()));
            //Adicionando a criptomoeda no banco e atualizando o usuário
            cryptoRepository.save(crypto);
            userRepository.save(userVerificated);
        }

    }
    public void deleteCryptoById(UUID idUser, long cryptoId) {
        Crypto crypto = verificateCrypto(cryptoId);
        User userNotVerified = userRepository.findById(idUser).get();
        User userVerificated = userService.verifyUser(userNotVerified);
        for (Crypto c: userVerificated.getCryptoList()){
            if (c.getCryptoName().equals(crypto.getCryptoName())) {
                userVerificated.getCryptoList().remove(c);
                userRepository.save(userVerificated);
                break;
            }
        }
        cryptoRepository.delete(crypto);
    }
    public void updateCrypto(UUID idUser, Double cryptoAmount, long cryptoId) {
        Crypto crypto = verificateCrypto(cryptoId);
        User userNotVerified = userRepository.findById(idUser).get();
        User userVerificated = userService.verifyUser(userNotVerified);
        crypto.setCryptoAmount(cryptoAmount);
        for (Crypto c: userVerificated.getCryptoList()){
            if (c.getCryptoName().equals(crypto.getCryptoName())) {
                c.setCryptoAmount(cryptoAmount);
                c.setTotalUserCryptoValue(userCryptoValue(cryptoAmount, crypto.getCryptoPrice()));
                userVerificated.setUserBalance(userService.updateUserBalance(userVerificated.getCryptoList(), userVerificated.getStockList()));
                userRepository.save(userVerificated);
                break;
            }
        }
        crypto.setTotalUserCryptoValue(userCryptoValue(cryptoAmount, crypto.getCryptoPrice()));
        cryptoRepository.save(crypto);
    }
}
