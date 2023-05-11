package com.service.surveyservice.domain.member.exception.handler;

import com.service.surveyservice.domain.member.exception.exceptions.member.*;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;
import java.util.List;

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

    @ExceptionHandler(DuplicatedSignUpException.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicatedSignUpException(DuplicatedSignUpException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return DUPLICATED_SIGN_UP;
    }

    @ExceptionHandler(InvalidEmailAndPasswordRequestException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidEmailAndPasswordRequestException(InvalidEmailAndPasswordRequestException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return INVALID_EMAIL_AND_PASSWORD_REQUEST;
    }

    @ExceptionHandler(NotSignInException.class)
    protected final ResponseEntity<ErrorResponse> handleNotSignInException(NotSignInException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return NOT_SIGN_IN;
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return DUPLICATED_EMAIL;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        String message = getMessage(allErrors.iterator());
        // validate 관련 핸들링은 Message 때문에 예외적으로 여기서 responseEntity를 직접 만들어 반환하겠다.
        ErrorResponse errorResponse = ErrorResponse.builder()
                .exceptionName(MethodArgumentNotValidException.class.getSimpleName())
                .message(message)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    private String getMessage(Iterator<ObjectError> errorIterator) {
        final StringBuilder resultMessageBuilder = new StringBuilder();
        while(errorIterator.hasNext()) {
            ObjectError error = errorIterator.next();
            resultMessageBuilder
                    .append(error.getDefaultMessage());

            if(errorIterator.hasNext()) {
                resultMessageBuilder.append(", ");
            }
        }
        return resultMessageBuilder.toString();
    }
}
