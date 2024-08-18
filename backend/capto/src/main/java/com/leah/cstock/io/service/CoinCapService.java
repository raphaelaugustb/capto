package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.response.Crypto.CryptoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "coincap", url = "https://api.coincap.io")
public interface CoinCapService {

    @GetMapping("/v2/assets/{id}")
    CryptoResponse getCrypto(@PathVariable("id") String id);
}
