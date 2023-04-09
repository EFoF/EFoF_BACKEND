package com.service.surveyservice.domain.answer.model;

import com.service.surveyservice.domain.member.model.MemberSurvey;
import com.service.surveyservice.domain.model.BaseTimeEntity;
import com.service.surveyservice.domain.question.model.Question;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseTimeEntity {

    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @OneToOne
    @JoinColumn
    private Question question;

    @ManyToOne
    @JoinColumn(name = "memberSurvey_id")
    private MemberSurvey memberSurvey;

    private int multipleChoiceAnswer;

    private String essayQuestionAnswer;
}

