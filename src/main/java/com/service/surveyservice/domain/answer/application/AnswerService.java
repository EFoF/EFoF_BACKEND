package com.service.surveyservice.domain.answer.application;

import com.service.surveyservice.domain.answer.dao.AnswerRepository;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.model.Answer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    //아직 구현 안 함
    @Transactional(readOnly = true) // 사용자가 참여한 설문(MemberSurvey)의 질문에 대한 답 조회 로직
    public void getAnwerDetail(AnswerDTO answerDTO, Long currentMemberId){
        Optional<List<Answer>> answerDetail =
                answerRepository.findByMemberSurveyAndQuestionAAndQuestionOption(); // 옘병이다.!!

    }
}
