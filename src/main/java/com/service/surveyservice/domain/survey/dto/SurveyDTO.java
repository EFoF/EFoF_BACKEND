package com.service.surveyservice.domain.survey.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.service.surveyservice.domain.section.dto.SectionDTO.*;

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
//                    .pointColor(this.pointColor)
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
    public static class SurveyInfoDTO {
        private String title;
        private String description;
        private Long author;
        private String surveyImageUrl;
        //        private String pointColor;
        private SurveyStatus surveyStatus;
        // 개봉, 마감일 포함할까 고민 중

        @QueryProjection
        public SurveyInfoDTO(String title, String description, Long author, String surveyImageUrl, SurveyStatus surveyStatus) {
            this.title = title;
            this.description = description;
            this.author = author;
            this.surveyImageUrl = surveyImageUrl;
//            this.pointColor = pointColor;
            this.surveyStatus = surveyStatus;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveSurveyRequestDto {
        private String title;
        private String detail;
        private String image;
        private String fontColor;
        private String bgColor;
        private String btColor;
        private List<SaveSectionRequestDto> sections;

        public Survey toEntity(Member author, SurveyStatus surveyStatus) {
            return Survey.builder()
                    .author(author)
                    .description(this.detail)
                    .sImageURL(image)
                    .surveyStatus(surveyStatus)
                    .bgColor(this.bgColor)
                    .fontColor(this.fontColor)
                    .btColor(this.btColor)
                    .title(this.title).build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurveySectionQueryDTO {
        private Long id;
        private String title;
        private String description;
        private String sImageURL;

        private String fontColor;

        private String bgColor;

        private String btColor;
        private LocalDateTime openDate;

        private LocalDateTime expireDate;

        private List<SectionQuestionQueryDto> sectionList;

        public SurveySectionQueryDTO(Long id, String title, String description, String sImageURL, String fontColor, String bgColor, String btColor, LocalDateTime openDate, LocalDateTime expireDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.sImageURL = sImageURL;
            this.fontColor = fontColor;
            this.bgColor = bgColor;
            this.btColor = btColor;
            this.openDate = openDate;
            this.expireDate = expireDate;
        }

        public void setSectionList(List<SectionQuestionQueryDto> sectionList) {
            this.sectionList = sectionList;
        }
    }


}
