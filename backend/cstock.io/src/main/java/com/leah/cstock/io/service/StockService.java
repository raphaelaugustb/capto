package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.response.Stock.StockResponse;
import com.leah.cstock.io.repository.StockRepository;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    @Value("#{environment.TOKEN}")
    private String TOKEN;

    BrapiService brapiService;
    StockRepository stockRepository;
    UserRepository userRepository;
    UserService userService;

    public StockService(BrapiService brapiService, StockRepository stockRepository, UserRepository userRepository, UserService userService) {
        this.brapiService = brapiService;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public StockResponse getStock(String stockSymbol) {
        StockResponse stock = brapiService.getStockById(stockSymbol, TOKEN);
        return stock;
    }
}
