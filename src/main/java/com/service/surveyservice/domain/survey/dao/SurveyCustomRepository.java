package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

public interface SurveyCustomRepository {

    Page<SurveyInfoDTO> findSurveyInfoDTOByAuthorId(Long authorId, Pageable pageable);
}
