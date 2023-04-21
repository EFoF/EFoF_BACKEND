package com.service.surveyservice.domain.survey.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
