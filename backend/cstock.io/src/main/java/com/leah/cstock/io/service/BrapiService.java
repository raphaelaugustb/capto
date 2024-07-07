package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.response.Stock.StockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "brapiservice", url = "https://brapi.dev")
public interface BrapiService {
    @GetMapping("/api/quote/{ticker}")
    StockResponse getStockById(@PathVariable("ticker") String ticker, @RequestParam(name ="token") String token);
}
