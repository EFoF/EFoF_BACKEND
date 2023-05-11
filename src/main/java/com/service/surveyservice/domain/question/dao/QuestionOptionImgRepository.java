package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.question.model.QuestionOptionImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionImgRepository extends JpaRepository<QuestionOptionImg, Long> {
}
