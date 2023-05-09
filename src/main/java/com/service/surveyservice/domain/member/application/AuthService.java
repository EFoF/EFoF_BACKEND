package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.DuplicatedEmailException;
import com.service.surveyservice.domain.member.exception.exceptions.member.InvalidEmailAndPasswordRequestException;
import com.service.surveyservice.domain.member.exception.exceptions.member.InvalidRefreshTokenException;
import com.service.surveyservice.domain.member.exception.exceptions.member.NotSignInException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.token.dao.RefreshTokenDao;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import com.service.surveyservice.global.jwt.JwtTokenProvider;
import com.service.surveyservice.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        if(memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }
        Member member = signUpRequest.toEntity();
        memberRepository.save(member);
        return CREATED;
    }

    @Transactional(readOnly = true)
    public MemberLoginDTO login(LoginRequestDTO loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequestDto.toAuthentication();
        try {
            Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDTO(authenticate);
            String refreshToken = tokenInfoDTO.getRefreshToken();
            saveRefreshTokenInStorage(refreshToken, Long.valueOf(authenticate.getName()));
            CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);

            MemberLoginDTO memberLoginDTO = MemberLoginDTO.builder()
                    .memberDetail(memberCustomRepository.getMemberDetail(Long.parseLong(authenticate.getName())))
                    .build();

            CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME, true);
            CookieUtil.addCookie(response, TOKEN_PUBLISH_CONFIRM, memberLoginDTO.getMemberDetail().getEmail(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME, false);

            return memberLoginDTO;
        } catch (BadCredentialsException e) {
            throw new InvalidEmailAndPasswordRequestException();
        }
    }

    /**
     *
     * @param request
     * @param response
     * 쿠키가 모두 유효하지만 만료되어서 다시 발급받아야 하는 경우 호출
     */
    @Transactional
    public Boolean reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, ACCESS_TOKEN).orElse(null);
        String accessToken;

        if(cookie == null) {
            return false;
        }

        accessToken = cookie.getValue();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Long memberId = Long.valueOf(authentication.getName());
        // 1. 사용자가 존재하는지 확인
        if(!memberRepository.existsById(memberId)) {
            throw new NotFoundByIdException();
        }
        // 2. redis에서 사용자 정보로 refresh token 가져오기
        String refreshToken = refreshTokenDao.getRefreshToken(memberId);

        if(refreshToken == null) {
            return false;
        }
        log.error(refreshToken);
        // 3. refresh token 검증
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            return false;
        }
        // 4. 새로운 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDTO(authentication);
        log.error(tokenInfoDTO.getRefreshToken());
        // 5. 저장소에 저장
        saveRefreshTokenInStorage(tokenInfoDTO.getRefreshToken(), memberId);
        // 6. 토큰 발급
        CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME, true);

        return true;
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
