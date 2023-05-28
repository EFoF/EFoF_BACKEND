package com.service.surveyservice.domain.survey.model;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.model.BaseTimeEntity;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Survey extends BaseTimeEntity {
    @Id
    @Column(name = "survey_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

//    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<Section> section;

    // FetchType.EAGER 고민중
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ConstraintOptions> constraintOptions = new HashSet<>();

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberSurvey> memberSurveys = new ArrayList<>();

    private String title;

    private String description;

    private String sImageURL;

    private String fontColor;

    private String bgColor;

    private String btColor;

    @Enumerated(EnumType.STRING)
    private ReleaseStatus releaseStatus;

    private LocalDateTime openDate;

    private LocalDateTime expireDate;

    @Builder
    public Survey(Long id, Member author, List<Section> section, Set<ConstraintOptions> constraintOptions, List<MemberSurvey> memberSurveys, String title, String description, String sImageURL, String fontColor, String bgColor, String btColor, SurveyStatus surveyStatus, LocalDateTime openDate, LocalDateTime expireDate) {

        this.id = id;
        this.author = author;
        this.section = section;
        this.constraintOptions = constraintOptions;
        this.memberSurveys = memberSurveys;
        this.title = title;
        this.description = description;
        this.sImageURL = sImageURL;
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.btColor = btColor;
        this.releaseStatus = releaseStatus;
        this.openDate = openDate;
        this.expireDate = expireDate;
    }

    public void setImageURL(@Nullable String sImageURL) {
        this.sImageURL = sImageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void updateColor(SurveyDTO.UpdateSurveyColorDto updateSurveyColorDto) {
        this.fontColor = updateSurveyColorDto.getFontColor();
        this.bgColor = updateSurveyColorDto.getBgColor();
        this.btColor = updateSurveyColorDto.getBtColor();
    }

    public void updateOpenDate(SurveyDTO.UpdateSurveyDateDto updateSurveyDateDto) {
        this.openDate = updateSurveyDateDto.getOpenDate();
    }

    public void updateExpireDate(SurveyDTO.UpdateSurveyDateDto updateSurveyDateDto) {
        this.expireDate = updateSurveyDateDto.getExpireDate();
    }
}
