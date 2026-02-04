package com.Chinmay.MoneyManager.IO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    String token;
    String email;
}
