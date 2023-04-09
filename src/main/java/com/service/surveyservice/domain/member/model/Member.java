package com.service.surveyservice.domain.member.model;

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
public class Member extends MemberBase {

    private String nickname;

    private String mImageURL;

    // 내가 참여한 설문 리스트(를 가져오기 위한 객체 리스트)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberSurvey> memberSurveys = new ArrayList<>();

    // 내가 만든 설문 리스트
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Survey> surveys = new ArrayList<>();


}

