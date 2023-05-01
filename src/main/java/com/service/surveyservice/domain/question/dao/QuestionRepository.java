package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>{
}
