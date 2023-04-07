package com.service.surveyservice.domain.survey.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Id // Primary Key 지정
    @Column(name = "SURVEY_ID") // 컬럼 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 설정
    private Long id;

    @Column // 설문조사 제목
    private String title;

    @Column // 설문조사 설명
    private String description;

    @Column // 설문조사 헤더 이미지
    private String sImageURL;

    @Column // 템플릿 색상
    private String pointColor;

    @Column // ?
    private String questionOrder;

    @Column // ?
    private String surveyStatus;

    @Column // 설문 조사 시작 날짜
    private LocalDateTime openDate;

    @Column // 설문 조사 종료 날짜
    private LocalDateTime expireDate;
}
