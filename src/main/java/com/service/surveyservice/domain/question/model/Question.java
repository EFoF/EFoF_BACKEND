package com.service.surveyservice.domain.question.model;

import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.section.model.Section;
import lombok.*;

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

    private boolean isNecessary;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<QuestionOption> questionOptions = new ArrayList<>();

    // question에서는 answer를 알 필요가 없어보여서 일단 단방향으로 설정하겠다.

    public void updateQuestion(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto){
        this.questionText = saveQuestionRequestDto.getQuestionContent();
        this.questionType = QuestionType.fromId(saveQuestionRequestDto.getType());
        this.isNecessary = saveQuestionRequestDto.isNecessary();
    }
}

