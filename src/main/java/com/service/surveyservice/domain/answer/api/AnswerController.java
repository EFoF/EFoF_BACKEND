package com.service.surveyservice.domain.answer.api;

import com.amazonaws.Response;
import com.service.surveyservice.domain.answer.application.AnswerService;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.question.application.QuestionService;
import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.section.application.SectionService;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.util.CookieUtil;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.service.surveyservice.domain.answer.dto.AnswerDTO.*;
import static com.service.surveyservice.global.common.constants.JwtConstants.ACCESS_TOKEN;

//import static jdk.internal.org.jline.reader.impl.LineReaderImpl.CompletionType.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping(value = "/survey/{survey_id}/statistics")
    public ResponseEntity<AnswerDTO.SurveyForStatisticResponseDto> getSurveyForStatistic(
            @PathVariable Long survey_id) {
        log.info("getSurveyForStatistic 확인용");

        // 현재 로그인한 사람의 member id를 받는 변수
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        // survey에 대한 정보를 SurveyForStatisticResponseDto 형태로 받아옴
        AnswerDTO.SurveyForStatisticResponseDto surveyForStatistic =
                answerService.getSurveyForStatistic(survey_id, currentMemberId);

        return new ResponseEntity<>(surveyForStatistic,HttpStatus.CREATED);
    }

    @GetMapping(value = "/survey/{survey_id}/statistics/{section_id}")
    public ResponseEntity<List<QuestionDTO.QuestionInfoByIdDto>> getQuestionBySectionForStatistic(
            @PathVariable Long survey_id, @PathVariable Long section_id) {
        log.info("getQuestionBySectionForStatistic 정보 확인용");

//        AnswerDTO.QuestionBySectionForStatisticResponseDto answerForQuestion =
//                answerService.getQuestionBySectionForStatistic(survey_id, section_id);
//        AnswerDTO.QuestionBySectionForStatisticResponseDto answerForQuestion =
//                (QuestionBySectionForStatisticResponseDto) answerService.getQuestionBySectionForStatistic(survey_id, section_id);
        List<QuestionDTO.QuestionInfoByIdDto> answerForQuestion = answerService.getQuestionBySectionForStatistic(survey_id, section_id);
//        return new ResponseEntity<>(List<answerForQuestion>, HttpStatus.CREATED);
        return new ResponseEntity<>(answerForQuestion, HttpStatus.CREATED);
    }

    /**
     *
     * @param participateAnswerListDTO
     * @return void
     * 설문 참여 응답 저장
     */
    @PostMapping(value = "/answer/participate")
    public void formParticipate(@RequestBody ParticipateAnswerListDTO participateAnswerListDTO){
        log.info("데이터 들어옴");

        Long currentNullableMemberId = SecurityUtil.getCurrentNullableMemberId();

        log.info("currentNullableMemberId : {}", currentNullableMemberId);

        answerService.participateForm(participateAnswerListDTO,currentNullableMemberId);
    }

    // TODO 현종 참고용
    @GetMapping(value = "/survey/currentMember/test")
    public ResponseEntity<Long> tester() {
        Long currentNullableMemberId = SecurityUtil.getCurrentNullableMemberId();
        System.out.println(currentNullableMemberId);
        log.info("currentNullableMemberId : {}", currentNullableMemberId);
        return new ResponseEntity<>(currentNullableMemberId, HttpStatus.OK);
    }

}
