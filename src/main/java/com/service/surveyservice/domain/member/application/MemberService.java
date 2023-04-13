package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.domain.member.dto.MemberDTO.RedunCheckDTO;
import com.service.surveyservice.domain.member.exception.member.UserNotFoundByUsernameAndNickname;
import com.service.surveyservice.domain.member.exception.member.UserNotFoundException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.global.common.constants.RandomCharacters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.service.surveyservice.domain.member.dto.MailDTO.*;
import static com.service.surveyservice.domain.member.dto.MemberDTO.*;


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
        String userName = findEmailDTO.getUsername();
        String nickname = findEmailDTO.getNickname();
        Member member = memberRepository.findByUserNameAndNickname(userName, nickname).orElseThrow(UserNotFoundByUsernameAndNickname::new);
        return member.encryptEmailDTO();
    }

    @Transactional
    public ReturnPasswordDTO findUserPassword(FindPasswordRequestDTO findPasswordRequestDTO) {
        String userName = findPasswordRequestDTO.getUserName();
        String nickname = findPasswordRequestDTO.getNickname();
        String email = findPasswordRequestDTO.getEmail();
        Member member = memberRepository.findByUserNameAndNicknameAndEmail(userName, nickname, email).orElseThrow(UserNotFoundException::new);
        String newPassword = createTemporalPassword();
        UpdateUserPasswordRequestDTO updateUserPasswordRequestDTO = UpdateUserPasswordRequestDTO.builder()
                .oldPassword(member.getPassword())
                .newPassword(newPassword)
                .build();
        updateUserPasswordRequestDTO.encrypt(passwordEncoder);
        member.updatePasswordWithDTO(updateUserPasswordRequestDTO);
        return ReturnPasswordDTO.builder()
                .password(newPassword)
                .build();
    }

    private String createTemporalPassword() {
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(targetStringLength);
        for(int i=0; i<targetStringLength; i++) {
            sb.append(RandomCharacters.RandomCharacters[(int)(random.nextFloat() * RandomCharacters.RandomCharacters.length)]);
        }
        return sb.toString();
    }

}
