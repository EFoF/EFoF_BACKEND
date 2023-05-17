package com.service.surveyservice.domain.section.dto;


import lombok.*;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionQuestionQueryDto{
        private Long id;
        private Long nextSectionId;
        private String questionOrder;
        private List<QuestionQueryDto> questionList;


        public SectionQuestionQueryDto(Long id, Long nextSectionId, String questionOrder) {
            this.id = id;
            this.nextSectionId = nextSectionId;
            this.questionOrder = questionOrder;
        }

        public void setQuestionList(List<QuestionQueryDto> questionList) {
            this.questionList = questionList;
        }
    }

}
