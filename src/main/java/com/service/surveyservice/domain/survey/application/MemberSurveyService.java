package com.service.surveyservice.domain.survey.application;

import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.NotMatchingCurrentMemberAndRequesterException;
import com.service.surveyservice.domain.survey.dao.MemberSurveyCustomRepositoryImpl;
import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dao.SurveyCustomRepositoryImpl;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSurveyService {
    private final MemberSurveyCustomRepositoryImpl memberSurveyCustomRepository;
    private final MemberCustomRepositoryImpl memberCustomRepository;
    private final SurveyCustomRepositoryImpl surveyCustomRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;

    /*
        두가지로 구현 : 페이지네이션이 적용된 로직과 적용되지 않은 로직
        페이지네이션을 위해서 JSON에 추가되는 정보들이 꽤 많아서, 필요에 따라 페이지네이션 데이터 없이
        필요한 데이터만 사용할 수 있는 옵션을 제공한다.
     */
    // 철웅오빠 - 참여?
    // 페이지네이션을 지원하는 참여 설문 조회
    public Page<MemberSurveyInfoDTO> getInfoPagination(Long requesterId, Long currentMemberId,Pageable pageable) {
        if(!requesterId.equals(currentMemberId)) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }
        return memberSurveyCustomRepository.findByMemberIdWithPage(currentMemberId, pageable);
    }

    public List<MemberSurveyInfoDTO> getInfoAll(Long requesterId, Long currentMemberId) {
        if(!requesterId.equals(currentMemberId)) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }
        return memberSurveyRepository.findByMemberId(currentMemberId);
    }
}
