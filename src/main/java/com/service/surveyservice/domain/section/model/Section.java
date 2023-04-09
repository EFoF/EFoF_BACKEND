package com.service.surveyservice.domain.section.model;

import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @Column(name = "section_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @Column
    private int nextSectionId; // 다음 섹션

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

}

