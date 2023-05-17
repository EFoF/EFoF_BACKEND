package com.service.surveyservice.domain.question.dto;


import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.question.model.QuestionType;
import com.service.surveyservice.domain.section.model.Section;
import lombok.*;

import java.util.ArrayList;
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
                    .isNecessary(false)
                    .questionType(QuestionType.ONE_CHOICE)
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createSectionResponseQuestionDto{
        private Long id;
        private Long type;
        private String questionContent;
        private Boolean isNecessary;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class QuestionQueryDto{
        private Long id;
        private QuestionType type;
        private String questionContent;
        private Boolean isNecessary;
        private Long sectionId;
        private List<QuestionOptionDTO.QuestionOptionQueryDto> questionOptions;

        public QuestionQueryDto(Long id, QuestionType type, String questionContent, Boolean isNecessary, Long sectionId) {
            this.id = id;
            this.type = type;
            this.questionContent = questionContent;
            this.isNecessary = isNecessary;
            this.sectionId = sectionId;
        }

        public void setQuestionOptions(List<QuestionOptionDTO.QuestionOptionQueryDto> questionOptions) {
            this.questionOptions = questionOptions;
        }
    }

}
