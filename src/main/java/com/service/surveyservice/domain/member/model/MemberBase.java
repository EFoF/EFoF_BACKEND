package com.service.surveyservice.domain.member.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase {
    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    private String password;

    private String email;
}

