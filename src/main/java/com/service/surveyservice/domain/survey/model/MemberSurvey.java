package com.service.surveyservice.domain.survey.model;

import com.service.surveyservice.domain.answer.model.Answer;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberSurvey {
    @Id
    @Column(name = "memberSurvey_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "memberSurvey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public MemberSurvey(Member member, Survey survey) {
        this.member = member;
        this.survey = survey;
    }
}

