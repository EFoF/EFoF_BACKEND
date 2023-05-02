package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionRepository  extends JpaRepository<QuestionOption, Long> {
}
