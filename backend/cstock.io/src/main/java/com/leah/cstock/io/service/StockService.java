package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.StockRequest;
import com.leah.cstock.io.dto.response.Stock.Results;
import com.leah.cstock.io.dto.response.Stock.StockResponse;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.repository.StockRepository;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    public double calcStockUserValue(double amount, double regularMarketPrice){
        double userStockValue = amount * regularMarketPrice;
        return userStockValue;
    }
    public void createNewStockUser(StockRequest stockRequest, UUID userId) {
        User notverifiedUser = userRepository.findById(userId).get();

        User userVerified = userService.verifyUser(notverifiedUser);
        boolean stockIsOnUserList = false;
        String stockName = stockRequest.getStockName();
        Double stockAmount = stockRequest.getStockAmount();
        Results response = brapiService.getStockById(stockName, TOKEN).results().getFirst();
        Stock newStock = new Stock();
        newStock.setStockAmount(stockAmount);
        newStock.setStockName(stockName);
        newStock.setSymbol(response.symbol());
        newStock.setStockPrice(String.valueOf(response.regularMarketPrice()));
        newStock.setTotalUserStockValue(calcStockUserValue(stockAmount, response.regularMarketPrice()));
        for (Stock c : userVerified.getStockList()) {
            if (c.getStockName().equals(stockRequest.getStockName())) {
                stockIsOnUserList = true;
                break;
            }
        }

        if (stockIsOnUserList){
            for (Stock c : userVerified.getStockList()) {
                if (c.getStockName().equals(stockRequest.getStockName())) {
                    c.setStockAmount(stockAmount);
                    c.setStockPrice(String.valueOf(response.regularMarketPrice()));
                    c.setTotalUserStockValue(calcStockUserValue(stockAmount, response.regularMarketPrice()));
                    userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
                    stockRepository.save(c);
                    userRepository.save(userVerified);
                    break;
                }
            }
        } else {
            stockRepository.save(newStock);
            userVerified.getStockList().add(newStock);
            userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
            userRepository.save(userVerified);
        }


    }

}
