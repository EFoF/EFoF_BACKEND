package com.service.surveyservice.domain.member.application;

import com.nimbusds.oauth2.sdk.GeneralException;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.DuplicatedSignUpException;
import com.service.surveyservice.domain.member.model.Authority;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberLoginType;
import com.service.surveyservice.domain.member.model.UserPrincipal;
import com.service.surveyservice.global.oauth.OAuth2UserInfo;
import com.service.surveyservice.global.oauth.OAuth2UserInfoFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        // 위 코드 2줄로 리소스 서버(구글, 네이버, ...)로부터 유저 정보를 받아왔다.

        MemberLoginType memberLoginType = MemberLoginType.LabelToValue(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(memberLoginType.getType(), oAuth2User.getAttributes());

        Member member = memberRepository.findByEmailForOAuth(userInfo.getEmail());
        if(member != null) {
            if(memberLoginType != member.getMemberLoginType()) {
                throw new DuplicatedSignUpException(member.getMemberLoginType().getType() + "계정으로 회원가입 되어있습니다.");
            }
        } else {
            // 사용자가 없으면 바로 가입시킨다.
             member = createMember(userInfo, memberLoginType);
        }

        return UserPrincipal.create(member, userInfo.getAttributes());
    }

    private Member createMember(OAuth2UserInfo memberInfo, MemberLoginType memberLoginType) {
        String password = passwordEncoder.encode(memberInfo.getId());
        // TODO 사용자 닉네임은 직접 입력 받게 변경 예정
        Member member = Member.builder()
                .nickname(memberLoginType.getType() + "_" + memberInfo.getName())
                .memberLoginType(memberLoginType)
                .username(memberInfo.getName())
                .authority(Authority.ROLE_USER)
                .email(memberInfo.getEmail())
                .password(password)
                .build();

        return memberRepository.save(member);
    }

}
