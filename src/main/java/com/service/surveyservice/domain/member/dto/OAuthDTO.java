package com.service.surveyservice.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OAuthDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleUesrDTO {
        private String id;
        private String email;
        private Boolean verifiedEmail;
        private String name;
        private String givenName;
        private String familyName;
        private String picture;
        private String locale;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleLoginRequestDTO {
        private String email;
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleTokenDTO {
        private String token;
    }
}
