package com.service.surveyservice.domain.answer.exceptoin.exceptions;

public class DuplicatedParticipateException extends RuntimeException{
    public DuplicatedParticipateException() {
        super("이미 참여한 설문입니다.");
    }
}
