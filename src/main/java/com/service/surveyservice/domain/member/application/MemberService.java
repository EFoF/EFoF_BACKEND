package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.answer.exceptoin.exceptions.AuthorOrMemberNotFoundException;
import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO.RedunCheckDTO;
import com.service.surveyservice.domain.member.exception.exceptions.member.*;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.global.common.constants.RandomCharacters;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.service.surveyservice.domain.member.dto.MailDTO.*;
import static com.service.surveyservice.domain.member.dto.MemberDTO.*;
import static com.service.surveyservice.global.common.constants.ResponseConstants.UPDATED;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCustomRepositoryImpl memberCustomRepository;
    private final PasswordEncoder passwordEncoder;


    // 중복되는 이메일이 존재하는지 검사
    @Transactional(readOnly = true)
    public RedunCheckDTO existsByEmail(String email) {
        return new RedunCheckDTO(memberRepository.existsByEmail(email));
    }

    // 중복되는 닉네임이 존재하는지 검사
    @Transactional(readOnly = true)
    public RedunCheckDTO existsByNickname(String nickname) {
        return new RedunCheckDTO(memberRepository.existsByNickname(nickname));
    }

    // 이메일 찾기 기능에서 유저 정보를 찾아서 암호화된 이메일을 반환함
    @Transactional(readOnly = true)
    public EncryptEmailDTO findUserEmail(FindEmailDTO findEmailDTO) {
        String userName = findEmailDTO.getUsername();
        String nickname = findEmailDTO.getNickname();
        Member member = memberRepository.findByUserNameAndNickname(userName, nickname).orElseThrow(UserNotFoundByUsernameAndNickname::new);
        return member.encryptEmailDTO();
    }

    // 비밀번호 찾기 기능에서 유저 정보를 찾아서 새로운 비밀번호로 변경 후 반환
    @Transactional
    public ReturnPasswordDTO findUserPassword(FindPasswordRequestDTO findPasswordRequestDTO) {
        String userName = findPasswordRequestDTO.getUserName();
        String nickname = findPasswordRequestDTO.getNickname();
        String email = findPasswordRequestDTO.getEmail();
        Member member = memberRepository.findByUserNameAndNicknameAndEmail(userName, nickname, email).orElseThrow(UserNotFoundException::new);
        String newPassword = createTemporalPassword();
        UpdateGeneratedPasswordRequestDTO updateGeneratedPasswordRequestDTO = UpdateGeneratedPasswordRequestDTO.builder()
                .oldPassword(member.getPassword())
                .generatedPassword(newPassword)
                .build();
        updateGeneratedPasswordRequestDTO.encrypt(passwordEncoder);
        member.updateToGeneratedPasswordWithDTO(updateGeneratedPasswordRequestDTO);
        return ReturnPasswordDTO.builder()
                .password(newPassword)
                .build();
    }

    // 비밀번호 찾기 기능에서 사용되는 임시 비밀번호 생성
    private String createTemporalPassword() {
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(targetStringLength);
        for(int i=0; i<targetStringLength; i++) {
            sb.append(RandomCharacters.RandomCharacters[(int)(random.nextFloat() * RandomCharacters.RandomCharacters.length)]);
        }
        return sb.toString();
    }

    // 비밀번호 변경
    @Transactional
    public String updatePassword(UpdateMemberPasswordRequestDTO updateMemberPasswordRequestDTO, Long currentMemberId) {
        String oldPassword = updateMemberPasswordRequestDTO.getOldPassword();
        String newPassword = updateMemberPasswordRequestDTO.getNewPassword();

        // 해당 이메일로 사용자를 찾을 수 없다면 예외 발생
        Member member = memberRepository.findById(currentMemberId).orElseThrow(UserNotFoundException::new);

        // 현재 사용자의 비밀번호가 전달 받은 비밀번호와 다르다면 예외 발생
        if(!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new NotMatchingPasswordException();
        }

        // 변경할 비밀번호가 현재 비밀번호랑 일치한다면 예외 발생
        if(oldPassword.equals(newPassword)) {
            throw new UpdateDuplicatedPasswordException();
        }

        // 비밀번호 변경
        updateMemberPasswordRequestDTO.encrypt(passwordEncoder);
        member.updatePasswordWithDTO(updateMemberPasswordRequestDTO);
        return UPDATED;
    }

    // 비밀번호 변경 - 로그인 안 된 상황
    @Transactional
    public String updatePasswordVisitor(UpdateMemberPasswordVisitorRequestDTO updateMemberPasswordVisitorRequestDTO) {
        String email = updateMemberPasswordVisitorRequestDTO.getEmail();
        String newPassword = updateMemberPasswordVisitorRequestDTO.getNewPassword();

        // 해당 이메일로 사용자를 찾을 수 없다면 예외 발생
        Member member = memberRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        String oldPassword = member.getPassword();

        // 기존 사용자의 비밀번호와 바꾸고자하는 비밀번호와 같다면 예외 발생
        if(passwordEncoder.matches(newPassword, oldPassword)) {
            throw new UpdateDuplicatedPasswordException();
        }

        // 비밀번호 변경
        updateMemberPasswordVisitorRequestDTO.encrypt(passwordEncoder);
        member.updatePasswordVisitorWithDTO(updateMemberPasswordVisitorRequestDTO);
        return UPDATED;
    }

    @Transactional
    public String updateMemberNickname(UpdateNicknameRequestDTO updateNicknameRequestDTO, Long currentMemberId) {
        String email = updateNicknameRequestDTO.getEmail();
        String oldNickname = updateNicknameRequestDTO.getOldNickname();
        String newNickname = updateNicknameRequestDTO.getNewNickname();

        Member member = memberRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if(!member.getId().equals(currentMemberId)) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }

        if(!member.getNickname().equals(oldNickname)) {
            throw new NotMatchingUpdatingNicknameException();
        }

        if(oldNickname.equals(newNickname)) {
            throw new UpdateDuplicatedNicknameException();
        }

        // 닉네임 변경
        member.updateNickname(newNickname);

        return UPDATED;
    }

    @Transactional
    public String updateMemberProfileImg(UpdateMemberProfileImgRequestDTO updateMemberProfileImgRequestDTO, Long currentMemberId) {
        String email = updateMemberProfileImgRequestDTO.getEmail();
        String newProfileImg = updateMemberProfileImgRequestDTO.getNewProfileImg();
        Member member = memberRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(!member.getId().equals(currentMemberId)) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }
        // 프로필 이미지 변경
        member.updateProfileImg(newProfileImg);
        return UPDATED;
    }

//    // 사용자 세부 정보 조회
//    @Transactional(readOnly = true)
//    public MemberDetail getMemberDetail(long id, Long currentMemberId) {
//        log.error("아이디" + id);
//        if(id != currentMemberId) {
//            throw new NotMatchingCurrentMemberAndRequesterException();
//        }
//        MemberDetail memberDetail = memberCustomRepository.getMemberDetailOptional(id).orElseThrow(NotFoundByIdException::new);
//        return memberDetail;
//    }

    // 마이페이지용 사용자 세부 정보 조회
    @Transactional(readOnly = true)
    public MemberDetail getMyInfoDetail(Long currentMemberId) {
        MemberDetail memberDetail = memberCustomRepository.getMemberDetailOptional(currentMemberId).orElseThrow(NotFoundByIdException::new);
        return memberDetail;
    }

}
