package com.service.surveyservice.global.common.constants;

public class JwtConstants {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_TYPE = "Bearer";

    public static final int ACCESS_TOKEN_COOKIE_EXPIRE_TIME = 60 * 60 * 24 * 7;
    // access token 쿠키의 경우 1분 뒤에 만료
//    public static final int ACCESS_TOKEN_COOKIE_EXPIRE_TIME = 60;

    public static final int CONFIRM_TOKEN_COOKIE_EXPIRE_TIME = 3600;

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
//    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 10;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60*60*24*7;
//    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 20;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String PREFIX_REFRESH_TOKEN = "refreshToken:";
    public static final String ACCESS_TOKEN = "token";

    public static final String TOKEN_PUBLISH_CONFIRM = "tokenPublishConfirm";
}
