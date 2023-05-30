package com.service.surveyservice.domain.constraintoptions.dao;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import com.service.surveyservice.domain.constraintoptions.model.LocationConstraintOptions;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationConstraintRepository extends JpaRepository<LocationConstraintOptions, Long> {
    Optional<LocationConstraintOptions> findByConstraintOptions(ConstraintOptions constraintOptions);
}
