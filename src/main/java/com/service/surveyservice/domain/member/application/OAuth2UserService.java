package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.component.GoogleAuth;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberLoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;
import static com.service.surveyservice.domain.member.dto.OAuthDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService {
    private final GoogleAuth googleAuth;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    @Value("${spring.OAuth2.google.client_password}")
    private final String CLIENT_PW;

    @Transactional
    public MemberLoginDTO googleLogin(GoogleLoginRequestDTO googleLoginRequestDTO) {
        Optional<Member> byEmail = memberRepository.findByEmail(googleLoginRequestDTO.getEmail());
        if(byEmail.isPresent()) {
            Member member = byEmail.get();
            if(member.getMemberLoginType() != MemberLoginType.GOOGLE) {
                throw new IllegalArgumentException("디비 이메일 중복");
            }
        } else {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .email(googleLoginRequestDTO.getEmail())
                    .userName(googleLoginRequestDTO.getName())
                    .memberLoginType(MemberLoginType.GOOGLE)
                    .nickname("독수리 회원 " + Long.toString(System.currentTimeMillis()))
                    .password(CLIENT_PW)
                    .build();
            authService.signUp(signUpRequest);
        }
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email(googleLoginRequestDTO.getEmail())
                .password(CLIENT_PW)
                .build();
        return authService.login(loginRequestDTO);
    }
}
