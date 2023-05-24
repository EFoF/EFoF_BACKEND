package com.service.surveyservice.domain.answer.model;

import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.model.BaseTimeEntity;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class Answer {

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

    @CreatedDate // Entity가 생성되어 저장될 때 시간이 자동 저장
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Builder
    public Answer(Question question, MemberSurvey memberSurvey, QuestionOption questionOption, String answerSentence) {
        this.question = question;
        this.memberSurvey = memberSurvey;
        this.questionOption = questionOption;
        this.answerSentence = answerSentence;
    }
}

