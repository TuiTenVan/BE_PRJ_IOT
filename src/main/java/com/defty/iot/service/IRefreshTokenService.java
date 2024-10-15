package com.defty.iot.service;

public interface IRefreshTokenService {
    String createRefreshToken(Integer accountId);
    void deleteRefreshToken(Integer accountId);
}
