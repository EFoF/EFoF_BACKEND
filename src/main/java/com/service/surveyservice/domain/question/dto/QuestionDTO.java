package com.service.surveyservice.domain.question.dto;


import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionType;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionRequestDto{
        private String id;
        private int type;
        private String questionContent;
        private boolean isNecessary;
        private List<SectionDTO.SaveSectionRequestDto> options;

        public Question toEntity(Section section){
            return Question.builder()
                    .questionType(QuestionType.fromId(this.type))
                    .questionText(this.questionContent)
                    .isNecessary(this.isNecessary)
                    .section(section).build();
        }

    }
}
