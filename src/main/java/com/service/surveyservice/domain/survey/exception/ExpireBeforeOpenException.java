package com.service.surveyservice.domain.survey.exception;

public class ExpireBeforeOpenException extends IllegalArgumentException{
    public ExpireBeforeOpenException() {
        super("마감 기한이 설문 시작 기간보다 앞설 수 없습니다.");
    }

    public ExpireBeforeOpenException(String s) {
        super(s);
    }
}
