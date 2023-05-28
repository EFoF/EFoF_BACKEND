package com.service.surveyservice.domain.answer.exceptoin.response;

import com.service.surveyservice.domain.answer.exceptoin.exceptions.*;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.token.exception.exceptions.ExpiredAccessTokenException;
import com.service.surveyservice.domain.token.exception.exceptions.NoSuchCookieException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AnswerErrorResponse {


    public static final ResponseEntity<ErrorResponse> AUTHOR_MEMBER_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(AuthorOrMemberNotFoundException.class.getSimpleName())
            .message("통계에 대한 권한이 없습니다.")
            .build(),HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<ErrorResponse> NO_SUCH_COOKIE_ANSWER = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(NoSuchCookieAnswerException.class.getSimpleName())
            .message("로그인이 필요합니다.")
            .build(), HttpStatus.UNAUTHORIZED);

    public static final ResponseEntity<ErrorResponse> ACCESS_TOKEN_EXPIRE_ANSWER = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(ExpiredAccessTokenAnswerException.class.getSimpleName())
            .message("로그인이 필요합니다.")
            .build(), HttpStatus.UNAUTHORIZED);

    public static final ResponseEntity<ErrorResponse> SURVEY_NOT_FOUND_ANSWER = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyNotFoundAnswerException.class.getSimpleName())
            .message("해당 설문이 존재하지 않습니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> DUPLICATED_PARTICIPATE = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(DuplicatedParticipateException.class.getSimpleName())
            .message("이미 참여한 설문입니다.")
            .build(),HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> NO_NECESSARY_ANSWER = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(NoNecessaryAnswerException.class.getSimpleName())
            .message("필수 응답에 모두 답해주세요.")
            .build(),HttpStatus.BAD_REQUEST);
}
