package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.DuplicatedEmailException;
import com.service.surveyservice.domain.member.exception.exceptions.member.InvalidEmailAndPasswordRequestException;
import com.service.surveyservice.domain.member.exception.exceptions.member.InvalidRefreshTokenException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.token.dao.RefreshTokenDao;
import com.service.surveyservice.domain.token.exception.exceptions.NoSuchAccessTokenException;
import com.service.surveyservice.domain.token.exception.exceptions.NoSuchCookieException;
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

import java.util.Date;

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


            long now = new Date().getTime();
            Date loginExpire = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
            MemberDetail memberDetail = memberCustomRepository.getMemberDetail(Long.parseLong(authenticate.getName()));
            MemberLoginDTO memberLoginDTO = MemberLoginDTO.builder()
                    .memberDetail(memberDetail)
                    .loginLastDTO(LoginLastDTO.builder()
                            .expiresAt(loginExpire.getTime())
                            .nickname(memberDetail.getNickname())
                            .build())
//                            .loginType(MemberLoginType.DOKSEOL_LOGIN)
//                              .tokenIssueDTO(tokenInfoDTO.toTokenIssueDTO())
                    .build();

            CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME, true);

            return memberLoginDTO;
        } catch (BadCredentialsException e) {
            throw new InvalidEmailAndPasswordRequestException();
        }
    }

    @Transactional
    public LoginLastDTO reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, ACCESS_TOKEN).orElse(null);

        if(cookie == null) {
            throw new NoSuchCookieException();
        }

        String token = cookie.getValue();

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Long memberId = Long.valueOf(authentication.getName());

        // 2. redis에서 사용자 정보로 refresh token 가져오기
        String refreshToken = refreshTokenDao.getRefreshToken(memberId);

        if(refreshToken == null) {
            throw new NoSuchAccessTokenException();
        }
        // 3. refresh token 검증
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }
        // 4. 새로운 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDTO(authentication);
        // 5. 저장소에 저장
        saveRefreshTokenInStorage(tokenInfoDTO.getRefreshToken(), memberId);
//        // 6. 토큰 발급
        log.info("토큰 재발급 성공");
        CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME, true);
        long now = new Date().getTime();
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        Date loginExpire = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return LoginLastDTO.builder()
                .expiresAt(loginExpire.getTime())
                .nickname(member.getNickname())
                .build();
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
