package com.service.surveyservice.domain.question.model;

import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.dto.QuestionOptionDTO;
import com.service.surveyservice.domain.section.model.Section;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class QuestionOption {

    @Id
    @Column(name = "questionChoice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nextSection_id")
    private Section nextSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

//    @OneToOne(cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    @Nullable
//    @JoinColumn(name = "questionOptionImg_id")
    private String questionOptionImg;


//    public void setQuestionOptionImg(@Nullable QuestionOptionImg questionOptionImg) {
//        this.questionOptionImg = questionOptionImg;
//    }


    public void setQuestionOptionText(String optionText) {
        this.optionText = optionText;
    }

    public void setQuestionOptionNextSection(Section nextSection) {
        this.nextSection = nextSection;
    }


    public void setQuestionOptionImage(String image) {
        this.questionOptionImg = image;
    }

    public QuestionOptionDTO.ResponseSaveQuestionOptionDto toResponseDto(){
        return QuestionOptionDTO.ResponseSaveQuestionOptionDto.builder()
                .option(this.getOptionText())
                .id(this.getId())
                .imageUrl(this.getQuestionOptionImg())
                .nextSectionId(this.getNextSection().getId())
                .build();
    }

    public static List<QuestionOptionDTO.ResponseSaveQuestionOptionDto> toResponseDtoList(List<QuestionOption> dtoList) {
        return dtoList.stream()
                .map(dto -> dto.toResponseDto())
                .collect(Collectors.toList());
    }

}

