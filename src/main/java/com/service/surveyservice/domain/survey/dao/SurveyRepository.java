package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long>, SurveyCustomRepository {
}
