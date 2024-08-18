package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.UserRequest;
import com.leah.cstock.io.dto.response.UserResponse;
import com.leah.cstock.io.entity.Crypto;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.exceptions.user.InvalidUserRequest;
import com.leah.cstock.io.exceptions.user.UserAlreadyExistException;
import com.leah.cstock.io.exceptions.user.UserNotFoundException;
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
    //Implement Function to find user by name and password

    private UserRequest verifyUserRequest(UserRequest userRequest) {
        if (userRequest.email() == null || userRequest.password() == null || userRequest.username() == null)
            throw new InvalidUserRequest("Invalid user request");
        return userRequest;

    }

    public User verifyUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserResponse createNewUser(UserRequest userRequest) {
        UserRequest verifiedRequest = verifyUserRequest(userRequest);
        String username = verifiedRequest.username();
        String password = verifiedRequest.password();
        String email = verifiedRequest.email();
        User verifyUserExist = userRepository.findByEmail(email);
        if (verifyUserExist != null)
            throw new UserAlreadyExistException("User already exists");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userRepository.save(user);
        return new UserResponse(username, password, email, user.getUserBalance());
    }

    public UserResponse getUserInfo(UUID userId) {
        User userVerified = verifyUser(userId);
        return new UserResponse(userVerified.getUsername(), userVerified.getPassword(), userVerified.getEmail(), userVerified.getUserBalance());
    }

    public void updateUserInfo(UUID userId, UserRequest userRequest) {
        UserRequest verifiedRequest = verifyUserRequest(userRequest);
        User verifiedUser = verifyUser(userId);
        String username = verifiedRequest.username();
        String password = verifiedRequest.password();
        String email = verifiedRequest.email();
        verifiedUser.setEmail(email);
        verifiedUser.setPassword(password);
        verifiedUser.setUsername(username);
        userRepository.save(verifiedUser);
    }

    public void deleteUserById(UUID userId) {
        User userVerified = verifyUser(userId);
        userRepository.delete(userVerified);
    }

}
