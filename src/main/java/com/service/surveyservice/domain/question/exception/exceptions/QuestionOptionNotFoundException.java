package com.service.surveyservice.domain.question.exception.exceptions;

public class QuestionOptionNotFoundException extends RuntimeException{
    public QuestionOptionNotFoundException() {
        super("이미 다른 사용자가 사용 중인 이메일입니다.");
    }

    public QuestionOptionNotFoundException(String message) {
        super(message);
    }
}
