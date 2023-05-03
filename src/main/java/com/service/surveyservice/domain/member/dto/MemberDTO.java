package com.service.surveyservice.domain.member.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.service.surveyservice.domain.member.model.Authority;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberLoginType;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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

        @Enumerated(value = EnumType.STRING)
        private MemberLoginType memberLoginType;

        @Enumerated(value = EnumType.STRING)
        private Authority authority;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(password);
        }

        public Member toEntity() {

            return Member.builder()
                    .email(email)
                    .password(password)
                    .username(userName)
                    .nickname(nickname)
                    .authority(authority)
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
    }


    @Getter
    @Builder
    @NoArgsConstructor
    public static class MemberDetail {
        private Long id;
        private String username;
        private String nickname;
        private String email;

        @QueryProjection
        public MemberDetail(Long id, String username, String nickname, String email) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
            this.email = email;
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
    public static class UpdateNicknameRequestDTO {
        private String email;
        private String oldNickname;
        private String newNickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberProfileImgRequestDTO {
        private String email;
        private String newProfileImg;
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


    // 사용자의 요청으로 비밀번호를 변경하기 위한 DTO
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
