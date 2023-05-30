package com.service.surveyservice.domain.survey.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
import lombok.*;

import java.time.LocalDateTime;

public class MemberSurveyDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    public static class MemberSurveyInfoDTO {
        private Long id;
        private Long authorId;
        private Long surveyId;

        @QueryProjection
        public MemberSurveyInfoDTO (Long id, Long authorId, Long surveyId) {
            this.id = id;
            this.authorId = authorId;
            this.surveyId = surveyId;
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class GetParticipateSurveyDTO {
        private Long survey_id;
        private String title;
        private String description;
        private Long member_id;
        private String s_imageurl;
        private LocalDateTime open_date;
        private LocalDateTime expire_date;
        private String surveyStatus;

        @QueryProjection
        public GetParticipateSurveyDTO(MemberSurveyRepository.GetSurveyInterface getParticipateSurveyInterface) {
            this.survey_id = getParticipateSurveyInterface.getSurvey_id();
            this.title = getParticipateSurveyInterface.getTitle();
            this.description = getParticipateSurveyInterface.getDescription();
            this.member_id = getParticipateSurveyInterface.getMember_id();
            this.s_imageurl = getParticipateSurveyInterface.getS_imageurl();
            this.open_date = getParticipateSurveyInterface.getOpen_date();
            this.expire_date = getParticipateSurveyInterface.getExpire_date();
            if(LocalDateTime.now().isAfter(open_date) && LocalDateTime.now().isBefore(expire_date)) {
                this.surveyStatus = SurveyStatus.PROGRESS.getName();
            } else if (LocalDateTime.now().isAfter(expire_date)) {
                this.surveyStatus = SurveyStatus.OVER.getName();
            }
        }
    }
}
