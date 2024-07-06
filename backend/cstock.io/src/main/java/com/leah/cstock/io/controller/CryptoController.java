package com.leah.cstock.io.controller;

import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import com.leah.cstock.io.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
