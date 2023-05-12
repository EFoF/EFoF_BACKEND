package com.service.surveyservice.domain.answer.api;

import com.service.surveyservice.domain.answer.application.AnswerService;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping(value = "/{survey_id}/statistics")
    public ResponseEntity<AnswerDTO.SurveyForStatisticResponseDto> getSurveyForStatistic(
            @PathVariable Long survey_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        AnswerDTO.SurveyForStatisticResponseDto surveyForStatistic =
                answerService.getSurveyForStatistic(survey_id, currentMemberId);

        return new ResponseEntity<>(surveyForStatistic,HttpStatus.CREATED);
    }


}
