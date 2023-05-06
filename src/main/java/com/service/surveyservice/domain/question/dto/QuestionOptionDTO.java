package com.service.surveyservice.domain.question.dto;


import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.section.model.Section;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionOptionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionOptionRequestDTOInit {
        private String id;
        private String option;
        private String image;
        private String nextSectionId;

        public QuestionOption toQuestionOptionEntity(Section nextSection,Question question){
            return QuestionOption.builder()
                    .nextSection(nextSection)
                    .questionOptionImg(this.image)
                    .optionText(this.option)
                    .question(question)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionOptionTextRequestDTO {
        private String optionText;

        public QuestionOption toEntity(Question question){
            return QuestionOption.builder()
                    .question(question)
                    .optionText(this.optionText).build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionOptionNextSectionRequestDTO {
        private Long nextSection_id;

    }
}
