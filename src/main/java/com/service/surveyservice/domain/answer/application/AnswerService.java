package com.service.surveyservice.domain.answer.application;

import com.service.surveyservice.domain.answer.dao.AnswerCustomRepository;
import com.service.surveyservice.domain.answer.dao.AnswerRepository;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.model.Answer;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintCustomRepositoryImpl;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintRepository;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.UserNotFoundException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.question.dao.QuestionOptionRepository;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.question.model.QuestionType;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.service.surveyservice.global.common.constants.ResponseConstants.CREATED;
import static com.service.surveyservice.domain.answer.dto.AnswerDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerCustomRepository answerCustomRepository;

    private final SectionRepository sectionRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final SurveyRepository surveyRepository;

    private final ConstraintRepository constraintRepository;

    private final ConstraintCustomRepositoryImpl constraintCustomRepository;

    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
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

//    // 설문 참여 응답 저장 - 로그인 한 유저
//    @Transactional
//    public String participateForm(ParticipateAnswerListDTO participateAnswerListDTO, Long currentMemberId){
//        // 응답하려는 사용자를 찾을 수 없다면 예외 발생
//        Member member = memberRepository.findById(currentMemberId).orElseThrow(UserNotFoundException::new);
//        log.info("member : {}",member);
//
//        Long surveyId = participateAnswerListDTO.getSurveyId();
//        log.info("surveyID : {}",surveyId);
//
//        Optional<Survey> survey = surveyRepository.findById(surveyId);
//        log.info("survey : {}",survey.get());
//
//        List<ConstraintOptions> constraintOptions = constraintRepository.findBySurvey(survey.get());
//
//        for (ConstraintOptions constraintOption : constraintOptions) {
//            ConstraintType constraintType = constraintOption.getConstraintType();
//            // 로그인 여부 & Email 걸려있는 survey 일 때
//            if (constraintType == ConstraintType.LOGGED_IN || constraintType == ConstraintType.EMAIL_CONSTRAINT){
//
//            }
//            else {
//                member = null;
//            }
//        }
//
//        //MemberSurvey 생성
//        MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
//                .member(member)
//                .survey(survey.get())
//                .build();
//
//        memberSurveyRepository.save(memberSurveyBuilder);
//        log.info("memberSurveyBuilder : {}",memberSurveyBuilder);
//
//        //어떤 유저가 참여한 어떤 설문인지 특정한다.
//        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey.get());
//        log.info("memberSurveyId : {}",memberSurvey.get().getId());
//
//        List<ParticipateAnswerDTO> participateAnswerDTOList = participateAnswerListDTO.getParticipateAnswerDTOList();
//
//        //bulk insert 할 List 생성
//        List<AnswerForBatch> subAnswers = new ArrayList<>();
//
//        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
//
//            Long questionType = participateAnswerDTO.getQuestionType();
//            if (questionType == QuestionType.LONG_ANSWER.getId()){ // 주관식 일 경우
//                subAnswers.add(AnswerForBatch.builder()
//                        .questionId(participateAnswerDTO.getQuestionId())
//                        .answerSentence(participateAnswerDTO.getAnswerSentence())
//                        .memberSurveyId(memberSurvey.get().getId())
//                        .questionType(questionType)
//                        .build());
//            }
//            else { // 주관식이 아닐 경우
//                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
//                    subAnswers.add(AnswerForBatch.builder()
//                            .questionId(participateAnswerDTO.getQuestionId())
//                            .questionOptionId(questionOptionId)
//                            .memberSurveyId(memberSurvey.get().getId())
//                            .questionType(questionType)
//                            .build());
//                }
//            }
//        }
//
//        answerCustomRepository.saveAll(subAnswers);
////        Iterator<ParticipateAnswerDTO> iterator = participateAnswerDTOList.iterator();
//////        Iterator<ParticipateAnswerDTO> iterator = participateAnswerListDTO.iterator();
////
////        while (iterator.hasNext()){
////            ParticipateAnswerDTO next = iterator.next(); // 인덱스 값을 반환하고 다음 인덱스로 커서를 옮김
////
////            //질문 pk 값
////            Long questionId = next.getQuestionId();
////            Optional<Question> question = questionRepository.findById(questionId);
////
////            //질문 형식
////            QuestionType questionType = QuestionType.fromId(next.getQuestionType());
////
////            //객관식 답변
//////            Long questionChoiceId = next.getQuestionChoiceId();
//////            Optional<QuestionOption> questionOption = questionOptionRepository.findById(questionChoiceId);
////            List<Long> questionChoiceId = next.getQuestionChoiceId();
////            Iterator<Long> choiceIterator = questionChoiceId.iterator();
////            while (choiceIterator.hasNext()){
////                Long choiceNext = choiceIterator.next();
////                Optional<QuestionOption> questionOption = questionOptionRepository.findById(choiceNext);
////            }
////
////
////            //주관식 답변
////            String answerSentence = next.getAnswerSentence();
////
////            Answer answers = Answer.builder()
////                    .memberSurvey(memberSurvey.get())
////                    .question(question.get())
////                    .questionOption(questionOption.get())
////                    .answerSentence(answerSentence)
////                    .build();
////
////            answerRepository.save(answers);
////        }
//        return CREATED;
//    }

    // 설문 참여 응답 저장 - 로그인 한 유저
    @Transactional
    public String participateForm(ParticipateAnswerListDTO participateAnswerListDTO, Long currentMemberId){
        // 응답하려는 사용자를 찾을 수 없다면 예외 발생
        Member member = memberRepository.findById(currentMemberId).orElseThrow(UserNotFoundException::new);
        log.info("member : {}",member);

        Long surveyId = participateAnswerListDTO.getSurveyId();
        log.info("surveyID : {}",surveyId);

        Optional<Survey> survey = surveyRepository.findById(surveyId);
        log.info("survey : {}",survey.get());

        //survey로 제약조건 조사
        List<ConstraintOptions> constraintOptions = constraintRepository.findBySurvey(survey.get());

        List<ParticipateAnswerDTO> participateAnswerDTOList = participateAnswerListDTO.getParticipateAnswerDTOList();

        //bulk insert 할 List 생성
        List<AnswerForBatch> subAnswers = new ArrayList<>();

        for (ConstraintOptions constraintOption : constraintOptions) {
            ConstraintType constraintType = constraintOption.getConstraintType();
            // 로그인 여부 & Email 걸려있는 survey 일 때
            if (constraintType == ConstraintType.LOGGED_IN || constraintType == ConstraintType.EMAIL_CONSTRAINT){
                //MemberSurvey 생성
                MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                        .member(member)
                        .survey(survey.get())
                        .build();

                memberSurveyRepository.save(memberSurveyBuilder);
                log.info("memberSurveyBuilder : {}",memberSurveyBuilder);

                //어떤 유저가 참여한 어떤 설문인지 특정한다.
                Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey.get());
                log.info("memberSurveyId : {}",memberSurvey.get().getId());

                for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                    Long questionType = participateAnswerDTO.getQuestionType();
                    if (questionType == QuestionType.LONG_ANSWER.getId()){ // 주관식 일 경우
                        subAnswers.add(AnswerForBatch.builder()
                                .questionId(participateAnswerDTO.getQuestionId())
                                .answerSentence(participateAnswerDTO.getAnswerSentence())
                                .memberSurveyId(memberSurvey.get().getId())
                                .questionType(questionType)
                                .build());
                    }
                    else { // 주관식이 아닐 경우
                        for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(participateAnswerDTO.getQuestionId())
                                    .questionOptionId(questionOptionId)
                                    .memberSurveyId(memberSurvey.get().getId())
                                    .questionType(questionType)
                                    .build());
                        }
                    }
                }

                answerCustomRepository.saveAll(subAnswers);
            }
            // 설문 참여에 로그인이 필요없을 경우
            else {
                member = null;

                for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                    Long questionType = participateAnswerDTO.getQuestionType();
                    if (questionType == QuestionType.LONG_ANSWER.getId()){ // 주관식 일 경우
                        subAnswers.add(AnswerForBatch.builder()
                                .questionId(participateAnswerDTO.getQuestionId())
                                .answerSentence(participateAnswerDTO.getAnswerSentence())
//                                .memberSurveyId(memberSurvey.get().getId())
                                .memberSurveyId(null)
                                .questionType(questionType)
                                .build());
                    }
                    else { // 주관식이 아닐 경우
                        for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(participateAnswerDTO.getQuestionId())
                                    .questionOptionId(questionOptionId)
//                                    .memberSurveyId(memberSurvey.get().getId())
                                    .memberSurveyId(null)
                                    .questionType(questionType)
                                    .build());
                        }
                    }
                }

                answerCustomRepository.saveAll(subAnswers);
            }
        }
//        Iterator<ParticipateAnswerDTO> iterator = participateAnswerDTOList.iterator();
////        Iterator<ParticipateAnswerDTO> iterator = participateAnswerListDTO.iterator();
//
//        while (iterator.hasNext()){
//            ParticipateAnswerDTO next = iterator.next(); // 인덱스 값을 반환하고 다음 인덱스로 커서를 옮김
//
//            //질문 pk 값
//            Long questionId = next.getQuestionId();
//            Optional<Question> question = questionRepository.findById(questionId);
//
//            //질문 형식
//            QuestionType questionType = QuestionType.fromId(next.getQuestionType());
//
//            //객관식 답변
////            Long questionChoiceId = next.getQuestionChoiceId();
////            Optional<QuestionOption> questionOption = questionOptionRepository.findById(questionChoiceId);
//            List<Long> questionChoiceId = next.getQuestionChoiceId();
//            Iterator<Long> choiceIterator = questionChoiceId.iterator();
//            while (choiceIterator.hasNext()){
//                Long choiceNext = choiceIterator.next();
//                Optional<QuestionOption> questionOption = questionOptionRepository.findById(choiceNext);
//            }
//
//
//            //주관식 답변
//            String answerSentence = next.getAnswerSentence();
//
//            Answer answers = Answer.builder()
//                    .memberSurvey(memberSurvey.get())
//                    .question(question.get())
//                    .questionOption(questionOption.get())
//                    .answerSentence(answerSentence)
//                    .build();
//
//            answerRepository.save(answers);
//        }
        return CREATED;
    }
}
