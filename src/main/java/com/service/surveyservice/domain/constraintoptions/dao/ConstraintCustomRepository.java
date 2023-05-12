package com.service.surveyservice.domain.constraintoptions.dao;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ConstraintCustomRepository {

    List<ConstraintOptions> findConstraintBySurveyId(Long surveyId);
}
