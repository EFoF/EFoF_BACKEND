package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MailDTO;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.domain.member.dto.MemberDTO.RedunCheckDTO;
import com.service.surveyservice.domain.member.exception.member.UserNotFoundByUsernameAndNickname;
import com.service.surveyservice.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.service.surveyservice.domain.member.dto.MailDTO.*;


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

    @Transactional(readOnly = true)
    public EncryptEmailDTO findUserEmail(FindEmailDTO findEmailDTO) {
        String userName = findEmailDTO.getUserName();
        String nickname = findEmailDTO.getNickname();
        Member member = memberRepository.findByUserNameAndNickname(userName, nickname).orElseThrow(UserNotFoundByUsernameAndNickname::new);
        return member.encryptEmailDTO();
    }

}
