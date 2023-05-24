package com.service.surveyservice.domain.answer.application;

import com.service.surveyservice.domain.answer.dao.AnswerCustomRepository;
import com.service.surveyservice.domain.answer.dao.AnswerRepository;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.exceptoin.exceptions.AuthorOrMemberNotFoundException;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintCustomRepositoryImpl;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final SectionRepository sectionRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

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
        // ====================Part 1. Question Info====================
        // 1-1. sectionId에 따른 questionOrder를 가져옴
        String questionOrder = sectionRepository.findQuestionOrderById(sectionId);
//        log.info(questionOrder);

        String[] split = questionOrder.split(",");
        // 1-2. questionOrder 타입 변환 (String -> Long)
        List<Long> questionOrderList = Arrays.stream(questionOrder.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
//        log.info(questionOrderList.toString()); // [8, 7, 9]


        //List<questionListDto> list = repos
        // 위의 dto에는 객관식 dto, 주관식 dto가 각각 존재
        //list.get(0) => id =7 -> list.get(0) => id=8
        //결국 소팅 해야함

        //List<optionListDto> optionlist = qpRespo
        //optionlist의 question_id가 7인 것들을 리스트로 만들어
        //위에 있는 questionListDto인 list에 값을 넣어


        /**
         * 1. 먼저 질문에 대한 정보 리스트 - 정보를 어딘가에 넣어줘야함
         * 2. 객관식 답변 리스트 - 위 리스트에 각각 넣어줘야함
         * 3. 주관식 답변 리스트
         */
//        List<List<QuestionDTO.QuestionOptionByQuestionDto>> questionInfoById = new ArrayList<>();
//        for(Long question_id : questionOrderList) {
//            List<QuestionDTO.QuestionOptionByQuestionDto> temp = questionRepository.findQuestionInfoById(question_id);
//
//        }

        //
        List<QuestionRepository.questionInfoByIdDtoI> questionInfoByIdInter = questionRepository.findQuestionInfoById(sectionId);
        List<QuestionDTO.QuestionInfoByIdDto> questionInfoById = questionInfoByIdInter.stream().map(QuestionDTO.QuestionInfoByIdDto::new).collect(Collectors.toList());
        log.info(questionInfoById.toString());

        log.info(answerRepository.findLongAnswerByQuestionId(questionOrderList).toString());
        log.info(answerRepository.findChoiceAnswerByQuestionId(questionOrderList).toString());

        // 1-3. questionOrderList 순서대로 question_text, question_type 가져옴
//        List<List<String>> questionInfoList = new ArrayList<>();
//        for (Long question_id : questionOrderList) {
//            String questionText = questionRepository.findQuestionTextById(question_id);
//            String questionType = questionRepository.findQuestionTypeById(question_id);
////            log.info(questionText, questionType);
//
//            List<String> questionInfo = Arrays.asList(questionText, questionType);
//            questionInfoList.add(questionInfo);
//        }
//        log.info(questionInfoList.toString());

        // ====================Part 2. Graph Info====================



        return null;
//        return new AnswerDTO.SurveyForStatisticResponseDto().toResponseDto(survey,participantNum,sectionList);
    }
}
