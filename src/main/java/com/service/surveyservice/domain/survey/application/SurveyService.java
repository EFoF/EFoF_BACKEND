package com.service.surveyservice.domain.survey.application;

import com.service.surveyservice.domain.survey.dao.SurveyCustomRepositoryImpl;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyCustomRepositoryImpl surveyCustomRepository;


}
