package com.demo.iot.service;

import com.demo.iot.dto.request.AccountRequest;
import com.demo.iot.dto.request.LoginRequest;
import com.demo.iot.dto.response.AccountResponse;
import com.demo.iot.dto.response.LoginResponse;
import com.demo.iot.dto.response.RefreshTokenResponse;
import com.demo.iot.entity.Account;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    Page<AccountResponse> findAccount(String username, Pageable pageable);
    void deleteAccount(List<Integer> ids);
    AccountResponse updateAccount(Integer id, AccountRequest accountRequest);
    AccountResponse getAccount(Integer id);
}
