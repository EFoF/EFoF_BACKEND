package com.service.surveyservice.domain.answer.api;

import com.service.surveyservice.domain.answer.application.AnswerService;
import com.service.surveyservice.domain.answer.dao.AnswerRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    private final AnswerRepository answerRepository;

    //아직 구현 안 함
//    @GetMapping(value = "/answer/memberSurvey/question") // 사용자가 참여한 설문(MemberSurvey)의 질문에 대한 답 조회
//    public void getAnswerDetail(){
//        Long currentMemberId = SecurityUtil.getCurrentMemberId();
//        answerService.getAnwerDetail(currentMemberId);
//    }
}
