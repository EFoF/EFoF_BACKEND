package com.service.surveyservice.domain.section.model;

import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Section {
    @Id
    @Column(name = "section_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Section parentSection;

    @OneToMany(mappedBy = "parentSection", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Section> child = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    // 기존에 섹션에 있던 질문 순서 필드.
    private String questionOrder;

    // Section에서 QuestionOption을 가지고 있을 필요성이 크게 느껴지지 않아 단방향 연관관계로 설정하였다.
//    @OneToOne(mappedBy = "nextSection", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private QuestionOption questionOption;


    @Builder
    public Section(Long id, Section parentSection, List<Section> child, Survey survey, List<Question> questions, String questionOrder) {
        this.id = id;
        this.parentSection = parentSection;
        this.child = child;
        this.survey = survey;
        this.questions = questions;
        this.questionOrder = questionOrder;
    }

    public void setParentSection(Section parentSection){
        this.parentSection = parentSection;
    }
    public void setQuestionOrder(@Nullable String questionOrder){
        this.questionOrder = questionOrder;
    }
}

