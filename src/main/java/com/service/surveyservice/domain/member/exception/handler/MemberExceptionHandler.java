package com.service.surveyservice.domain.member.exception.handler;

import com.service.surveyservice.domain.member.exception.exceptions.member.*;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.member.exception.response.MemberErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return USER_NOT_FOUND;
    }

    @ExceptionHandler(UserNotFoundByUsernameAndNickname.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFoundByUsernameAndNicknameException(UserNotFoundByUsernameAndNickname ex, WebRequest request) {
        log.error(request.getDescription(false));
        return USER_NOT_FOUND_BY_USERNAME_AND_NICKNAME;
    }

    @ExceptionHandler(UpdateDuplicatedPasswordException.class)
    protected final ResponseEntity<ErrorResponse> handleUpdateDuplicatedPasswordException(UpdateDuplicatedPasswordException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return UPDATE_DUPLICATED_PASSWORD;
    }

    @ExceptionHandler(UpdateDuplicatedNicknameException.class)
    protected final ResponseEntity<ErrorResponse> handleUpdateDuplicatedNicknameException(UpdateDuplicatedNicknameException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return UPDATE_DUPLICATED_NICKNAME;
    }

    @ExceptionHandler(NotMatchingUpdatingNicknameException.class)
    protected final ResponseEntity<ErrorResponse> handleNotMatchingUpdatingNicknameException(NotMatchingUpdatingNicknameException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return NOT_MATCHING_UPDATING_NICKNAME;
    }

    @ExceptionHandler(NotMatchingPasswordException.class)
    protected final ResponseEntity<ErrorResponse> handleNotMatchingPasswordException(NotMatchingPasswordException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return NOT_MATCHING_PASSWORD;
    }

    @ExceptionHandler(NotMatchingCurrentMemberAndRequesterException.class)
    protected final ResponseEntity<ErrorResponse> handleNotMatchingCurrentMemberAndRequesterException(NotMatchingCurrentMemberAndRequesterException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return NOT_MATCHING_CURRENT_MEMBER_AND_REQUESTER;
    }
}