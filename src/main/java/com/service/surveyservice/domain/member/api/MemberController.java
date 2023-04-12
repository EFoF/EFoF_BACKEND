package com.service.surveyservice.domain.member.api;

import com.service.surveyservice.domain.member.application.AuthService;
import com.service.surveyservice.domain.member.application.EmailCertificationService;
import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.member.dto.MailDTO.EmailSentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.service.surveyservice.domain.member.dto.MailDTO.*;
import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final EmailCertificationService emailCertificationService;

    @PostMapping(value = "/auth/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberLoginDTO> loginJson (@RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authService.login(loginRequestDTO), HttpStatus.OK);
    }

    // 이메일 중복 확인
    @GetMapping(value = "/auth/check-email")
    public ResponseEntity<RedunCheckDTO> emailDuplicationCheck(@RequestBody CheckEmailRequestDTO checkEmailRequestDTO) {
        return new ResponseEntity<>(memberService.existsByEmail(checkEmailRequestDTO.getEmail()), HttpStatus.OK);
    }

    // 닉네임 중복 확인
    @GetMapping(value = "/auth/check-nickname")
    public ResponseEntity<RedunCheckDTO> nicknameDuplicationCheck(@RequestBody CheckNicknameRequestDTO checkNicknameRequestDTO) {
        return new ResponseEntity<>(memberService.existsByNickname(checkNicknameRequestDTO.getNickname()), HttpStatus.OK);
    }

    // 이메일 인증
    @GetMapping(value = "/auth/mailConfirm")
    public ResponseEntity<EmailSentDTO> mailConfirm(@RequestBody ConfirmEmailDTO confirmEmailDTO) throws Exception {
        emailCertificationService.sendSimpleMessage(confirmEmailDTO.getEmail());
        return new ResponseEntity<>(EmailSentDTO.builder().email(confirmEmailDTO.getEmail()).success(true).build(), HttpStatus.OK);
    }

    @GetMapping(value = "/auth/find/email")
    public ResponseEntity<CodeConfirmDTO> codeConfirm(@RequestBody EmailConfirmCodeDTO emailConfirmCodeDTO) {
        return new ResponseEntity<>(emailCertificationService.confirmCode(emailConfirmCodeDTO), HttpStatus.OK);
    }

//    @GetMapping(value = "/auth/")
}
