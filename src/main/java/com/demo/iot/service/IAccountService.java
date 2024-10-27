package com.demo.iot.service;

import com.demo.iot.dto.request.LoginRequest;
import com.demo.iot.dto.response.AccountResponse;
import com.demo.iot.dto.response.LoginResponse;
import com.demo.iot.dto.response.RefreshTokenResponse;
import com.demo.iot.entity.Account;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface IAccountService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);
    void logout(String token);
    AccountResponse getAccountFromToken(String token);
    Optional<Account> getCurrentAccount();
    RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response);
}
