package com.service.surveyservice.domain.constraintoptions.dto;


import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

public class ConstraintDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurveyForStatisticConstraintResponseDto {
        private Long id; //제약 조건 id
        private ConstraintType constraintType; //통계보기 허용, 수정 허용, GPS , PASSWORD , EMAIL, 익명

        private String ConstraintValue; //GPS 나 PASSWORD, EMAIL 의 경우 값이 필요함
    }
}
