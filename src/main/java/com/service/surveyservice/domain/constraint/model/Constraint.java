package com.service.surveyservice.domain.constraint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Constraint {
    @Id // Primary Key 지정
    @Column(name = "Constraint_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    //    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "Survey_ID") // 어떤 column과 연결이 될 지 설정
//    private Survey surveyId;

    @Column
    private String constraintType; //통계보기 허용, 수정 허용, GPS , PASSWORD , EMAIL, 익명

    @Column
    private String ConstraintValue; //GPS 나 PASSWORD, EMAIL 의 경우 값이 필요함
}

