package com.service.surveyservice.domain.question.dto;


import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.question.model.QuestionOptionImg;
import com.service.surveyservice.domain.section.model.Section;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionOptionDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveQuestionOptionRequestDTO{
        private String id;
        private String option;
        private String image;
        private String nextSectionId;

        public QuestionOption toQuestionOptionEntity(Section nextSection,Question question){
            return QuestionOption.builder()
                    .nextSection(nextSection)
                    .questionOptionImg(toQuestionOptionImgEntity())
                    .optionText(this.option)
                    .question(question)
                    .build();
        }
        public QuestionOptionImg toQuestionOptionImgEntity(){
            return QuestionOptionImg.builder()
                    .imgUrl(image)
                    .build();
        }
    }
}
