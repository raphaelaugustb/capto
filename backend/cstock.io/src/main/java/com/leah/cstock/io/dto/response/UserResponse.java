package com.leah.cstock.io.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public record UserResponse(String username, String password, String email, double userBalance) {
}
