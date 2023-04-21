package com.service.surveyservice.domain.survey.exception;

public class SurveyConvertException extends IllegalArgumentException {
    public SurveyConvertException() {
        super("MemberSurveyInfoDTO -> SurveyInfoDTO로의 변환이 실패했습니다.");
    }

    public SurveyConvertException(String s) {
        super(s);
    }
}
