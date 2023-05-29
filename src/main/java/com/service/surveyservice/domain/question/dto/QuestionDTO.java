package com.service.surveyservice.domain.question.dto;


import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.model.Question;
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
        private Boolean hasImage;

        private List<QuestionOptionDTO.SaveQuestionOptionRequestDTOInit> options;

        public Question toEntity(Section section) {
            return Question.builder()
                    .questionType(QuestionType.fromId(this.type))
                    .questionText(this.questionContent)
                    .isNecessary(this.isNecessary)
                    .hasImage(this.hasImage)
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
        private Long id;
        private QuestionType type;
        private String questionContent;
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
        private List<String> answers = new ArrayList<>();
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
        private long type;
        private List<String> answers;
        private String questionContent;
        private Boolean isNecessary;
        private Long sectionId;
        private List<QuestionOptionDTO.QuestionOptionQueryDto> options;
        private Boolean hasImage;

        public QuestionQueryDto(Long id, QuestionType type, String questionContent, Boolean isNecessary, Boolean hasImage, Long sectionId) {
            this.id = id;
            this.type = type.getId();
            this.questionContent = questionContent;
            this.isNecessary = isNecessary;
            this.sectionId = sectionId;
            this.hasImage = hasImage;
            this.answers = new ArrayList<>();
        }

        public void setOptions(List<QuestionOptionDTO.QuestionOptionQueryDto> options) {
            this.options = options;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class QuestionInfoByIdDto{
        private Long question_id;
        private String question_text;
        private QuestionType question_type;
        private int participant_num_question;
        List<AnswerDTO.LongAnswerResponseDto> longAnswerDtos;
        List<AnswerDTO.ChoiceAnswerResponseDto> choiceAnswerDtos;

        public QuestionInfoByIdDto(QuestionRepository.questionInfoByIdDtoI questionOptionByQuestionDtoI) {
            this.question_id = questionOptionByQuestionDtoI.getQuestion_id();
            this.question_text = questionOptionByQuestionDtoI.getQuestion_text();
            this.question_type = questionOptionByQuestionDtoI.getQuestion_type();
            this.participant_num_question = questionOptionByQuestionDtoI.getParticipant_num_question();
        }

        public void setLongAnswerDtos(List<AnswerDTO.LongAnswerResponseDto> longAnswerDtos) {
            this.longAnswerDtos = longAnswerDtos;
        }
        public void setChoiceAnswerDtos(List<AnswerDTO.ChoiceAnswerResponseDto> choiceAnswerDtos) {
            this.choiceAnswerDtos = choiceAnswerDtos;
        }

        // =======================================================

        public AnswerDTO.QuestionBySectionForStatisticResponseDto toResponseDto(List<QuestionInfoByIdDto> questionInfoById) {

            return AnswerDTO.QuestionBySectionForStatisticResponseDto.builder()
                    .build();
        }
    }
}
