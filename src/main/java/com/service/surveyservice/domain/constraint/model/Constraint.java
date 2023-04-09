package com.service.surveyservice.domain.constraint.model;

import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Constraint {
    @Id
    @Column(name = "constraint_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToOne(mappedBy = "constraint", cascade = CascadeType.REMOVE)
    private LocationConstraint locationConstraint;

    @Column
    private String constraintType; //통계보기 허용, 수정 허용, GPS , PASSWORD , EMAIL, 익명

    @Column
    private String ConstraintValue; //GPS 나 PASSWORD, EMAIL 의 경우 값이 필요함
}

