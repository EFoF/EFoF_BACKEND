package com.service.surveyservice.domain.answer.model;

import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.model.BaseTimeEntity;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberSurvey_id")
    private MemberSurvey memberSurvey;

    // 객관식 답변
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionChoice_id")
    private QuestionOption questionOption;

    // 주관식 답변
    private String answerSentence;
}

