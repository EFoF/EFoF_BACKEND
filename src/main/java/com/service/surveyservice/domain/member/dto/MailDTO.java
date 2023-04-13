package com.service.surveyservice.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MailDTO {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailConfirmCodeDTO {
        private String email;
        private String code;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CodeConfirmDTO {
        private boolean matches;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailSentDTO {
        private String email;
        private boolean success;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EncryptEmailDTO {
        // 사용자가 이메일 찾기를 요청했을 때, 응답으로 일부만 공개된 이메일을 보낸다.
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmEmailDTO {
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindEmailDTO {
        private String username;
        // 전화번호를 필드로 가지지 않기 때문에 닉네임을 사용했다.
        private String nickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserPasswordRequestDTO {
        private Long userId;
        private String oldPassword;
        private String newPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.oldPassword = passwordEncoder.encode(oldPassword);
            this.newPassword = passwordEncoder.encode(newPassword);
        }
    }
}
