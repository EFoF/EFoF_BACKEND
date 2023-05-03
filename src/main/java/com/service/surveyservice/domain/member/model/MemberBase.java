package com.service.surveyservice.domain.member.model;

import com.service.surveyservice.domain.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    private String password;

    private String email;

    private String userName;

    @Enumerated(EnumType.STRING)
    protected Authority authority;

    @Enumerated(EnumType.STRING)
    protected MemberLoginType memberLoginType;

    public void updatePassword(String pw) {
        this.password = pw;
    }
}

