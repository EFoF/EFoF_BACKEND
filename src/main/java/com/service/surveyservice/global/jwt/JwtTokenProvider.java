package com.service.surveyservice.global.jwt;

import com.service.surveyservice.domain.token.dto.TokenDTO;
import com.service.surveyservice.domain.token.dto.TokenDTO.TokenInfoDTO;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import static com.service.surveyservice.global.common.constants.JwtConstants.ACCESS_TOKEN_EXPIRE_TIME;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode(System.getenv("JWT_SECRET"));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfoDTO generateTokenDTO(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

    }

}
