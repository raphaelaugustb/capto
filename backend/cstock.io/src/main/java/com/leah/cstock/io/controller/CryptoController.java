package com.leah.cstock.io.controller;

import com.leah.cstock.io.dto.request.CryptoRequest;
import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.entity.Crypto;
import com.leah.cstock.io.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/crypto")
    public ResponseEntity<CryptoResponse> getCryptoByname(@RequestParam(name = "name") String cryptoId) {
        return ResponseEntity.ok( cryptoService.getCryptoByName(cryptoId));
    }
    @GetMapping("{userId}/crypto/list")
    public ResponseEntity<List<Crypto>> getUserCryptoList(@PathVariable UUID userId) {
        return ResponseEntity.ok(cryptoService.getUserCryptoList(userId));
    }
    @GetMapping("{userId}/crypto")
    public ResponseEntity<Crypto> getUserCryptoOnWallet(@PathVariable UUID userId, @RequestParam(name = "name") String cryptoName) {
        return ResponseEntity.ok(cryptoService.getUserCryptoByName(userId, cryptoName));
    }
    @PostMapping("{userId}/crypto")
    public ResponseEntity<CryptoRequest> addUserCryptoOnWallet(@PathVariable UUID userId, @RequestBody CryptoRequest cryptoRequest) {
        cryptoService.createNewCrypto(cryptoRequest, userId);
        return  ResponseEntity.ok(cryptoRequest);
    }
    @PutMapping("{userId}/crypto")
    public ResponseEntity<Double> updateUserCryptoOnWallet(@PathVariable UUID userId, @RequestParam(name = "name") String cryptoName, @RequestBody double cryptoAmount){
        cryptoService.updateCrypto(userId, cryptoAmount, cryptoName);
        return ResponseEntity.ok(cryptoAmount);
    }
    @DeleteMapping("{userId}/crypto")
    public ResponseEntity<String> deleteUserCryptoOnWallet(@PathVariable UUID userId, @RequestParam(name = "name") String cryptoName){
        cryptoService.deleteCryptoById(userId, cryptoName);
        return ResponseEntity.ok("Deleted user crypto:" + cryptoName);
    }

}
