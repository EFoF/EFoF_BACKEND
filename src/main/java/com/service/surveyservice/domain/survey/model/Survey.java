package com.service.surveyservice.domain.survey.model;

import com.service.surveyservice.domain.constraint.model.Constraint;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberSurvey;
import com.service.surveyservice.domain.section.model.Section;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Id
    @Column(name = "survey_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Section> sections = new ArrayList<>();

    // FetchType.EAGER 고민중
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Constraint> constraints = new ArrayList<>();

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberSurvey> memberSurveys = new ArrayList<>();

    private String title;

    private String description;

    private String sImageURL;

    private String pointColor;

    private String questionOrder;

    private String surveyStatus;

    private LocalDateTime openDate;

    private LocalDateTime expireDate;
}
