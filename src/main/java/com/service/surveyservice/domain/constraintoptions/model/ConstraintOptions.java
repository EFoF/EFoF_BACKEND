package com.service.surveyservice.domain.constraintoptions.model;

import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintOptions {
    @Id
    @Column(name = "constraintOptions_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToOne(mappedBy = "constraintOptions", cascade = CascadeType.REMOVE)
    private LocationConstraintOptions locationConstraintOptions;

    @Column
    private String constraintType; //통계보기 허용, 수정 허용, GPS , PASSWORD , EMAIL, 익명

    @Column
    private String ConstraintValue; //GPS 나 PASSWORD, EMAIL 의 경우 값이 필요함
}

