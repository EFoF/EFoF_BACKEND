package com.service.surveyservice.domain.answer.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;



    //- 객관식 + 찬부식 답변
    //- 주관식 답변
    // 아직 안 넣음
}

