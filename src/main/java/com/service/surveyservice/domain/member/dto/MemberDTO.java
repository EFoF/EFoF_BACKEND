package com.service.surveyservice.domain.member.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberLoginType;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.service.surveyservice.domain.token.dto.TokenDTO.*;

public class MemberDTO {

    @Getter
    @Builder
    @ToString
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
    @ToString
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RedunCheckDTO {
        private Boolean exists;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckEmailRequestDTO {
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckNicknameRequestDTO {
        private String nickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnPasswordDTO {
        private String password;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindPasswordRequestDTO {
        private String userName;
        private String nickname;
        private String email;
    }

    // 사용자의 비밀번호를 비밀번호 찾기 과정에서 생성된 랜덤 비밀번호로 임시 변경하기 위한 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateGeneratedPasswordRequestDTO {
        private Long userId;
        private String oldPassword;
        private String generatedPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.oldPassword = passwordEncoder.encode(oldPassword);
            this.generatedPassword = passwordEncoder.encode(generatedPassword);
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberPasswordRequestDTO {
        private String email;
        private String oldPassword;
        private String newPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.oldPassword = passwordEncoder.encode(oldPassword);
            this.newPassword = passwordEncoder.encode(newPassword);
        }
    }
}
