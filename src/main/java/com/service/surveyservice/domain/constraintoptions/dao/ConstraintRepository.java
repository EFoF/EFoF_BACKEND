package com.service.surveyservice.domain.constraintoptions.dao;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstraintRepository extends JpaRepository<ConstraintOptions, Long> {
    List<ConstraintOptions> findBySurvey(Survey survey);
}
