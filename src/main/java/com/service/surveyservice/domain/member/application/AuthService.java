package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

//    @Transactional
//    public String signUp()


}
