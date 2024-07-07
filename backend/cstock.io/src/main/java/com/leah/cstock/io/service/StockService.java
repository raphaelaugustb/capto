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

import java.util.List;
import java.util.UUID;

@Service
public class StockService {
    private final CoinCapService coinCapService;
    @Value("#{environment.TOKEN}")
    private String TOKEN;

    BrapiService brapiService;
    StockRepository stockRepository;
    UserRepository userRepository;
    UserService userService;

    public StockService(BrapiService brapiService, StockRepository stockRepository, UserRepository userRepository, UserService userService, CoinCapService coinCapService) {
        this.brapiService = brapiService;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.coinCapService = coinCapService;
    }

    public StockResponse getStock(String stockSymbol) {
        StockResponse stock = brapiService.getStockById(stockSymbol, TOKEN);
        return stock;
    }
    public double calcStockUserValue(double amount, double regularMarketPrice){
        double userStockValue = amount * regularMarketPrice;
        return userStockValue;
    }
    public List<Stock> getUserStockList(UUID userId){
        User userVerified = userService.verifyUser(userId);
        return userVerified.getStockList();
    }
    public Stock getStockOnUserList(UUID userId, String stockName){
        //Fix bugs to delete next Stock on list
        Stock stockSelected = null;
        User userVerified = userService.verifyUser(userId);
        for (Stock a: userVerified.getStockList()){
            if (a.getStockName().equals(stockName)){
                stockSelected = a;
                break;
            }
        }
        if (stockSelected != null){

            return stockSelected;
        } else {
            throw new NullPointerException("Stock not found");
        }
    }
    public void updateStockOnUserList(UUID userId, StockRequest stockRequest){
        String stockName = stockRequest.getStockName();
        double stockAmount = stockRequest.getStockAmount();
        double newRegularMarketPrice = getStock(stockName).results().getFirst().regularMarketPrice();
        User userVerified = userService.verifyUser(userId);
        Stock stockUpdated = null;
        for (Stock a: userVerified.getStockList()){
            if (a.getSymbol().equals(stockName)){
                stockUpdated = a;
            }
        }
        if (stockUpdated != null){
            stockUpdated.setStockAmount(stockAmount);
            stockUpdated.setStockPrice(String.valueOf(newRegularMarketPrice));
            stockUpdated.setTotalUserStockValue(calcStockUserValue(stockAmount, newRegularMarketPrice));
            userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
            stockRepository.save(stockUpdated);
            userRepository.save(userVerified);

        } else {
            throw new NullPointerException("Stock not found");
        }

    }
    public void deleteUserStock(UUID userId, String stockName){
        User userVerified = userService.verifyUser(userId);
        Stock stockRemoved = null;
        for (Stock a: userVerified.getStockList()){
            if (a.getStockName().equals(stockName)){
                stockRemoved = a;
                break;
            }
        }
        if (stockRemoved != null) {
            userVerified.getStockList().remove(stockRemoved);
            userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
            userRepository.save(userVerified);
            stockRepository.deleteById(stockRemoved.getIdStock());
        } else {
            throw new NullPointerException("Stock not found");
        }
    }
    public void createNewStockUser(StockRequest stockRequest, UUID userId) {
        User userVerified = userService.verifyUser(userId);
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
