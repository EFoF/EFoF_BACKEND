package com.service.surveyservice.domain.member.dao;

import com.service.surveyservice.domain.member.dto.MemberDTO;

import java.util.Optional;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

public interface MemberCustomRepository {

    MemberDetail getMemberDetail(Long userId);
    Optional<MemberDetail> getMemberDetailOptional(Long userId);
    Optional<MemberTokenPublishConfirmDTO> getMemberTokenPublishConfirm(Long userId);
}
