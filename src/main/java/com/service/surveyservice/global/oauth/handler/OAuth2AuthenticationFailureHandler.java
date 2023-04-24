package com.service.surveyservice.global.oauth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    // 일단은 생성만 해두겠음.
    // 쿠키 인증방식을 사용하면 여기에서 쿠키 삭제 처리 등의 부가적인 처리를 함
}
