package com.service.surveyservice.domain.question.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {
    @Id // Primary Key 지정
    @Column(name = "QuestionOption_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @Column // 몇 번 항목인지
    private Long index;

    @Column // 객관식 답변 내용
    private String answerChoiceText;

//    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "Question_ID") // 어떤 column과 연결이 될 지 설정
//    private Question questionId;
}

