package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.UserRequest;
import com.leah.cstock.io.dto.response.UserResponse;
import com.leah.cstock.io.entity.Crypto;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public double updateUserBalance(List<Crypto> cryptoList, List<Stock> stockList) {
        double userBalance;
        double cryptoBalance = 0;
        double stockBalance = 0;
        for (Crypto crypto : cryptoList) {
            cryptoBalance += crypto.getTotalUserCryptoValue();

        }
        for (Stock stock : stockList) {
            stockBalance += stock.getTotalUserStockValue();
        }

        userBalance = cryptoBalance + stockBalance;
        return userBalance;
    }

    public UserRequest verifyUserRequest(UserRequest userRequest) {
        if (userRequest != null) {
            return userRequest;
        } else {
            throw new NullPointerException("Campos inválidos");
        }

    }

    public User verifyUser(UUID userId) {
        User user = userRepository.findById(userId).get();
        if (user != null) {
            return user;
        } else {
            throw new NullPointerException("User not found");
        }

    }

    public void createNewUser(UserRequest userRequest) {
        UserRequest requestVerificated = verifyUserRequest(userRequest);
        String username = requestVerificated.getUsername();
        String password = requestVerificated.getPassword();
        String email = requestVerificated.getEmail();
        User verifyUserExist = userRepository.findByEmail(email);
        if (verifyUserExist == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            userRepository.save(newUser);
        } else {
            throw new RuntimeException("Username already exists");
        }

    }

    public UserResponse getUserInfo(UUID userId) {
        User userVerified = verifyUser(userId);
        return new UserResponse(userVerified.getUsername(), userVerified.getPassword(), userVerified.getEmail(), userVerified.getUserBalance());
    }

    public void updateUserInfo(UUID userId, UserRequest userRequest) {
        UserRequest requestVerificated = verifyUserRequest(userRequest);
        User vericatedUser = verifyUser(userId);
        String username = requestVerificated.getUsername();
        String password = requestVerificated.getPassword();
        String email = requestVerificated.getEmail();
        vericatedUser.setEmail(
                email
        );
        vericatedUser.setPassword(
                password
        );
        vericatedUser.setUsername(username);
        userRepository.save(vericatedUser);
    }

    public void deleteUserById(UUID userId) {
        User userVerified = verifyUser(userId);
        userRepository.delete(userVerified);
    }

}
