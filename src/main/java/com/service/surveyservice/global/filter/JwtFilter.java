package com.service.surveyservice.global.filter;

import com.service.surveyservice.domain.member.application.AuthService;
import com.service.surveyservice.domain.token.exception.exceptions.ExpiredAccessTokenException;
import com.service.surveyservice.global.jwt.JwtTokenProvider;
import com.service.surveyservice.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.service.surveyservice.global.common.constants.JwtConstants.*;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
//    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 여기서 헤더를 까야한다.
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
//            log.error(bearerToken.substring(7));
//            return bearerToken.substring(7);
//        }
//        return null;
        Cookie bearerToken = CookieUtil.getCookie(request, ACCESS_TOKEN).orElse(null);
        if(bearerToken != null) {
            return bearerToken.getValue();
        }
        return null;
    }
}
