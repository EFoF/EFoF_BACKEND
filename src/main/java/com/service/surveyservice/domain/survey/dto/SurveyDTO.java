package com.service.surveyservice.domain.survey.dto;


import com.service.surveyservice.domain.survey.model.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SurveyDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSurveyRequestDTO {
        private String title;
        private String description;
        private Long author;
        private String surveyImageUrl;
        private String pointColor;
        private SurveyStatus surveyStatus;
        private LocalDateTime openDate;
        private LocalDateTime expireDate;
        // 제약, 섹션, memberSurvey는 설문이 만들어진 뒤에 생성되어 insert 된다.
    }
}
