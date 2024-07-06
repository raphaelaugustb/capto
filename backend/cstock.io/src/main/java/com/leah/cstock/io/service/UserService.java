package com.leah.cstock.io.service;

import com.leah.cstock.io.dto.request.UserRequest;
import com.leah.cstock.io.dto.response.UserResponse;
import com.leah.cstock.io.entity.Crypto;
import com.leah.cstock.io.entity.Stock;
import com.leah.cstock.io.entity.User;
import com.leah.cstock.io.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;



@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public double updateUserBalance(List<Crypto> cryptoList, List<Stock> stockList) {
        double userBalance = 0;
        double cryptoBalance = 0;
        double stockBalance = 0;
             for (Crypto crypto : cryptoList){
                 cryptoBalance += crypto.getTotalUserCryptoValue();

            }
             for (Stock stock: stockList){
                stockBalance += stock.getTotalUserStockValue();
            }

        userBalance = cryptoBalance + stockBalance;
        return userBalance;
    }
    public UserRequest verifyUserRequest(UserRequest userRequest) {
        if (userRequest != null){
            return  userRequest;
        } else {
            throw  new NullPointerException("Campos inválidos");
        }

    }
    public User verifyUser(User user) {
        if (user != null){
            return  user;
        } else {
            throw  new NullPointerException("Campos inválidos");
        }

    }
    public void createNewUser(UserRequest userRequest) {
        UserRequest requestVerificated = verifyUserRequest(userRequest);
        String username = requestVerificated.getUsername();
        String password = requestVerificated.getPassword();
        String email = requestVerificated.getEmail();
        User newUser= new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        userRepository.save(newUser);
    }

    public UserResponse getUserInfo(UUID userId){
        User userNotVerificated = userRepository.findById(userId).get();
        User vericatedUser = verifyUser(userNotVerificated);
        UserResponse userResponse = new UserResponse(vericatedUser.getUsername(), vericatedUser.getPassword(), vericatedUser.getEmail(), vericatedUser.getUserBalance());
        return userResponse;
    }

    public void updateUserInfo(UUID userId, UserRequest userRequest) {
        User userNotVerificated = userRepository.findById(userId).get();
        UserRequest requestVerificated = verifyUserRequest(userRequest);
        User vericatedUser = verifyUser(userNotVerificated);
        String username = requestVerificated.getUsername();
        String password = requestVerificated.getPassword();
        String email = requestVerificated.getEmail();
        vericatedUser.setEmail(
                email
        );
        vericatedUser.setPassword(
                password
        );
        vericatedUser.setUsername( username);
        userRepository.save(vericatedUser);
    }
    public  void deleteUserById(UUID userId) {
        User userNotVerificated = userRepository.findById(userId).get();
        User vericatedUser = verifyUser(userNotVerificated);
        userRepository.delete(vericatedUser);
    }

}
