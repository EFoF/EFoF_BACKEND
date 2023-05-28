package com.service.surveyservice.domain.survey.exception.response;

import com.service.surveyservice.domain.member.exception.exceptions.member.UserNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyPreMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveySectionMisMatchException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SurveyErrorResponse {

    public static final ResponseEntity<ErrorResponse> SURVEY_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyNotFoundException.class.getSimpleName())
            .message("해당 설문은 존재하지 않는 설문입니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> SURVEY_MEMBER_MISMATCH = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyMemberMisMatchException.class.getSimpleName())
            .message("설문 생성자와 일치하지 않습니다.")
            .build(),HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<ErrorResponse> SURVEY_SECTION_MISMATCH = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveySectionMisMatchException.class.getSimpleName())
            .message("섹션의 설문이 잘못되었습니다.")
            .build(),HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<ErrorResponse> SURVEY_PRE_MISMATCH = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyPreMisMatchException.class.getSimpleName())
            .message("현재 설문은 임시저장 상태가 아닙니다.")
            .build(),HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<ErrorResponse> FILE_SIZE_EXCEED = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(FileSizeLimitExceededException.class.getSimpleName())
            .message("업로드한 사진의 크기가 최대 용량을 초과합니다.")
            .build(),HttpStatus.CONFLICT);

}
