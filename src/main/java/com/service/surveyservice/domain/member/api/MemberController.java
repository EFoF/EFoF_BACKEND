package com.service.surveyservice.domain.member.api;

import com.service.surveyservice.domain.member.application.AuthService;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;

    @PostMapping(value = "/auth/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberLoginDTO> loginJson (@RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authService.login(loginRequestDTO), HttpStatus.OK);
    }
}
