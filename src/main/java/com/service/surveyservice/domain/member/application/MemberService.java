package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.domain.member.dto.MemberDTO.RedunCheckDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCustomRepositoryImpl memberCustomRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public RedunCheckDTO existsByEmail(String email) {
        return new RedunCheckDTO(memberRepository.existsByEmail(email));
    }

    @Transactional(readOnly = true)
    public RedunCheckDTO existsByNickname(String nickname) {
        return new RedunCheckDTO(memberRepository.existsByNickname(nickname));
    }

}
