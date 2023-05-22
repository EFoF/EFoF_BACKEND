package com.service.surveyservice.domain.answer.api;

import com.amazonaws.Response;
import com.service.surveyservice.domain.answer.application.AnswerService;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.section.application.SectionService;
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
    private final SectionService sectionService;

    @GetMapping(value = "/{survey_id}/statistics")
    public ResponseEntity<AnswerDTO.SurveyForStatisticResponseDto> getSurveyForStatistic(
            @PathVariable Long survey_id) {
        log.info("확인용");

        // 현재 로그인한 사람의 member id를 받는 변수
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        // survey에 대한 정보를 SurveyForStatisticResponseDto 형태로 받아옴
        AnswerDTO.SurveyForStatisticResponseDto surveyForStatistic =
                answerService.getSurveyForStatistic(survey_id, currentMemberId);

        return new ResponseEntity<>(surveyForStatistic,HttpStatus.CREATED);
    }

//    @GetMapping(value = "{survey_id}/statistics/{section_id}")
//    public ResponseEntity<AnswerDTO.SectionForStatisticResponseDto> getSectionForStatistic(
//            @PathVariable Long section_id) {
//        log.info("section 정보 확인용");
//
//        AnswerDTO.SectionForStatisticResponseDto sectionForStatistic =
//                answerService.getSectionForStatistic(section_id);
//
//    }

}
