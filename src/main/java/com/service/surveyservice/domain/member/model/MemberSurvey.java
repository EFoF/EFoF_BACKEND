package com.service.surveyservice.domain.member.model;

import com.service.surveyservice.domain.answer.model.Answer;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Column(name = "member_survey_id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "memberSurvey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();
}

