package com.service.surveyservice.member;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.member.dao.MemberCustomRepository;
import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MailDTO;
import com.service.surveyservice.domain.member.exception.exceptions.member.NotMatchingPasswordException;
import com.service.surveyservice.domain.member.exception.exceptions.member.UpdateDuplicatedPasswordException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.dto.MemberDTO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;


    @Mock
    private MemberCustomRepositoryImpl memberCustomRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .email("email@example.com")
                .username("testName")
                .password("encryptedPassword")
                .nickname("oldNickname").build();
    }

    @Test
    void existsByEmail() {
        when(memberRepository.existsByEmail("email@example.com")).thenReturn(true);

        RedunCheckDTO result = memberService.existsByEmail("email@example.com");

        assertTrue(result.getExists());
    }

    @Test
    void existsByNickname() {
        when(memberRepository.existsByNickname("testName")).thenReturn(true);

        RedunCheckDTO result = memberService.existsByNickname("testName");

        assertTrue(result.getExists());
    }



    @Test
    void updatePassword() {
        UpdateMemberPasswordRequestDTO updatePasswordDTO = new UpdateMemberPasswordRequestDTO("email@example.com","oldPassword", "newPassword");
        when(passwordEncoder.encode("oldPassword")).thenReturn("oldEncryptedPassword");
        when(passwordEncoder.matches("oldPassword", testMember.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncryptedPassword");
        when(memberRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));

        memberService.updatePassword(updatePasswordDTO,testMember.getId());

        assertEquals("newEncryptedPassword", testMember.getPassword());
    }

    @Test
    void updatePasswordVisitor() {
        UpdateMemberPasswordVisitorRequestDTO updatePasswordVisitorDTO = new UpdateMemberPasswordVisitorRequestDTO("email@example.com", "newPassword");
        when(memberRepository.findByEmail("email@example.com")).thenReturn(Optional.of(testMember));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncryptedPassword");

        memberService.updatePasswordVisitor(updatePasswordVisitorDTO);

        assertEquals("newEncryptedPassword", testMember.getPassword());
    }

    @Test
    void updateMemberNickname() {
        UpdateNicknameRequestDTO updateNicknameDTO
                = new UpdateNicknameRequestDTO("email@example.com","oldNickname","newNickname");
        when(memberRepository.findByEmail("email@example.com")).thenReturn(Optional.of(testMember));


        memberService.updateMemberNickname(updateNicknameDTO,testMember.getId());

        assertEquals("newNickname", testMember.getNickname());
    }

    @Test
    void getMyInfoDetail() {
        MemberDetail memberDetail = MemberDetail.builder()
                .id(testMember.getId())
                .email(testMember.getEmail())
                .nickname(testMember.getNickname())
                .username(testMember.getUserName()).build();
        when(memberCustomRepository.getMemberDetailOptional(testMember.getId())).thenReturn(Optional.of(memberDetail));


        MemberDetail result = memberService.getMyInfoDetail(testMember.getId());

        assertEquals(testMember.getId(), result.getId());
        assertEquals(testMember.getNickname(), result.getNickname());
        assertEquals(testMember.getEmail(), result.getEmail());
        assertEquals(testMember.getUserName(), result.getUsername());
    }


    @Test
    void existsByEmail_userNotFoundException() {
        String email = "test@example.com";
        when(memberRepository.existsByEmail(email)).thenReturn(false);

        RedunCheckDTO result = memberService.existsByEmail(email);
        assertFalse(result.getExists());
    }

    @Test
    void existsByNickname_userNotFoundException() {
        String nickname = "test_nick";
        when(memberRepository.existsByNickname(nickname)).thenReturn(false);

        RedunCheckDTO result = memberService.existsByNickname(nickname);
        assertFalse(result.getExists());
    }

    @Test
    void updatePassword_notMatchingPasswordException() {
        when(passwordEncoder.matches("wrong_password", testMember.getPassword())).thenReturn(false);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        UpdateMemberPasswordRequestDTO requestDTO = new UpdateMemberPasswordRequestDTO("email@example.com","wrong_password", "newPassword");

        assertThrows(NotMatchingPasswordException.class, () -> memberService.updatePassword(requestDTO, 1L));
    }

    @Test
    void updatePassword_updateDuplicatedPasswordException() {
        when(passwordEncoder.matches("oldPassword", testMember.getPassword())).thenReturn(true);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        UpdateMemberPasswordRequestDTO requestDTO = new UpdateMemberPasswordRequestDTO("email@example.com","oldPassword", "oldPassword");

        assertThrows(UpdateDuplicatedPasswordException.class, () -> memberService.updatePassword(requestDTO, 1L));
    }

    @Test
    void updatePasswordVisitor_updateDuplicatedPasswordException() {
        when(passwordEncoder.matches("password1", testMember.getPassword())).thenReturn(true);
        when(memberRepository.findByEmail("email@example.com")).thenReturn(Optional.of(testMember));

        UpdateMemberPasswordVisitorRequestDTO requestDTO =new UpdateMemberPasswordVisitorRequestDTO("email@example.com", "password1");

        assertThrows(UpdateDuplicatedPasswordException.class, () -> memberService.updatePasswordVisitor(requestDTO));
    }

}
