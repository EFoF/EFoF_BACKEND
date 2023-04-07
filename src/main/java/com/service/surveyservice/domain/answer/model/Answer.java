package com.service.surveyservice.domain.answer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id // Primary Key 지정
    @Column(name = "Answer_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    //    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "Question_ID") // 어떤 column과 연결이 될 지 설정
//    private Question questionId;

    //    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "MemberSurvey_ID") // 어떤 column과 연결이 될 지 설정
//    private MemberSurvey memberSurveyId;

    //- 객관식 + 찬부식 답변
    //- 주관식 답변
    // 아직 안 넣음
}

