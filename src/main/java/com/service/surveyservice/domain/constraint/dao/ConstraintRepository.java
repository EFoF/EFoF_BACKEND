package com.service.surveyservice.domain.constraint.dao;

import com.service.surveyservice.domain.constraint.model.Constraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<Constraint, Long>, ConstraintCustomRepository {
}
