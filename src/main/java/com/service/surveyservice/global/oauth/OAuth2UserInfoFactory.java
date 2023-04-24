package com.service.surveyservice.global.oauth;

import com.service.surveyservice.global.oauth.impl.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String memberLoginType, Map<String, Object> attributes) {
        switch (memberLoginType) {
            case "GOOGLE": return new GoogleOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
