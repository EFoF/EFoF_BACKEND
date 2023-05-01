package com.service.surveyservice.domain.question.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionOptionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionOptionRequestDTO{
        private String id;
        private String option;
        private String image;
        private String nextSectionId;
    }
}
