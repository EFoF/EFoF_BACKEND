package com.service.surveyservice.domain.constraintoptions.dao;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstraintRepository extends JpaRepository<ConstraintOptions, Long> {
    List<ConstraintOptions> findBySurvey(Survey survey);

    @Query(value = "select co from ConstraintOptions co where co.survey.id = :survey_id")
    List<ConstraintOptions> findBySurveyId(@Param("survey_id")Long survey_id);

    @Modifying
    @Query(value = "delete FROM ConstraintOptions co WHERE co.survey.id = :survey_id and co.constraintType = :constraintType" )
    void deleteByType(@Param("survey_id")Long survey_id, @Param("constraintType")ConstraintType constraintType);


    @Query(value = "select co FROM ConstraintOptions co WHERE co.survey.id = :survey_id and co.constraintType = :constraintType" )
    ConstraintOptions findBySurveyId(@Param("survey_id")Long survey_id, @Param("constraintType")ConstraintType constraintType);
}
