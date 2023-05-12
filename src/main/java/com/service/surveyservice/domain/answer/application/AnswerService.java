package com.service.surveyservice.domain.answer.application;

import com.service.surveyservice.domain.answer.dao.AnswerCustomRepository;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintCustomRepository;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintCustomRepositoryImpl;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintRepository;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerCustomRepository answerCustomRepository;

    private final SectionRepository sectionRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final SurveyRepository surveyRepository;

//    private final ConstraintRepository constraintRepository;

    private final ConstraintCustomRepositoryImpl constraintCustomRepository;
//
//    public List<SurveyInfoDTO> getInfoTitleNDescription(Long surveyId) {
//        return answerCustomRepository.findSurveyInfoDTOBySurveyIdQuery(surveyId);
//    }

    public AnswerDTO.SurveyForStatisticResponseDto getSurveyForStatistic(Long surveyId, Long memberId){


        //-1. 이 설문이 통계 보는것이 허용 되는지? 추가 예정

//        0. 사용자가 설문 ID 등을 포함해서 설문 통계 조회 요청을 보냄
        Optional<Survey> surveyByOptional = surveyRepository.findById(surveyId);
        if(surveyByOptional==null){
            throw new SurveyNotFoundException();
        }
        Survey survey = surveyByOptional.get();

        List<ConstraintOptions> constraintOptions = constraintCustomRepository.findConstraintBySurveyId(surveyId);


//        1. 통계 기능을 담당하는 백엔드 컨트롤러에서 이 요청을 DTO로 받아서 서비스로직을 호출함

        Integer participantNum = memberSurveyRepository.countParticipantBySurveyId(surveyId);

//        Optional<Survey> survey = surveyRepository.findById(surveyId);

        List<Long> sectionList = sectionRepository.findIdBySurveyId(surveyId);

        return new AnswerDTO.SurveyForStatisticResponseDto().toResponseDto(survey,participantNum,sectionList,constraintOptions);
    }
}
