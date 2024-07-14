package com.leah.cstock.io.controller;


import com.leah.cstock.io.dto.request.UserRequest;
import com.leah.cstock.io.dto.response.UserResponse;
import com.leah.cstock.io.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRequest> registerUser(@RequestBody UserRequest userRequest) {
        userService.createNewUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRequest);
    }

    @GetMapping("{idUser}/user")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable UUID idUser) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserInfo(idUser));
    }

    @PutMapping("{idUser}/user")
    public ResponseEntity<UserRequest> updateUser(@PathVariable UUID idUser, @RequestBody UserRequest userRequest) {
        userService.updateUserInfo(idUser, userRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRequest);
    }

    @DeleteMapping("{idUser}/user")
    public ResponseEntity<String> deleteUser(@PathVariable UUID idUser) {
        userService.deleteUserById(idUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("User deleted successfully");
    }

}
