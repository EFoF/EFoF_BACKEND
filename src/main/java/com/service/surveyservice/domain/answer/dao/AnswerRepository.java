package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long>{



}
