package com.service.surveyservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor // 파라미터가 없는 생성자를 생성
@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
@ToString
@Entity
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

