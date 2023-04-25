package com.service.surveyservice.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.IOException;

import static com.service.surveyservice.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그아웃 내부적으로 호출됨");
//        log.info(Long.valueOf(authentication.getName()) + " 사용자가 로그아웃 호출함");
//        String token = request.getHeader(AUTHORIZATION_HEADER).split(" ")[1];
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        // 로그아웃이라서 현재 활성화된 token을 지워준다.
//        valueOperations.getAndDelete(token);
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        super.onLogoutSuccess(request, response, authentication);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        return "logout-redirect";
    }
}
