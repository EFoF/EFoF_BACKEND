package com.service.surveyservice.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    // 특정 요청이 권한이 없어서 실행될 수 없을때 내부적으로 호출되는 로직
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    }

    private void sendResponse(HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("JwtAccessDeniedHandler 에서 sendResponse 실행됨");
        String result = objectMapper.writeValueAsString(new ErrorResponse(accessDeniedException.getClass().getSimpleName(), accessDeniedException.getMessage()));
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.setStatus(response.SC_FORBIDDEN);
    }
}
