package com.service.surveyservice.domain.section.model;

import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @Column(name = "section_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @ManyToOne
    private Section parentSection;

    @OneToMany(mappedBy = "parentSection", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Section> child = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Question> questions = new ArrayList<>();

    // Section에서 QuestionOption을 가지고 있을 필요성이 크게 느껴지지 않아 단방향 연관관계로 설정하였다.
//    @OneToOne(mappedBy = "nextSection", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private QuestionOption questionOption;


}

