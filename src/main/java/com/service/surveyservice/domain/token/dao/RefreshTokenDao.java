package com.service.surveyservice.domain.token.dao;

public interface RefreshTokenDao {
    void createRefreshToken(Long memberId, String refreshToken);
    String getRefreshToken(Long memberId);
    void removeRefreshToken(Long memberId);
}
