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
import java.util.stream.Collectors;

public class QuestionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionRequestDto {
        private String id;
        private int type;
        private String questionContent;
        private boolean isNecessary;
        private List<QuestionOptionDTO.SaveQuestionOptionRequestDTO> options;

        public Question toEntity(Section section) {
            return Question.builder()
                    .questionType(QuestionType.fromId(this.type))
                    .questionText(this.questionContent)
                    .isNecessary(this.isNecessary)
                    .section(section).build();
        }

    }

    public static List<Question> toEntities(List<SaveQuestionRequestDto> dtoList, Section section) {
        return dtoList.stream()
                .map(dto -> dto.toEntity(section))
                .collect(Collectors.toList());
    }
}
