package com.leah.cstock.io.controller;

import com.leah.cstock.io.dto.request.StockRequest;
import com.leah.cstock.io.dto.response.Stock.StockResponse;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
public class StockController {
    StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stock")
    public ResponseEntity<StockResponse> getStockById(@RequestParam(name = "name") String stockName){
        return ResponseEntity.status(HttpStatus.FOUND).body(stockService.getStock(stockName));
    }
    @PostMapping("{userId}/stock")
    public ResponseEntity<StockRequest> createNewStockOnUserWallet(@PathVariable UUID userId,  @RequestBody StockRequest stockRequest){
        stockService.createNewStockUser(stockRequest,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockRequest);
    }

    @DeleteMapping("{userId}/stock")
    public  ResponseEntity<String> deleteStockOnUserWallet(@PathVariable UUID userId,  @RequestParam("name") String stockName){
        stockService.deleteUserStock(userId, stockName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Stock deleted successfully");
    }
    @GetMapping("{userId}/stock/list")
    public ResponseEntity<List<Stock>> getUserStockList(@PathVariable UUID userId){
        return ResponseEntity.status(HttpStatus.FOUND).body(stockService.getUserStockList(userId));
    }
    @GetMapping("{userId}/stock")
    public ResponseEntity<Stock> getUserStockByName(@PathVariable UUID userId,  @RequestParam("name") String stockName){
        return ResponseEntity.status(HttpStatus.FOUND).body(stockService.getStockOnUserList(userId, stockName));
    }
    @PutMapping("{userId}/stock")
    public  ResponseEntity<StockRequest> updateStockOnUserWallet(@PathVariable UUID userId, @RequestBody StockRequest stockRequest){
        stockService.updateStockOnUserList(userId, stockRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(stockRequest);
    }
}
