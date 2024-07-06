package com.leah.cstock.io.controller;

import com.leah.cstock.io.dto.request.CryptoRequest;
import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CryptoController {
    private CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
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

}
