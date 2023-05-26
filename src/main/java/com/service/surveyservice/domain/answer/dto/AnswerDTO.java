package com.service.surveyservice.domain.answer.dto;


import com.service.surveyservice.domain.answer.dao.AnswerRepository;
import com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

public class AnswerDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurveyForStatisticResponseDto {
        private String title; //설문 제목
        private String description; //설문 설명
        private String sImageURL; //설문 이미지
        private int participantNum; //참여자 수
        private List<Long> SectionList; //설문의 리스트

        public SurveyForStatisticResponseDto toResponseDto(
                Survey survey,
                int participantNum,
                List<Long> sectionList) {

            return SurveyForStatisticResponseDto.builder()
                    .title(survey.getTitle())
                    .description(survey.getDescription())
                    .sImageURL(survey.getSImageURL())
                    .participantNum(participantNum)
                    .SectionList(sectionList)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ParticipateAnswerDTO {
        private Long questionId;
        private Boolean isNecessary;
        private String answerSentence;
        private Long questionType;
        private List<Long> questionChoiceId;
    }

    @Getter
    @NoArgsConstructor
    public static class ParticipateAnswerListDTO {
        private Long surveyId;
        private List<ParticipateAnswerDTO> participateAnswerDTOList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerForBatch {
        private Long questionId;
        private Long questionOptionId;
        private String answerSentence;
        private Long memberSurveyId;
        private Long questionType;
        private Boolean isNecessary;
        private List<ConstraintType> constraintTypeList;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionBySectionForStatisticResponseDto {

        private String title; //설문 제목
        private String description; //설문 설명
        private String sImageURL; //설문 이미지
        private int participantNum; //참여자 수
        private List<Long> SectionList; //설문의 리스트

        private String question_text;
        private String question_type;

        public QuestionBySectionForStatisticResponseDto toResponseDto(
                Question question,
                int participantNum,
                List<Long> sectionList) {

            return QuestionBySectionForStatisticResponseDto.builder()
//                    .title(survey.getTitle())
//                    .description(survey.getDescription())
//                    .sImageURL(survey.getSImageURL())
                    .question_text(question.getQuestionText())
                    .question_type(String.valueOf(question.getQuestionType()))  // 일단은..? 나중에 물어보자
                    .participantNum(participantNum)
                    .SectionList(sectionList)
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ChoiceAnswerResponseDto{    // 객관식
        private Long question_id;
        private Long question_choice_id;
        private int participant_num_question_option;
        private String option_text;
        private String question_option_img;

        public ChoiceAnswerResponseDto(AnswerRepository.choiceAnswerResponseDtoI choiceAnswerResponseDtoI) {
            this.question_id = choiceAnswerResponseDtoI.getQuestion_id();
            this.question_choice_id = choiceAnswerResponseDtoI.getQuestion_choice_id();
            this.participant_num_question_option = choiceAnswerResponseDtoI.getParticipant_num_question_option();
            this.option_text = choiceAnswerResponseDtoI.getOption_text();
            this.question_option_img = choiceAnswerResponseDtoI.getQuestion_option_img();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class LongAnswerResponseDto{  // 주관식
        private Long question_id;
        private String answer_sentence;

        public LongAnswerResponseDto(AnswerRepository.longAnswerResponseDtoI longAnswerResponseDtoI) {
            this.question_id = longAnswerResponseDtoI.getQuestion_id();
            this.answer_sentence = longAnswerResponseDtoI.getAnswer_sentence();
        }
    }
}
