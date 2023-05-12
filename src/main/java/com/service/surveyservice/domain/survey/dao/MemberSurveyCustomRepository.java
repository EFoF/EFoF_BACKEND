package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.*;

public interface MemberSurveyCustomRepository {

    Page<MemberSurveyInfoDTO> findByMemberIdWithPage(Long id, Pageable pageable);


}
