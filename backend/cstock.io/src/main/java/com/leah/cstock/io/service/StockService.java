package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.StockRequest;
import com.leah.cstock.io.dto.response.Stock.Results;
import com.leah.cstock.io.dto.response.Stock.StockResponse;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.exceptions.Stock.StockNotFoundException;
import com.leah.cstock.io.repository.StockRepository;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService {

    private final BrapiService brapiService;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final String stockNotFoundError = "Stock not found";
    @Value("#{environment.TOKEN}")
    private String TOKEN;

    public StockService(BrapiService brapiService, StockRepository stockRepository, UserRepository userRepository, UserService userService) {
        this.brapiService = brapiService;
        this.stockRepository = stockRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public double updateUserStockPnl(User user, String stockSymbol){
        double userStockPnl;
        Stock stockUser = user.getStockList().stream().filter(stock -> stock.getStockSymbol().equalsIgnoreCase(stockSymbol)).findFirst().orElse(null);
        Results stockService = getStock(stockSymbol).results().stream().findFirst().orElse(null);
        if(stockUser != null && stockService != null){
            double userStockValue = Double.parseDouble(stockUser.getStockPrice());
            userStockPnl = (stockService.regularMarketPrice() - userStockValue)/100;
        } else {
            throw new StockNotFoundException(stockNotFoundError);
        }
        return userStockPnl;
    }
    public StockResponse getStock(String stockSymbol) {
        return brapiService.getStockById(stockSymbol, TOKEN);
    }

    public double calcStockUserValue(double amount, double regularMarketPrice) {
        return amount * regularMarketPrice;
    }

    public List<Stock> getUserStockList(UUID userId) {
        User userVerified = userService.verifyUser(userId);
        return userVerified.getStockList().stream().map(stock -> {
            stock.setUserPnlValueStock(updateUserStockPnl(userVerified, stock.getStockSymbol()));
            return stock;
        }).toList();
    }

    public Stock getStockOnUserList(UUID userId, String stockName) {
        Stock stockSelected = null;
        User userVerified = userService.verifyUser(userId);
        for (Stock a : userVerified.getStockList()) {
            if (a.getStockSymbol().equals(stockName)) {
                stockSelected = a;
                break;
            }
        }
        if (stockSelected != null) {
            stockSelected.setUserPnlValueStock(updateUserStockPnl(userVerified, stockSelected.getStockSymbol()));
            return stockSelected;
        } else {
            throw new StockNotFoundException(stockNotFoundError);
        }
    }

    public void updateStockOnUserList(UUID userId, StockRequest stockRequest) {
        String stockName = stockRequest.stockName();
        double stockAmount = stockRequest.stockAmount();
        double newRegularMarketPrice = getStock(stockName).results().getFirst().regularMarketPrice();
        User userVerified = userService.verifyUser(userId);
        Stock stockUpdated = null;
        for (Stock a : userVerified.getStockList()) {
            if (a.getStockSymbol().equals(stockName)) {
                stockUpdated = a;
            }
        }
        if (stockUpdated != null) {
            stockUpdated.setStockAmount(stockAmount);
            stockUpdated.setStockPrice(String.valueOf(newRegularMarketPrice));
            stockUpdated.setTotalUserStockValue(calcStockUserValue(stockAmount, newRegularMarketPrice));
            userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
            stockRepository.save(stockUpdated);
            userRepository.save(userVerified);

        } else {
            throw new StockNotFoundException(stockNotFoundError);
        }

    }

    public void deleteUserStock(UUID userId, String stockName) {
        User userVerified = userService.verifyUser(userId);
        Stock stockRemoved = null;
        for (Stock a : userVerified.getStockList()) {
            if (a.getStockSymbol().equals(stockName)) {
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
            throw new StockNotFoundException(stockNotFoundError);
        }
    }

    public void createNewStockUser(StockRequest stockRequest, UUID userId) {
        User userVerified = userService.verifyUser(userId);
        boolean stockIsOnUserList = false;
        String stockName = stockRequest.stockName();
        double stockAmount = stockRequest.stockAmount();
        Results response = brapiService.getStockById(stockName, TOKEN).results().getFirst();
        Stock newStock = new Stock();
        newStock.setStockAmount(stockAmount);
        newStock.setStockName(response.longName());
        newStock.setStockSymbol(response.symbol());
        newStock.setStockPrice(String.valueOf(response.regularMarketPrice()));
        newStock.setTotalUserStockValue(calcStockUserValue(stockAmount, response.regularMarketPrice()));
        for (Stock a : userVerified.getStockList()) {
            if (a.getStockSymbol().equals(stockName)) {
                stockIsOnUserList = true;
                break;
            }
        }

        if (stockIsOnUserList) {
            for (Stock stock : userVerified.getStockList()) {
                if (stock.getStockSymbol().equals(stockRequest.stockName())) {
                    stock.setStockAmount(stockAmount);
                    stock.setStockPrice(String.valueOf(response.regularMarketPrice()));
                    stock.setTotalUserStockValue(calcStockUserValue(stockAmount, response.regularMarketPrice()));
                    userVerified.setUserBalance(userService.updateUserBalance(userVerified.getCryptoList(), userVerified.getStockList()));
                    stockRepository.save(stock);
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
