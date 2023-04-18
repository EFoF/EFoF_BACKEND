package com.service.surveyservice.domain.survey.model;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.member.model.MemberSurvey;
import com.service.surveyservice.domain.model.BaseTimeEntity;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Survey extends BaseTimeEntity {
    @Id
    @Column(name = "survey_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

//    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Section> sections = new ArrayList<>();

    @OneToOne(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Section section;

    // FetchType.EAGER 고민중
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ConstraintOptions> constraintOptions = new ArrayList<>();

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberSurvey> memberSurveys = new ArrayList<>();

    private String title;

    private String description;

    private String sImageURL;

    private String pointColor;

    @Enumerated(EnumType.STRING)
    private SurveyStatus surveyStatus;

    private LocalDateTime openDate;

    private LocalDateTime expireDate;

    @Builder
    public Survey(Long id, Member author, String title, String description, String sImageURL, String pointColor, SurveyStatus surveyStatus, LocalDateTime openDate, LocalDateTime expireDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.openDate = openDate;
        this.sImageURL = sImageURL;
        this.expireDate = expireDate;
        this.pointColor = pointColor;
        this.description = description;
        this.surveyStatus = surveyStatus;
    }

}
