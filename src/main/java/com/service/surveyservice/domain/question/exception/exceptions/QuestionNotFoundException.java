package com.service.surveyservice.domain.question.exception.exceptions;

public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException() {
        super("이미 다른 사용자가 사용 중인 이메일입니다.");
    }

    public QuestionNotFoundException(String message) {
        super(message);
    }
}
