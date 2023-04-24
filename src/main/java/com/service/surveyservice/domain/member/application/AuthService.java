package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.InvalidEmailAndPasswordRequestException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.token.dao.RefreshTokenDao;
import com.service.surveyservice.domain.token.exception.ExpiredRefreshTokenException;
import com.service.surveyservice.global.jwt.JwtTokenProvider;
import com.service.surveyservice.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;
import static com.service.surveyservice.domain.token.dto.TokenDTO.*;
import static com.service.surveyservice.global.common.constants.JwtConstants.*;
import static com.service.surveyservice.global.common.constants.ResponseConstants.CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberCustomRepositoryImpl memberCustomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenDao refreshTokenDao;

    @Transactional
    public String signUp(SignUpRequest signUpRequest) {
        signUpRequest.encrypt(passwordEncoder);
        Member member = signUpRequest.toEntity();
        memberRepository.save(member);
        return CREATED;
    }

    @Transactional
    public MemberLoginDTO login(LoginRequestDTO loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequestDto.toAuthentication();
        // 인증 정보 받아오기
        try {
            Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDTO(authenticate);
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
//        redisTemplate.expire(tokenInfoDTO.getAccessToken(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            String refreshToken = tokenInfoDTO.getRefreshToken();
            saveRefreshTokenInStorage(refreshToken, Long.valueOf(authenticate.getName()));
            CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
            CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME);


            return MemberLoginDTO.builder()
                    .memberDetail(memberCustomRepository.getMemberDetail(Long.parseLong(authenticate.getName())))
                    .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                    .build();
        } catch (BadCredentialsException e) {
            throw new InvalidEmailAndPasswordRequestException();
        }
    }

    @Transactional
    public TokenIssueDTO reissue(AccessTokenDTO accessTokenDTO) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshByAccess = valueOperations.get(accessTokenDTO.getAccessToken());
        if(refreshByAccess == null) {
            throw new ExpiredRefreshTokenException();
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessTokenDTO.getAccessToken());
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDTO(authentication);
        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(tokenInfoDTO.getAccessToken(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return tokenInfoDTO.toTokenIssueDTO();
    }

    /**
     *
     * @param refreshToken
     * @param memberId
     * redis에 refresh token을 저장함
     */
    private void saveRefreshTokenInStorage(String refreshToken, Long memberId) {
        refreshTokenDao.createRefreshToken(memberId, refreshToken);
    }

}
