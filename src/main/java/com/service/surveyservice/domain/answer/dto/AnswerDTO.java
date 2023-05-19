package com.service.surveyservice.domain.answer.dto;


import com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.member.model.Member;
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

        private List<ConstraintDTO.SurveyForStatisticConstraintResponseDto> constraintList; //제약 조건 리스트

        public SurveyForStatisticResponseDto toResponseDto(
                Survey survey,
                int participantNum,
                List<Long> sectionList,
                List<ConstraintOptions> constraintList) {

            return SurveyForStatisticResponseDto.builder()
                    .title(survey.getTitle())
                    .description(survey.getDescription())
                    .sImageURL(survey.getSImageURL())
                    .participantNum(participantNum)
                    .SectionList(sectionList)
                    .constraintList(ConstraintOptions.toEntities(constraintList))
                    .build();
        }
    }
}
