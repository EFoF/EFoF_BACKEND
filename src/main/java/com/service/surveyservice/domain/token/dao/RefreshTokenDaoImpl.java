package com.service.surveyservice.domain.token.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.service.surveyservice.global.common.constants.JwtConstants.PREFIX_REFRESH_TOKEN;
import static com.service.surveyservice.global.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDaoImpl implements RefreshTokenDao {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     *
     * @param memberId
     * @param refreshToken
     * refreshToken:memberId를 key값으로 value에는 refreshToken을 저장한다.
     */
    @Override
    public void createRefreshToken(Long memberId, String refreshToken) {
        stringRedisTemplate.opsForValue().set(PREFIX_REFRESH_TOKEN + memberId, refreshToken, Duration.ofSeconds(REFRESH_TOKEN_EXPIRE_TIME));
    }

    /**
     *
     * @param memberId
     * @return refreshToken (String)
     * memberId로 refreshToken을 redis에서 찾아 반환한다.
     */
    @Override
    public String getRefreshToken(Long memberId) {
        return stringRedisTemplate.opsForValue().get(PREFIX_REFRESH_TOKEN + memberId);
    }

    /**
     *
     * @param memberId
     * memberId로 refreshToken을 찾아서 삭제
     */
    @Override
    public void removeRefreshToken(Long memberId) {
        stringRedisTemplate.delete(PREFIX_REFRESH_TOKEN + memberId);
    }
}
