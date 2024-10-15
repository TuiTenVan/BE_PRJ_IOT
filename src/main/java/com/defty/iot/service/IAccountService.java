package com.defty.iot.service;

import com.defty.iot.dto.request.LoginRequest;
import com.defty.iot.dto.response.AccountResponse;
import com.defty.iot.dto.response.LoginResponse;
import com.defty.iot.dto.response.RefreshTokenResponse;
import com.defty.iot.entity.Account;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface IAccountService {
    LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);
    void logout(String token);
    AccountResponse getAccountFromToken(String token);
    Optional<Account> getCurrentAccount();
    RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response);
}
