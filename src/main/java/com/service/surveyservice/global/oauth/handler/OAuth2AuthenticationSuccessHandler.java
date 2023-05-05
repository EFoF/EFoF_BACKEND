package com.service.surveyservice.global.oauth.handler;

import com.service.surveyservice.domain.token.dao.RefreshTokenDao;
import com.service.surveyservice.domain.token.dto.TokenDTO;
import com.service.surveyservice.global.jwt.JwtTokenProvider;
import com.service.surveyservice.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.service.surveyservice.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.service.surveyservice.domain.token.dto.TokenDTO.*;
import static com.service.surveyservice.global.common.constants.AuthenticationConstants.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.service.surveyservice.global.common.constants.JwtConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    private final RefreshTokenDao refreshTokenDao;


    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        //토큰 생성
        TokenInfoDTO tokenDTO = tokenProvider.generateTokenDTO(authentication);

        saveRefreshTokenInStorage(tokenDTO.getRefreshToken(), Long.valueOf(authentication.getName()));
        CookieUtil.deleteCookie(request,response,ACCESS_TOKEN);
        CookieUtil.addCookie(response,ACCESS_TOKEN,tokenDTO.getAccessToken(),  ACCESS_TOKEN_COOKIE_EXPIRE_TIME, true);
        // 여기에 사용자 정보를 받아올 수 있어야 함.
        CookieUtil.addCookie(response,TOKEN_PUBLISH_CONFIRM,tokenDTO.getAccessToken(),  ACCESS_TOKEN_COOKIE_EXPIRE_TIME, false);
        String uriString = UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("token", tokenDTO.getAccessToken())
                .build().toUriString();
        return uriString;
    }

    /**
     * redis 에 refresh token 저장
     *
     * @param refreshToken
     * @param memberId
     */
    private void saveRefreshTokenInStorage(String refreshToken, Long memberId) {
        refreshTokenDao.createRefreshToken(memberId, refreshToken);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
