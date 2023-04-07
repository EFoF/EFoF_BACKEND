package com.service.surveyservice.domain.question.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id // Primary Key 지정
    @Column(name = "Question_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @Column // 질문 유형(객관식, 객관식 중복, 주관식, OX)
    private String questionType;

    @Column // 질문 내용
    private String questionText;

//    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "SURVEY_ID") // 어떤 column과 연결이 될 지 설정
//    private Survey surveyId;
}

