package com.service.surveyservice.domain.member.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberLoginType;
import com.service.surveyservice.domain.token.dto.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.service.surveyservice.domain.token.dto.TokenDTO.*;

public class MemberDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequest {
        private String userName;

        private String nickname;

        private String email;

        private String password;

        private String organization;

        private String organizationDetail;

        private MemberLoginType memberLoginType;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(password);
        }

        public Member toEntity() {

            return Member.builder()
                    .email(email)
                    .password(password)
                    .username(userName)
                    .nickname(nickname)
                    .organization(organization)
                    .organizationDetail(organizationDetail)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDTO {
        private String email;
        private String password;
        public void encrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(password);
        }
        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginDTO {

        private MemberDetail memberDetail;
        private TokenIssueDTO tokenInfo;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    public static class MemberDetail {
        private Long id;
        private String username;
        private String email;
        private String organization;
        private String organizationDetail;

        @QueryProjection
        public MemberDetail(Long id, String username, String email, String organization, String organizationDetail) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.organization = organization;
            this.organizationDetail = organizationDetail;
        }
    }
}
