package com.service.surveyservice.domain.member.exception.response;

import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MemberErrorResponse {

    public static final ResponseEntity<ErrorResponse> USER_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("해당 사용자를 찾을 수 없습니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> USER_NOT_FOUND_BY_USERNAME_AND_NICKNAME = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("해당 본명과 닉네임으로 존재하는 유저를 찾을 수 없습니다.")
            .build(), HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> UPDATE_DUPLICATED_PASSWORD = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.CONFLICT)
            .message("변경하려는 비밀번호가 기존 비밀번호와 동일합니다.")
            .build(),HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> UPDATE_DUPLICATED_NICKNAME = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.CONFLICT)
            .message("변경하려는 닉네임이 기존 닉네임과 동일합니다.")
            .build(),HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> NOT_MATCHING_UPDATING_NICKNAME = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.CONFLICT)
            .message("해당 이메일을 통해 찾은 사용자의 닉네임이 전달받은 닉네임과 일치하지 않습니다.")
            .build(),HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> NOT_MATCHING_PASSWORD = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.CONFLICT)
            .message("해당 이메일을 통해 찾은 사용자의 비밀번호가 전달받은 비밀번호와 일치하지 않습니다.")
            .build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> NOT_MATCHING_CURRENT_MEMBER_AND_REQUESTER = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.CONFLICT)
            .message("요청자와 현재 사용자가 일치하지 않습니다.")
            .build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> DUPLICATED_SIGN_UP = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.CONFLICT)
            .message("이미 연동이 완료된 계정입니다.")
            .build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> INVALID_EMAIL_AND_PASSWORD_REQUEST = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("아이디 또는 비밀번호가 일치하지 않습니다.")
            .build(), HttpStatus.BAD_REQUEST);
}
