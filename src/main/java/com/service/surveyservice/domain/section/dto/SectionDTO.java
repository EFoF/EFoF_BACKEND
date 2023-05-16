package com.service.surveyservice.domain.section.dto;


import com.service.surveyservice.domain.question.dto.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.service.surveyservice.domain.question.dto.QuestionDTO.*;

public class SectionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveSectionRequestDto{
        private String id;
        private String nextSectionId;
        private List<SaveSurveyQuestionRequestDto> questionList;

    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSectionOrderRequestDto{
        private Long startSectionId;
        private int startSectionIdx;
        private Long endSectionId;
        private int endSectionIdx;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSectionOrderResponseDto{
        private Long questionSectionId;
        private String startSectionOrder;
        private String endSectionOrder;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createSectionResponseDto{
        private Long id;
        private Long nextSectionId;
        private List<createSectionResponseQuestionDto> questionList;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateSectionDto{
        private Long nextSectionId;

    }


}
