package com.service.surveyservice.domain.answer.dto;


import com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionBySectionForStatisticResponseDto {
        public QuestionBySectionForStatisticResponseDto toResponseDto;
        private Long section_id;
        private String question_text;
        private String question_type;
        private String questionOrder;

//        private List<GraphInfo> graphInfo;

        public QuestionBySectionForStatisticResponseDto toResponseDto(Section section) {
            return QuestionBySectionForStatisticResponseDto.builder()
                    .section_id(section.getId())
                    .build();
        }

//        public void setGraphInfo(GraphInfo graphInfo) {
//            this.graphInfo = graphInfo;
//        }
    }
}
