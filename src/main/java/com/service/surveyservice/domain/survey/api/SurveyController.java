package com.service.surveyservice.domain.survey.api;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.survey.application.MemberSurveyService;
import com.service.surveyservice.domain.survey.application.SurveyService;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SurveyController {

    private final MemberService memberService;
    private final SurveyService surveyService;
    private final MemberSurveyService memberSurveyService;

    @PostMapping(value = "/form")
    public ResponseEntity<SurveyInfoDTO> createSurvey(@RequestBody CreateSurveyRequestDTO createSurveyRequestDTO) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        SurveyInfoDTO surveyInfoDTO = surveyService.createSurvey(createSurveyRequestDTO, currentMemberId);
        return new ResponseEntity<>(surveyInfoDTO, HttpStatus.CREATED);
    }

}
