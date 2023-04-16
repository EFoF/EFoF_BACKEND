package com.service.surveyservice.domain.survey.application;

import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.NotMatchingCurrentMemberAndRequesterException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.dao.SurveyCustomRepositoryImpl;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.exception.ExpireBeforeOpenException;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyCustomRepositoryImpl surveyCustomRepository;

    public SurveyInfoDTO createSurvey(CreateSurveyRequestDTO createSurveyRequestDTO, long currentMemberId) {
        LocalDateTime openDate = createSurveyRequestDTO.getOpenDate();
        LocalDateTime expireDate = createSurveyRequestDTO.getExpireDate();
        SurveyStatus surveyStatus = SurveyStatus.PRE_RELEASE;

        // 요청으로 넘어온 사용자가 존재하는지 확인
        Member member = memberRepository.findById(createSurveyRequestDTO.getAuthor()).orElseThrow(() -> new NotFoundByIdException("사용자"));

        if(member.getId() != currentMemberId) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }

        if(expireDate.isEqual(openDate)) {
            throw new ExpireBeforeOpenException("마감 기간과 설문 시작 기간은 같을 수 없습니다.");
        }
        if(expireDate.isBefore(openDate)) {
            throw new ExpireBeforeOpenException();
        }

        // 오픈 시간이 현재이거나 현재보다 과거라면 suveyStatus를 진행중으로 바꾼다.
        // 오픈 시간을 과거로 설정할 수 있게 할지는 고민중이다.
        if(LocalDateTime.now().isAfter(openDate)) {
            surveyStatus = SurveyStatus.IN_PROGRESS;
        }

        // 설문조사 저장
        Survey survey = createSurveyRequestDTO.toEntity(member, surveyStatus);
        surveyRepository.save(survey);

        return SurveyInfoDTO.builder()
                .surveyImageUrl(createSurveyRequestDTO.getSurveyImageUrl())
                .description(createSurveyRequestDTO.getDescription())
//                .pointColor(createSurveyRequestDTO.getPointColor())
                .title(createSurveyRequestDTO.getTitle())
                .surveyStatus(surveyStatus)
                .author(member.getId())
                .build();

    }

    public Page<SurveyInfoDTO> getAuthorSurveyList(Long memberId, Long currentMemberId, Pageable pageable) {
        // 아이디 검증
        if(!memberId.equals(currentMemberId)) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }
        Page<SurveyInfoDTO> surveyInfoDTOPage = surveyCustomRepository.findSurveyInfoDTOByAuthorId(memberId, pageable);
        List<SurveyInfoDTO> resultList = new ArrayList<>();
//        return new PageImpl<>(resultList, pageable, surveyInfoDTOPage.getTotalElements());
        return surveyInfoDTOPage;
    }
}
