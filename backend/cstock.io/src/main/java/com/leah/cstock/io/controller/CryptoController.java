package com.leah.cstock.io.controller;

import com.leah.cstock.io.dto.request.CryptoRequest;
import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.service.CryptoService;
import com.leah.cstock.io.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.DataOutput;
import java.util.UUID;

@RestController
public class CryptoController {
    private final UserService userService;
    private CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService, UserService userService) {
        this.cryptoService = cryptoService;
        this.userService = userService;
    }

    @GetMapping("/crypto")
    public ResponseEntity<CryptoResponse> getCryptoByname(@RequestParam(name = "name") String cryptoId) {
        return ResponseEntity.ok( cryptoService.getCryptoByName(cryptoId));
    }
    @PostMapping("{userId}/crypto")
    public ResponseEntity<CryptoRequest> addUserCryptoOnWallet(@PathVariable UUID userId, @RequestBody CryptoRequest cryptoRequest) {
        cryptoService.createNewCrypto(cryptoRequest, userId);
        return  ResponseEntity.ok(cryptoRequest);
    }
    @PutMapping("{userId}/crypto/{cryptoId}")
    public ResponseEntity<Double> updateUserCryptoOnWallet(@PathVariable UUID userId, @PathVariable long cryptoId, @RequestBody double cryptoAmount){
        cryptoService.updateCrypto(userId, cryptoAmount, cryptoId);
        return ResponseEntity.ok(cryptoAmount);
    }

}
