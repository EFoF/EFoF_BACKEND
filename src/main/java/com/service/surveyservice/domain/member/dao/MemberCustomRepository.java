package com.service.surveyservice.domain.member.dao;

import com.service.surveyservice.domain.member.dto.MemberDTO;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

public interface MemberCustomRepository {

    MemberDetail getMemberDetail(Long userId);
}
