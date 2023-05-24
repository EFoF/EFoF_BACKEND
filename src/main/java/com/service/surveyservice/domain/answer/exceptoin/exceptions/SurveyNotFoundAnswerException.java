package com.service.surveyservice.domain.answer.exceptoin.exceptions;

public class SurveyNotFoundAnswerException extends RuntimeException{
    public SurveyNotFoundAnswerException(){
        super("해당 설문이 존재하지 않습니다.");
    }
}
