package com.demo.iot.service;

public interface IRefreshTokenService {
    String createRefreshToken(Integer accountId);
    void deleteRefreshToken(Integer accountId);
}
