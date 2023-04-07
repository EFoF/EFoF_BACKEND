package com.service.surveyservice.domain.constraint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor // 파라미터가 없는 생성자를 생성
@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
@ToString
@Entity
public class Location_Constraint {
    @Id // Primary Key 지정
    @Column(name = "Location_Constraint_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @Column
    private Float latitude; // 위도

    @Column
    private Float longitude; // 경도

    //    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "Constraint_ID") // 어떤 column과 연결이 될 지 설정
//    private Constraint constraintId;
}

