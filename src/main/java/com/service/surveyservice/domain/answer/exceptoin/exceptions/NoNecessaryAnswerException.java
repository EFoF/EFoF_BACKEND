package com.service.surveyservice.domain.answer.exceptoin.exceptions;

public class NoNecessaryAnswerException extends RuntimeException{
    public NoNecessaryAnswerException() {
        super("필수 응답에 모두 답해주세요.");
    }
}
