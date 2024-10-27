package com.demo.iot.controller;

import com.demo.iot.utils.CookieUtil;
import com.demo.iot.dto.request.LoginRequest;
import com.demo.iot.dto.response.AccountResponse;
import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.LoginResponse;
import com.demo.iot.dto.response.RefreshTokenResponse;
import com.demo.iot.service.IAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    IAccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse res){
        LoginResponse loginResponse = accountService.login(loginRequest, res);
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(loginResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/check-account")
    public ResponseEntity<?> checkAccount(HttpServletRequest request) {
        String token = CookieUtil.getValue(request, "access_token");
        if (token == null || token.isEmpty()) {
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Not logged in or do not have a valid token")
                    .data("Ok")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            AccountResponse accountResponse = accountService.getAccountFromToken(token);
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(accountResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse<?> response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Token is expired or invalid")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse res) {
        String refreshToken = CookieUtil.getValue(request, "refresh_token");
        RefreshTokenResponse newToken = accountService.refreshToken(refreshToken, res);
        ApiResponse<?> response = ApiResponse.builder()
               .status(HttpStatus.OK.value())
               .message(HttpStatus.OK.getReasonPhrase())
               .data(newToken)
               .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.getValue(request, "access_token");
        accountService.logout(accessToken);
        CookieUtil.clear(response, "access_token");
        CookieUtil.clear(response, "refresh_token");
        ApiResponse<?> responseObj = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }
}
