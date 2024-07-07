package com.leah.cstock.io.controller;

import com.leah.cstock.io.dto.response.Stock.StockResponse;
import com.leah.cstock.io.service.StockService;
import feign.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stock")
    public ResponseEntity<StockResponse> getStockById(@RequestParam(name = "name") String stockName){
        return ResponseEntity.ok(stockService.getStock(stockName));
    }
}
