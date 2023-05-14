package com.service.surveyservice.domain.member.api;

import com.service.surveyservice.domain.member.application.AuthService;
import com.service.surveyservice.domain.member.application.EmailCertificationService;
import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.member.exception.exceptions.member.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static com.service.surveyservice.global.common.constants.JwtConstants.ACCESS_TOKEN;
import static com.service.surveyservice.global.common.constants.JwtConstants.TOKEN_PUBLISH_CONFIRM;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.service.surveyservice.domain.member.dto.MailDTO.*;
import static com.service.surveyservice.domain.member.dto.MemberDTO.*;
import static com.service.surveyservice.domain.member.dto.OAuthDTO.*;
import static com.service.surveyservice.domain.token.dto.TokenDTO.*;
import static com.service.surveyservice.global.common.constants.AuthenticationConstants.LOGOUT;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final EmailCertificationService emailCertificationService;

    /**
     *
     * @param signUpRequest
     * @return ResponseBody<String>
     * 회원가입 컨트롤러, 백엔드에서는 이메일 중복 정도만 체크함
     */

    @PostMapping(value = "/auth/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    /**
     *
     * @param loginRequestDTO
     * @param request
     * @param response
     * @return ResponseBody<MemberLoginDTO>
     * 로그인 컨트롤러, 토큰을 생성하고 쿠키, 서버(레디스)에 저장한다.
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberLoginDTO> loginJson (@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(authService.login(loginRequestDTO, request, response), HttpStatus.OK);
    }

    /**
     *
     * @param checkEmailRequestDTO
     * @return ResponseBody<RedunCheckDTO>
     * 이메일 중복 확인 컨트롤러
     */
    @PostMapping(value = "/auth/check-email")
    public ResponseEntity<RedunCheckDTO> emailDuplicationCheck(@RequestBody CheckEmailRequestDTO checkEmailRequestDTO) {
        return new ResponseEntity<>(memberService.existsByEmail(checkEmailRequestDTO.getEmail()), HttpStatus.OK);
    }

    /**
     *
     * @param checkNicknameRequestDTO
     * @return ResponseBody<RedunCheckDTO>
     * 닉네임 중복 확인 컨트롤러
     */
    @PostMapping(value = "/auth/check-nickname")
    public ResponseEntity<RedunCheckDTO> nicknameDuplicationCheck(@RequestBody CheckNicknameRequestDTO checkNicknameRequestDTO) {
        return new ResponseEntity<>(memberService.existsByNickname(checkNicknameRequestDTO.getNickname()), HttpStatus.OK);
    }

    // 이메일 인증

    /**
     *
     * @param confirmEmailDTO
     * @return ResponseBody<EmailSentDTO>
     * @throws Exception
     * 이메일 인증 컨트롤러, 인증 코드 생성과 동시에 이메일을 발송한다.
     */
    @PostMapping(value = "/auth/mailConfirm")
    public ResponseEntity<EmailSentDTO> mailConfirm(@RequestBody ConfirmEmailDTO confirmEmailDTO) throws Exception {
        emailCertificationService.sendSimpleMessage(confirmEmailDTO.getEmail());
        return new ResponseEntity<>(EmailSentDTO.builder().email(confirmEmailDTO.getEmail()).success(true).build(), HttpStatus.OK);
    }

    /**
     *
     * @param emailConfirmCodeDTO
     * @return ResponseBody<CodeConfirmDTO>
     * 이메일 인증에서 전송했던 코드가 일치한지 확인한다.
     */
    @PostMapping(value = "/auth/codeConfirm")
    public ResponseEntity<CodeConfirmDTO> codeConfirm(@RequestBody EmailConfirmCodeDTO emailConfirmCodeDTO) {
        log.info("이메일 : {}, 코드 : {}", emailConfirmCodeDTO.getEmail(), emailConfirmCodeDTO.getCode());
        return new ResponseEntity<>(emailCertificationService.confirmCode(emailConfirmCodeDTO), HttpStatus.OK);
    }

    /**
     *
     * @param findEmailDTO
     * @return ResponseBody<EncryptEmailDTO>
     * 이메일 찾기
     */
    @PostMapping(value = "/auth/find/email")
    public ResponseEntity<EncryptEmailDTO> findEmail(@RequestBody FindEmailDTO findEmailDTO) {
        EncryptEmailDTO userEmail = memberService.findUserEmail(findEmailDTO);
        return new ResponseEntity<>(userEmail, HttpStatus.OK);
    }

    /**
     *
     * @param findPasswordRequestDTO
     * @return ResponseBody<ReturnPasswordDTO>
     * 비밀번호 찾기
     */
    @PostMapping(value = "/auth/find/password")
    public ResponseEntity<ReturnPasswordDTO> findPassword(@RequestBody FindPasswordRequestDTO findPasswordRequestDTO) {
        ReturnPasswordDTO returnPasswordDTO = memberService.findUserPassword(findPasswordRequestDTO);
        return new ResponseEntity<>(returnPasswordDTO, HttpStatus.OK);
    }

    /**
     *
     * @return
     * 로그아웃 리디렉션
     */
    @GetMapping(value = "/logout-redirect")
    public ResponseEntity<String> loginRedirect(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(TOKEN_PUBLISH_CONFIRM) || cookie.getName().equals(ACCESS_TOKEN)) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return new ResponseEntity<>(LOGOUT, HttpStatus.OK);
    }

    /**
     *
     * @param request
     * @param response
     * @return ResponseBody<String> (null)
     * 토큰 reissue
     */
    @PostMapping(value = "/auth/reissue")
    public ResponseEntity<LoginLastDTO> reissue(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(authService.reissue(request,response), HttpStatus.OK);
    }

}
