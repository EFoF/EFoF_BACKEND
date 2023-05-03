package com.service.surveyservice.domain.section.dto;


import com.service.surveyservice.domain.question.dto.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SectionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveSectionRequestDto{
        private String id;
        private String nextSectionId;
        private List<QuestionDTO.SaveSurveyQuestionRequestDto> questionList;


    }
}
