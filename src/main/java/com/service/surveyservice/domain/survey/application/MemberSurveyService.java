package com.service.surveyservice.domain.survey.application;

import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSurveyService {
    private final MemberSurveyRepository memberSurveyRepository;


    @Transactional(readOnly = true)
    public Page<MemberSurveyDTO.GetParticipateSurveyDTO> findMemberSurveyByMemberId(Long memberId, Pageable pageable) {
        Page<MemberSurveyRepository.GetSurveyInterface> participateSurveyById = memberSurveyRepository.findMemberSurveyByMemberId(memberId, pageable);
        List<MemberSurveyDTO.GetParticipateSurveyDTO> collect = participateSurveyById.stream().map(MemberSurveyDTO.GetParticipateSurveyDTO::new).collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, participateSurveyById.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<MemberSurveyDTO.GetParticipateSurveyDTO> findMemberSurveyByMemberIdWithStatus(Long authorId, String SurveyStatus, Pageable pageable) {
        Page<MemberSurveyRepository.GetSurveyInterface> participateSurveyById;
        if(SurveyStatus.equals("progress")){
            participateSurveyById = memberSurveyRepository.findMemberSurveyByMemberIdPro(authorId, pageable);
        } else if(SurveyStatus.equals("over")){
            participateSurveyById = memberSurveyRepository.findMemberSurveyByMemberIdOver(authorId, pageable);
        } else{
            participateSurveyById = memberSurveyRepository.findMemberSurveyByMemberId(authorId, pageable);
        }
        List<MemberSurveyDTO.GetParticipateSurveyDTO> collect = participateSurveyById.stream().map(MemberSurveyDTO.GetParticipateSurveyDTO::new).collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, participateSurveyById.getTotalElements());
    }
}
