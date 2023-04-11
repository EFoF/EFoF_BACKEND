package com.service.surveyservice.domain.member.model;

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
public class Member extends MemberBase {

    private String nickname;

    private String mImageURL;

    // 내가 참여한 설문 리스트(를 가져오기 위한 객체 리스트)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberSurvey> memberSurveys = new ArrayList<>();

    // 내가 만든 설문 리스트
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Survey> surveys = new ArrayList<>();

    @Builder
    public Member(Long id, String password, String email, String username, String organization, String organizationDetail, String nickname, String mImageURL) {
        super(id, password, email, username, organization, organizationDetail);
        this.nickname = nickname;
        this.mImageURL = "https://cdn-icons-png.flaticon.com/128/7178/7178514.png";
    }

}

