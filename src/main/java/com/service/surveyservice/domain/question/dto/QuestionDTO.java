package com.service.surveyservice.domain.question.dto;


import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionType;
import com.service.surveyservice.domain.section.model.Section;
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
    public static class SaveSurveyQuestionRequestDto {
        private String id;
        private int type;
        private String questionContent;
        private Boolean isNecessary;

        private List<QuestionOptionDTO.SaveQuestionOptionRequestDTOInit> options;

        public Question toEntity(Section section) {
            return Question.builder()
                    .questionType(QuestionType.fromId(this.type))
                    .questionText(this.questionContent)
                    .isNecessary(this.isNecessary)
                    .section(section).build();
        }

    }

    public static List<Question> toEntities(List<SaveSurveyQuestionRequestDto> dtoList, Section section) {
        return dtoList.stream()
                .map(dto -> dto.toEntity(section))
                .collect(Collectors.toList());
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionRequestDto {
        private int type;
        private String questionContent;
        private Boolean isNecessary;


        public Question toEntity(Section section){
            return Question.builder()
                    .section(section)
                    .questionText(this.getQuestionContent())
                    .isNecessary(this.getIsNecessary())
                    .questionType(QuestionType.fromId(this.getType()))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseSaveQuestionDto{
        private Long question_id;
        private QuestionType questionType;
        private String questionText;
        private Boolean isNecessary;
        private Long section_id;
    }

}
