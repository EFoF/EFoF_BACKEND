package com.service.surveyservice.domain.constraint.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

