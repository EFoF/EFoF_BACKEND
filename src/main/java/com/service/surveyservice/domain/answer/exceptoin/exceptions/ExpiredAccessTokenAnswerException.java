package com.service.surveyservice.domain.answer.exceptoin.exceptions;

public class ExpiredAccessTokenAnswerException extends RuntimeException{
    public ExpiredAccessTokenAnswerException(){
        super("Access Token이 만료되었습니다.");
    }
}
