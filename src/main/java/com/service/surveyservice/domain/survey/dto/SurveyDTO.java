package com.service.surveyservice.domain.survey.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.model.ReleaseStatus;
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

        public Survey toEntity(Member author, ReleaseStatus releaseStatus) {
            return Survey.builder()
                    .author(author)
                    .title(this.title)
                    .openDate(this.openDate)
//                    .pointColor(this.pointColor)
                    .expireDate(this.expireDate)
                    .description(this.description)
                    .sImageURL(this.surveyImageUrl)
                    .releaseStatus(releaseStatus)
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
        private ReleaseStatus releaseStatus;
        // 개봉, 마감일 포함할까 고민 중

        @QueryProjection
        public SurveyInfoDTO(String title, String description, Long author, String surveyImageUrl, ReleaseStatus releaseStatus) {
            this.title = title;
            this.description = description;
            this.author = author;
            this.surveyImageUrl = surveyImageUrl;
//            this.pointColor = pointColor;
            this.releaseStatus = releaseStatus;
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

        public Survey toEntity(Member author, ReleaseStatus releaseStatus) {
            return Survey.builder()
                    .author(author)
                    .description(this.detail)
                    .sImageURL(image)
                    .releaseStatus(releaseStatus)
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
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSurveyTextDto {
        private String title;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSurveyColorDto {
        private String fontColor;
        private String bgColor;
        private String btColor;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSurveyDateDto {
        private LocalDateTime openDate;
        private LocalDateTime expireDate;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class GetGenerateSurveyDTO {
        private Long survey_id;
        private String title;
        private String description;
        private Long member_id;
        private String s_imageurl;
        private LocalDateTime open_date;
        private LocalDateTime expire_date;
        private String surveyStatus;

        @QueryProjection
        public GetGenerateSurveyDTO(SurveyRepository.GetSurveyInterface getGenerateSurveyInterface) {
            this.survey_id = getGenerateSurveyInterface.getSurvey_id();
            this.title = getGenerateSurveyInterface.getTitle();
            this.description = getGenerateSurveyInterface.getDescription();
            this.member_id = getGenerateSurveyInterface.getMember_id();
            this.s_imageurl = getGenerateSurveyInterface.getS_imageurl();
            this.open_date = getGenerateSurveyInterface.getOpen_date();
            this.expire_date = getGenerateSurveyInterface.getExpire_date();
            if (LocalDateTime.now().isBefore(open_date)) {
                this.surveyStatus = SurveyStatus.PRE_RELEASE.getName();
            }
            else if(LocalDateTime.now().isAfter(expire_date)) {
                this.surveyStatus = SurveyStatus.OVER.getName();
            }
            else {
                this.surveyStatus = SurveyStatus.PROGRESS.getName();
            }
        }
    }

}
