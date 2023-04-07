package com.service.surveyservice.domain.section.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id // Primary Key 지정
    @Column(name = "Section_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @Column
    private int nextSectionId; // 다음 섹션

    //    여기 조인 / 관계 설정
//    @ManyToOne
//    @JoinColumn(name = "Survey_ID") // 어떤 column과 연결이 될 지 설정
//    private Survey surveyId;
}

