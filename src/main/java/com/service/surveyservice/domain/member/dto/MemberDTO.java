package com.service.surveyservice.domain.member.dto;


import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberLoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
