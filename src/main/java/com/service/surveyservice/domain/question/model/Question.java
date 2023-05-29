package com.service.surveyservice.domain.question.model;

import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.section.model.Section;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class Question {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private QuestionType questionType;

    private String questionText;

    private Boolean isNecessary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    private Boolean hasImage;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,orphanRemoval = true)
    @BatchSize(size = 10)
    private List<QuestionOption> questionOptions = new ArrayList<>();

    // question에서는 answer를 알 필요가 없어보여서 일단 단방향으로 설정하겠다.

    public void updateQuestionText(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto){
        this.questionText = saveQuestionRequestDto.getQuestionContent();
    }

    public void updateQuestionType(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto){
        this.questionType = QuestionType.fromId(saveQuestionRequestDto.getType());
    }
    public void updateQuestionIsNecessary(){
        this.isNecessary = !this.isNecessary;
    }

    public void updateSection(Section section){
        this.section = section;
    }

    public QuestionDTO.ResponseSaveQuestionDto toResponseDto(){
        return QuestionDTO.ResponseSaveQuestionDto
                .builder()
                .id(this.getId())
                .section_id(this.section.getId())
                .isNecessary(this.getIsNecessary())
                .questionContent(this.getQuestionText())
                .type(this.getQuestionType())
                .build();
    }

    public QuestionDTO.createSectionResponseQuestionDto toCreateSectionResponseDto(){
        return QuestionDTO.createSectionResponseQuestionDto
                .builder()
                .questionContent(this.getQuestionText())
                .id(this.getId())
                .isNecessary(this.getIsNecessary())
                .type(this.getQuestionType().getId())
                .build();
    }
}

