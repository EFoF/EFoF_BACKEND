package com.service.surveyservice.domain.constraintoptions.model;

import com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"survey_id", "constraintType"})})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintOptions {
    @Id
    @Column(name = "constraintOptions_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToOne(mappedBy = "constraintOptions", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @BatchSize(size = 10)
    private LocationConstraintOptions locationConstraintOptions;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ConstraintType constraintType; //통계보기 허용, 수정 허용, GPS , PASSWORD , EMAIL, 익명, 로그인 여부

    @Column
    private String constraintValue; //GPS 나 PASSWORD, EMAIL 의 경우 값이 필요함


    public ConstraintDTO.SurveyForStatisticConstraintResponseDto toResponseDto() {

        return ConstraintDTO.SurveyForStatisticConstraintResponseDto.builder()
                .id(this.getId())
                .constraintType(this.getConstraintType())
                .ConstraintValue(this.getConstraintValue())
                .build();
    }
    public static List<ConstraintDTO.SurveyForStatisticConstraintResponseDto> toEntities(List<ConstraintOptions> constraintOptionsList) {
        return constraintOptionsList.stream()
                .map(dto -> dto.toResponseDto())
                .collect(Collectors.toList());
    }

    public void updateConstraintValue(String constraintValue) {
        this.constraintValue = constraintValue;
    }
}

