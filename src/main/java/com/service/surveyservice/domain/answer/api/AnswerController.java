package com.service.surveyservice.domain.answer.api;

import com.service.surveyservice.domain.answer.application.AnswerService;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.surveyservice.domain.answer.dto.AnswerDTO.*;

@Slf4j
@RestController
@RequestMapping("/form")
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

    /**
     *
     * @param participateAnswerListDTO
     * @return void
     * 설문 참여 응답 저장
     */
    @PostMapping(value = "/participate")
    public void formParticipate(@RequestBody ParticipateAnswerListDTO participateAnswerListDTO){
        log.info("데이터 들어옴");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        answerService.participateForm(participateAnswerListDTO, currentMemberId);
    }

}
