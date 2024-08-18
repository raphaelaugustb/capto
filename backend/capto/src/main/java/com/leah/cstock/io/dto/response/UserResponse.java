package com.leah.cstock.io.dto.response;

public record UserResponse(String username, String password, String email, double userBalance) {
}
