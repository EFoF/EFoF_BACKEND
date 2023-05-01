package com.service.surveyservice.domain.question.application;

import com.service.surveyservice.domain.question.dao.QuestionCustomRepository;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final SectionRepository sectionRepository;
    private final QuestionCustomRepository questionCustomRepository;

    @Transactional
    public void createQuestion(SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto, Survey survey) {

        List<Section> sectionList = sectionRepository.findBySurveyId(survey.getId());

        questionCustomRepository.saveAll(sectionList, saveSurveyRequestDto);

    }
}
