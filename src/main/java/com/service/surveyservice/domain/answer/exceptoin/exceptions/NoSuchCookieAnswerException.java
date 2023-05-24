package com.service.surveyservice.domain.answer.exceptoin.exceptions;

public class NoSuchCookieAnswerException extends RuntimeException{
    public NoSuchCookieAnswerException(){
        super("해당하는 쿠키가 존재하지 않습니다.");
    }
}
