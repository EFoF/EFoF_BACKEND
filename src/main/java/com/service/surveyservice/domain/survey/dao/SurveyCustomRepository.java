package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

public interface SurveyCustomRepository {
    Page<SurveyInfoDTO> findSurveyInfoDTOByAuthorId(Long authorId, Pageable pageable);


    SurveySectionQueryDTO findSurveyBySurveyId(Long survey_id);
}
