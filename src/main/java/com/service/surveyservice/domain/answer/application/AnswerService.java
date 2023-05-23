package com.service.surveyservice.domain.answer.application;

import com.service.surveyservice.domain.answer.dao.AnswerCustomRepository;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.exceptoin.exceptions.AuthorOrMemberNotFoundException;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintCustomRepositoryImpl;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
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
    private final QuestionRepository questionRepository;

//    private final ConstraintRepository constraintRepository;

    private final ConstraintCustomRepositoryImpl constraintCustomRepository;
//
//    public List<SurveyInfoDTO> getInfoTitleNDescription(Long surveyId) {
//        return answerCustomRepository.findSurveyInfoDTOBySurveyIdQuery(surveyId);
//    }

    public AnswerDTO.SurveyForStatisticResponseDto getSurveyForStatistic(Long surveyId, Long memberId){


//        0. 사용자가 설문 ID 등을 포함해서 설문 통계 조회 요청을 보냄
        Optional<Survey> surveyByOptional = surveyRepository.findById(surveyId);
        System.out.println("surveyByOptional = " + surveyByOptional);
        if(surveyByOptional.isEmpty()){ // 해당하는 survey id가 없으면 예외 처리
            throw new SurveyNotFoundException();
        }

        Survey survey = surveyByOptional.get(); // 잘 모르겠지만 get으로 한 번 더 새로운 변수에 옮겨준다

        Integer countConstraints = constraintCustomRepository.findConstraintBySurveyId(surveyId);
//        List<MemberSurvey> memberBySurvey = memberSurveyRepository.findBySurveyId(surveyId);

        if(countConstraints!=0){ //조회 제한 있음
                //예외를 터트리는데 이게 만든사람이 요청한거면 예외 터트리지말고 아니면 터트린다.
                //!(조회하는 사람이 만든사람인가? || 조회하는 사람이 설문에 참여한 사람인가?)
                if(!(survey.getAuthor().getId().equals(memberId)))
//                        || (memberSurveyRepository.findMemberIdBySurveyId(surveyId, memberId)!=0)))
                {
                    throw new AuthorOrMemberNotFoundException();
                }
        }else{//조회 제한 없음 - 참여한 사람 아닌경우 예외처리 + 만든사람은 제외
            if(memberSurveyRepository.findMemberIdBySurveyId(surveyId, memberId)==0){
                if(!(survey.getAuthor().getId().equals(memberId))) { //만든사람도 아니야
                    throw new AuthorOrMemberNotFoundException();
                }
            }
        }


        // 1. 통계 기능을 담당하는 백엔드 컨트롤러에서 이 요청을 DTO로 받아서 서비스로직을 호출함
        Integer participantNum = memberSurveyRepository.countParticipantBySurveyId(surveyId);
//        Optional<Survey> survey = surveyRepository.findById(surveyId);
        List<Long> sectionList = sectionRepository.findIdBySurveyId(surveyId);

        // survey는 해당 DTO에서 title, description, sImageUTL을 포함
        return new AnswerDTO.SurveyForStatisticResponseDto().toResponseDto(survey,participantNum,sectionList);
    }


    public AnswerDTO.QuestionBySectionForStatisticResponseDto getQuestionBySectionForStatistic(Long surveyId, Long sectionId) {

        // sectionRepository에서 가져올 것
        List<String> questionOrderList = sectionRepository.findQuestionOrderById(surveyId);
//        log.info(questionOrderList.toString());

        // questionRepository에서 가져올 것

        return null;
    }
}
