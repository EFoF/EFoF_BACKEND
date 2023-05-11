package com.service.surveyservice.domain.member.model;

import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.service.surveyservice.domain.member.dto.MailDTO.*;
import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

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
    public Member(Long id, String password, String email, String username, String nickname, String mImageURL, MemberLoginType memberLoginType, Authority authority) {
        super(id, password, email, username, authority, memberLoginType);
        this.nickname = nickname;
        this.mImageURL = "https://cdn-icons-png.flaticon.com/128/7178/7178514.png";
    }

    public EncryptEmailDTO encryptEmailDTO() {
        String[] subEmail = this.getEmail().split("@");
        int asteriskNum = subEmail[0].length() - 3;
        String asterisks = "*".repeat(asteriskNum);
        subEmail[0] = subEmail[0].substring(0, 3) + asterisks;
        String returnEmail = subEmail[0] + "@" + subEmail[1];
        return new EncryptEmailDTO(returnEmail);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImg(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    // 비밀번호 찾기를 통해 자동 생성된 비밀번호로 임시 변경할 때 사용됨
    public void updateToGeneratedPasswordWithDTO(UpdateGeneratedPasswordRequestDTO updateGeneratedPasswordRequestDTO) {
        super.updatePassword(updateGeneratedPasswordRequestDTO.getGeneratedPassword());
    }

    // 사용자의 비밀번호 변경 요청을 수행할 때 사용됨
    public void updatePasswordWithDTO(UpdateMemberPasswordRequestDTO updateMemberPasswordRequestDTO) {
        super.updatePassword(updateMemberPasswordRequestDTO.getNewPassword());
    }

    // 사용자의 비밀번호 변경 요청을 수행할 때 사용됨 - 비 로그인 시
    public void updatePasswordVisitorWithDTO(UpdateMemberPasswordVisitorRequestDTO updateMemberPasswordVisitorRequestDTO) {
        super.updatePassword(updateMemberPasswordVisitorRequestDTO.getNewPassword());
    }


}

