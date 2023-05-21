package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;

import java.util.List;

public interface QuestionOptionCustomRepository {
    void saveAll(List<String> optionList, Long question_id);
}
