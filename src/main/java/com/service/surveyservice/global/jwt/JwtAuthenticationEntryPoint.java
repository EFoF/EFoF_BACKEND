package com.service.surveyservice.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.service.surveyservice.global.common.constants.ErrorResponseConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // AuthenticationEntryPoint는 401에러가 발생할때 내부적으로 로직을 실행한다.
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error(String.valueOf(authException.getCause()));
        log.error(authException.getMessage());
        sendResponse(response, authException);
    }


    private void sendResponse(HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        String result;
        if(authenticationException instanceof BadCredentialsException) {
            log.info("로그인 실패 - 이메일 비밀번호 일치하지 않음");
            result =objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.CONFLICT, INVALID_EMAIL_PASSWORD));
            response.setStatus(response.SC_CONFLICT);
        } else if (authenticationException instanceof InternalAuthenticationServiceException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.NOT_FOUND, NOT_FOUND_USER));
            response.setStatus(response.SC_NOT_FOUND);
        } else {
            result = objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED, INVALID_ACCESS_TOKEN));
            response.setStatus(response.SC_UNAUTHORIZED);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.getWriter().write(result);
    }
}
