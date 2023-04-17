package com.service.surveyservice.domain.member.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.surveyservice.domain.member.dto.OAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.service.surveyservice.domain.member.dto.OAuthDTO.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleAuth {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.OAuth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_CALLBACK_URL;

    @Value("${spring.OAuth2.google.client_id}")
    private String CLIENT_ID;

    @Value("${spring.OAuth2.google.client_secret}")
    private String CLIENT_SECRET;

    @Value("${spring.OAuth2.google.scope}")
    private String GOOGLE_ACCESS_SCOPE;

    @Value("${spring.OAuth2.google.userinfo_request}")
    private String USERINFO_REQUEST_URL;

    public String getOAuthRedirectURL() {
        Map<String,Object> params = new HashMap<>();
        params.put("scope", GOOGLE_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);

        // callbackURL, scope 등을 활용해서 리다이렉트 URL을 만듦.
        String parameterString = params
                .entrySet()
                .stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        log.info("구글 인증 URL : " + redirectURL);
        return redirectURL;
    }


    // 헤더에 토큰 정보를 담아서 구글 URL로 요청을 보내면, 유저의 정보를 받을 수 있다.
    public ResponseEntity<String>requestUserInfo(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "bearer " + token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
    }

    // ObjectMapper를 통해서 JSON 형식의 데이터를 객체로 매핑해줌.
    public GoogleUesrDTO getUserInfo(ResponseEntity<String> userInfo) throws JsonProcessingException {
        return objectMapper.readValue(userInfo.getBody(), GoogleUesrDTO.class);
    }
}
