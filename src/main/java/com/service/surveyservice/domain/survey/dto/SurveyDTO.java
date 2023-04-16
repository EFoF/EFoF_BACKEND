package com.service.surveyservice.domain.survey.dto;


import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SurveyDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSurveyRequestDTO {
        private String title;
        private String description;
        private Long author;
        private String surveyImageUrl;
        private String pointColor;
        private LocalDateTime openDate;
        private LocalDateTime expireDate;

        // surveyStatus는 openDate를 보고 내부적으로 지정해주겠다.
        // 제약, 섹션, memberSurvey는 설문이 만들어진 뒤에 생성되어 insert 된다.

        public Survey toEntity(Member author, SurveyStatus surveyStatus) {
            return Survey.builder()
                    .author(author)
                    .title(this.title)
                    .openDate(this.openDate)
                    .pointColor(this.pointColor)
                    .expireDate(this.expireDate)
                    .description(this.description)
                    .sImageURL(this.surveyImageUrl)
                    .surveyStatus(surveyStatus)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurveyInfoDTO {
        private String title;
        private String description;
        private Long author;
        private String surveyImageUrl;
        private String pointColor;
        private SurveyStatus surveyStatus;
        // 개봉, 마감일 포함할까 고민 중
    }
}
