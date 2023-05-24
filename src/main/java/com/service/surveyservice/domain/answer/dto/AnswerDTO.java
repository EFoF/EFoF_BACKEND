package com.service.surveyservice.domain.answer.dto;


import com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public static class ChoiceAnswerDto{    // 객관식
        private Long question_id;
        private Long question_choice_id;
        private int participant_num_question_option;
        private String question_text;
        private String question_type;
        private String question_option_img;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LongAnswerDto{  // 주관식
        private Long question_id;
        private String answer_sentence;
    }
}
