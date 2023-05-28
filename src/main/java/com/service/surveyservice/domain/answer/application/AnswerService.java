package com.service.surveyservice.domain.answer.application;

import com.service.surveyservice.domain.answer.dao.AnswerCustomRepository;
import com.service.surveyservice.domain.answer.dao.AnswerRepository;
import com.service.surveyservice.domain.answer.dto.AnswerDTO;

import com.service.surveyservice.domain.answer.exceptoin.exceptions.*;
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
import com.service.surveyservice.domain.question.dto.QuestionDTO;

import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.question.model.QuestionType;

import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.survey.dao.MemberSurveyRepository;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.token.exception.exceptions.ExpiredAccessTokenException;
import com.service.surveyservice.domain.token.exception.exceptions.NoSuchCookieException;
import com.service.surveyservice.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import java.util.stream.Collectors;

import static com.service.surveyservice.global.common.constants.ResponseConstants.CREATED;
import static com.service.surveyservice.domain.answer.dto.AnswerDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final SectionRepository sectionRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final AnswerRepository answerRepository;
    private final AnswerCustomRepository answerCustomRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final ConstraintRepository constraintRepository;
    private final ConstraintCustomRepositoryImpl constraintCustomRepository;
    private final JwtTokenProvider jwtTokenProvider;

//
//    public List<SurveyInfoDTO> getInfoTitleNDescription(Long surveyId) {
//        return answerCustomRepository.findSurveyInfoDTOBySurveyIdQuery(surveyId);
//    }

    public AnswerDTO.SurveyForStatisticResponseDto getSurveyForStatistic(Long surveyId, Long memberId) {


//        0. 사용자가 설문 ID 등을 포함해서 설문 통계 조회 요청을 보냄
        Optional<Survey> surveyByOptional = surveyRepository.findById(surveyId);
        System.out.println("surveyByOptional = " + surveyByOptional);
        if (surveyByOptional.isEmpty()) { // 해당하는 survey id가 없으면 예외 처리
            throw new SurveyNotFoundException();
        }

        Survey survey = surveyByOptional.get(); // 잘 모르겠지만 get으로 한 번 더 새로운 변수에 옮겨준다

        Integer countConstraints = constraintCustomRepository.findConstraintBySurveyId(surveyId);
//        List<MemberSurvey> memberBySurvey = memberSurveyRepository.findBySurveyId(surveyId);

        if (countConstraints != 0) { //조회 제한 있음
            //예외를 터트리는데 이게 만든사람이 요청한거면 예외 터트리지말고 아니면 터트린다.
            //!(조회하는 사람이 만든사람인가? || 조회하는 사람이 설문에 참여한 사람인가?)
            if (!(survey.getAuthor().getId().equals(memberId)))
//                        || (memberSurveyRepository.findMemberIdBySurveyId(surveyId, memberId)!=0)))
            {
                throw new AuthorOrMemberNotFoundException();
            }
        } else {//조회 제한 없음 - 참여한 사람 아닌경우 예외처리 + 만든사람은 제외
            if (memberSurveyRepository.findMemberIdBySurveyId(surveyId, memberId) == 0) {
                if (!(survey.getAuthor().getId().equals(memberId))) { //만든사람도 아니야
                    throw new AuthorOrMemberNotFoundException();
                }
            }
        }


        // 1. 통계 기능을 담당하는 백엔드 컨트롤러에서 이 요청을 DTO로 받아서 서비스로직을 호출함
        Integer participantNum = memberSurveyRepository.countParticipantBySurveyId(surveyId);
//        Optional<Survey> survey = surveyRepository.findById(surveyId);
        List<Long> sectionList = sectionRepository.findIdBySurveyId(surveyId);

        // survey는 해당 DTO에서 title, description, sImageUTL을 포함
        return new AnswerDTO.SurveyForStatisticResponseDto().toResponseDto(survey, participantNum, sectionList);
    }


    public AnswerDTO.QuestionBySectionForStatisticResponseDto getQuestionBySectionForStatistic(Long surveyId, Long sectionId) {
        // 1-1. sectionId에 따른 questionOrder를 가져옴
        String questionOrder = sectionRepository.findQuestionOrderById(sectionId);

        // 1-2. questionOrder 타입 변환 (String -> Long)
        List<Long> questionOrderList = Arrays.stream(questionOrder.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
//        log.info(questionOrderList.toString()); // [8, 7, 9]

        /**
         * 1. 먼저 질문에 대한 정보 리스트 - 정보를 어딘가에 넣어줘야함
         * 2. 객관식 답변 리스트 - 위 리스트에 각각 넣어줘야함
         * 3. 주관식 답변 리스트
         */
        List<QuestionRepository.questionInfoByIdDtoI> questionInfoByIdInter = questionRepository.findQuestionInfoById(sectionId);
        List<QuestionDTO.QuestionInfoByIdDto> questionInfoById = questionInfoByIdInter.stream().map(QuestionDTO.QuestionInfoByIdDto::new).collect(Collectors.toList());

        // 객관식
        List<AnswerRepository.choiceAnswerResponseDtoI> choiceAnswerByQuestionIdInter = answerRepository.findChoiceAnswerByQuestionId(sectionId);
        List<AnswerDTO.ChoiceAnswerResponseDto> choiceAnswerByQuestionId = choiceAnswerByQuestionIdInter.stream().map(AnswerDTO.ChoiceAnswerResponseDto::new).collect(Collectors.toList());
        Map<Long, List<ChoiceAnswerResponseDto>> choiceQuestionOptionList = choiceAnswerByQuestionId.stream()
                .collect(Collectors.groupingBy(choiceAnswerByQuestionIdDto -> choiceAnswerByQuestionIdDto.getQuestion_id()));

        // 주관식
        List<AnswerRepository.longAnswerResponseDtoI> longAnswerByQuestionIdInter = answerRepository.findLongAnswerByQuestionId(sectionId);
        List<AnswerDTO.LongAnswerResponseDto> longAnswerByQuestionId = longAnswerByQuestionIdInter.stream().map(AnswerDTO.LongAnswerResponseDto::new).collect(Collectors.toList());
        Map<Long, List<LongAnswerResponseDto>> longQuestionOptionList = longAnswerByQuestionId.stream()
                .collect(Collectors.groupingBy(longAnswerByQuestionIdDto -> longAnswerByQuestionIdDto.getQuestion_id()));

        return new QuestionDTO.QuestionInfoByIdDto().toResponseDto((QuestionDTO.QuestionInfoByIdDto) questionInfoById, choiceQuestionOptionList, longQuestionOptionList);
    }

//    설문 참여 응답 저장
    @Transactional
    public String participateForm(ParticipateAnswerListDTO participateAnswerListDTO, Long currentNullableMemberId) {
        Long surveyId = participateAnswerListDTO.getSurveyId();
        log.info("surveyID : {}", surveyId);

        List<ParticipateAnswerDTO> participateAnswerDTOList = participateAnswerListDTO.getParticipateAnswerDTOList();

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundAnswerException::new);
        log.info("survey : {}", survey);

        //survey로 제약조건 조사
        List<ConstraintOptions> constraintOptions = constraintRepository.findBySurvey(survey);

        //bulk insert 할 List 생성
        List<AnswerForBatch> subAnswers = new ArrayList<>();

        //제약 조건들 담을 List 생성
        List<ConstraintType> constraintTypeList = new ArrayList<>();

        for (ConstraintOptions constraintOption : constraintOptions) {
            constraintTypeList.add(constraintOption.getConstraintType());
        }

        log.info("constraintTypeList : {}", constraintTypeList);

        boolean constraintLoggedIn = constraintTypeList.contains(ConstraintType.LOGGED_IN);
        boolean constraintEmail = constraintTypeList.contains(ConstraintType.EMAIL_CONSTRAINT);
        boolean constraintAnonymous = constraintTypeList.contains(ConstraintType.ANONYMOUS);

        if (constraintLoggedIn || constraintEmail) { // 로그인 여부 or Email 걸려있는 survey 일 때 => 로그인 필수
            if (!constraintAnonymous) { // 로그인 여부 or Email 걸려있고 익명이 아닌 상태
                if (currentNullableMemberId == null) { //currentNullableMemberId 가 없으면 예외 터뜨리기
                    throw new NoSuchCookieAnswerException(); //로그인 필요하다는 예외
                }

                //로그인이 정상적으로 돼 있을 때 로직
                Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);

                //어떤 유저가 참여한 어떤 설문인지 특정한다.
                Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                    //MemberSurvey 생성
                    MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                            .member(member)
                            .survey(survey)
                            .build();

                    memberSurveyRepository.save(memberSurveyBuilder);
                    log.info("memberSurveyBuilder : {}", memberSurveyBuilder);

                    Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                    for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                        Long questionType = participateAnswerDTO.getQuestionType();
                        Long questionId = participateAnswerDTO.getQuestionId();
                        Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                        if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(questionId)
                                    .answerSentence(participateAnswerDTO.getAnswerSentence())
                                    .memberSurveyId(memberSurvey1.get().getId())
                                    .questionType(questionType)
                                    .isNecessary(isNecessary)
                                    .constraintTypeList(constraintTypeList)
                                    .build());
                        } else { // 주관식이 아닐 경우
                            for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .questionOptionId(questionOptionId)
                                        .memberSurveyId(memberSurvey1.get().getId())
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .constraintTypeList(constraintTypeList)
                                        .build());
                            }
                        }
                    }
                    answerCustomRepository.saveAll(subAnswers);
                } else // 이미 참여한 설문인 경우
                    throw new DuplicatedParticipateException();
            } else { // 로그인 여부 or Email 걸려있고 익명인 상태
                if (currentNullableMemberId == null) { //currentNullableMemberId 가 없으면 예외 터뜨리기
                    throw new NoSuchCookieAnswerException(); //로그인 필요하다는 예외
                }
                //로그인이 정상적으로 돼 있을 때 로직
                Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);

                //어떤 유저가 참여한 어떤 설문인지 특정한다.
                Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                    //MemberSurvey 생성
                    MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                            .member(member)
                            .survey(survey)
                            .build();

                    memberSurveyRepository.save(memberSurveyBuilder);
                    log.info("memberSurveyBuilder : {}", memberSurveyBuilder);

                    Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                    for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                        Long questionType = participateAnswerDTO.getQuestionType();
                        Long questionId = participateAnswerDTO.getQuestionId();
                        Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                        if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(questionId)
                                    .answerSentence(participateAnswerDTO.getAnswerSentence())
                                    .memberSurveyId(memberSurvey1.get().getId())
                                    .questionType(questionType)
                                    .isNecessary(isNecessary)
                                    .constraintTypeList(constraintTypeList)
                                    .build());
                        } else { // 주관식이 아닐 경우
                            for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .questionOptionId(questionOptionId)
                                        .memberSurveyId(memberSurvey1.get().getId())
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .constraintTypeList(constraintTypeList)
                                        .build());
                            }
                        }
                    }
                    answerCustomRepository.saveAll(subAnswers);
                } else // 이미 참여한 설문인 경우
                    throw new DuplicatedParticipateException();
            }
        } else { // 로그인 여부 & Email 안 걸려있을 때 => 로그인 optional
            if (currentNullableMemberId != null) { // 로그인 한 상황
                if (!constraintAnonymous) { // 로그인 했고 익명인 상황
                    //로그인이 정상적으로 돼 있을 때 로직
                    Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);

                    //어떤 유저가 참여한 어떤 설문인지 특정한다.
                    Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                    if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                        //MemberSurvey 생성
                        MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                                .member(member)
                                .survey(survey)
                                .build();

                        memberSurveyRepository.save(memberSurveyBuilder);
                        log.info("memberSurveyBuilder : {}", memberSurveyBuilder);

                        Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                            Long questionType = participateAnswerDTO.getQuestionType();
                            Long questionId = participateAnswerDTO.getQuestionId();
                            Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                            if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .answerSentence(participateAnswerDTO.getAnswerSentence())
                                        .memberSurveyId(memberSurvey1.get().getId())
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .constraintTypeList(constraintTypeList)
                                        .build());
                            } else { // 주관식이 아닐 경우
                                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .questionOptionId(questionOptionId)
                                            .memberSurveyId(memberSurvey1.get().getId())
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .constraintTypeList(constraintTypeList)
                                            .build());
                                }
                            }
                        }
                        answerCustomRepository.saveAll(subAnswers);
                    } else // 이미 참여한 설문인 경우
                        throw new DuplicatedParticipateException();
                }
                else { // 로그인 했고 익명 아닌 상황
                    //로그인이 정상적으로 돼 있을 때 로직
                    Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);

                    //어떤 유저가 참여한 어떤 설문인지 특정한다.
                    Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                    if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                        //MemberSurvey 생성
                        MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                                .member(member)
                                .survey(survey)
                                .build();

                        memberSurveyRepository.save(memberSurveyBuilder);
                        log.info("memberSurveyBuilder : {}", memberSurveyBuilder);

                        Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(member, survey);

                        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                            Long questionType = participateAnswerDTO.getQuestionType();
                            Long questionId = participateAnswerDTO.getQuestionId();
                            Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                            if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .answerSentence(participateAnswerDTO.getAnswerSentence())
                                        .memberSurveyId(memberSurvey1.get().getId())
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .constraintTypeList(constraintTypeList)
                                        .build());
                            } else { // 주관식이 아닐 경우
                                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .questionOptionId(questionOptionId)
                                            .memberSurveyId(memberSurvey1.get().getId())
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .constraintTypeList(constraintTypeList)
                                            .build());
                                }
                            }
                        }
                        answerCustomRepository.saveAll(subAnswers);
                    } else // 이미 참여한 설문인 경우
                        throw new DuplicatedParticipateException();
                }
            }
            else { // 로그인 안 한 상황
                MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                        .member(null)
                        .survey(survey)
                        .build();

                MemberSurvey save = memberSurveyRepository.save(memberSurveyBuilder);
                log.info("memberSurveyBuilder : {}", memberSurveyBuilder);
                Long id = save.getId();
                log.info("id :{}", id);

//                Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(memberSurveyBuilder.getMember(), survey);
//                Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(memberRepository.findById(id).get(), survey);
//                Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findById(id);

                for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                    Long questionType = participateAnswerDTO.getQuestionType();
                    Long questionId = participateAnswerDTO.getQuestionId();
                    Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                    if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                        subAnswers.add(AnswerForBatch.builder()
                                .questionId(questionId)
                                .answerSentence(participateAnswerDTO.getAnswerSentence())
                                .memberSurveyId(id)
                                .questionType(questionType)
                                .isNecessary(isNecessary)
                                .constraintTypeList(constraintTypeList)
                                .build());
                    } else { // 주관식이 아닐 경우
                        for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(questionId)
                                    .questionOptionId(questionOptionId)
                                    .memberSurveyId(id)
                                    .questionType(questionType)
                                    .isNecessary(isNecessary)
                                    .constraintTypeList(constraintTypeList)
                                    .build());
                        }
                    }
                }
                log.info("insert-----------------------");
                entityManager.flush();
                log.info("insert-----------------------");

                answerCustomRepository.saveAll(subAnswers);

            }
        }
        return CREATED;
    }

    //아래는 리팩토링 중인 코드

//    @Transactional
//    public String participateForm2(ParticipateAnswerListDTO participateAnswerListDTO, Long currentNullableMemberId) {
//        Long surveyId = participateAnswerListDTO.getSurveyId();
//        log.info("surveyID : {}", surveyId);
//
//        List<ParticipateAnswerDTO> participateAnswerDTOList = participateAnswerListDTO.getParticipateAnswerDTOList();
//
//        Survey survey = surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundAnswerException::new);
//        log.info("survey : {}", survey);
//
//        //survey로 제약조건 조사
//        List<ConstraintOptions> constraintOptions = constraintRepository.findBySurvey(survey);
//
//        //bulk insert 할 List 생성
//        List<AnswerForBatch> subAnswers = new ArrayList<>();
//
//        //제약 조건들 담을 List 생성
//        List<ConstraintType> constraintTypeList = new ArrayList<>();
//
//        for (ConstraintOptions constraintOption : constraintOptions) {
//            constraintTypeList.add(constraintOption.getConstraintType());
//        }
//
//        log.info("constraintTypeList : {}", constraintTypeList);
//
//        boolean constraintLoggedIn = constraintTypeList.contains(ConstraintType.LOGGED_IN);
//        boolean constraintEmail = constraintTypeList.contains(ConstraintType.EMAIL_CONSTRAINT);
//        boolean constraintAnonymous = constraintTypeList.contains(ConstraintType.ANONYMOUS);
//
//        if (constraintLoggedIn || constraintEmail) { // 로그인 여부 or Email 걸려있는 survey 일 때 => 로그인 필수
//            if (!constraintAnonymous) {
//                handleLoggedInState(currentNullableMemberId, survey, participateAnswerDTOList, constraintTypeList, subAnswers);
//            } else { // 로그인 여부 or Email 걸려있고 익명인 상태
//                handleAnonymousState(currentNullableMemberId, survey, participateAnswerDTOList, constraintTypeList, subAnswers);
//            }
//        } else { // 로그인 여부 & Email 안 걸려있을 때 => 로그인 optional
//            if (currentNullableMemberId != null) { // 로그인 한 상황
//                if (!constraintAnonymous) { // 로그인 했고 익명 아닌 상황
//                    handleOptionalLoggedInState(currentNullableMemberId, survey, participateAnswerDTOList, constraintTypeList, subAnswers);
//                } else { // 로그인 했고 익명인 상황
//                    handleOptionalAnonymousState(currentNullableMemberId, survey, participateAnswerDTOList, constraintTypeList, subAnswers);
//                }
//            } else { // 로그인 안 한 상황
//                handleLoggedOutState(currentNullableMemberId, survey, participateAnswerDTOList, constraintTypeList, subAnswers);
//            }
//        }
//        return CREATED;
//    }
//
//    private void handleLoggedInState(Long currentNullableMemberId, Survey survey, List<ParticipateAnswerDTO> participateAnswerDTOList,
//                                     List<ConstraintType> constraintTypeList, List<AnswerForBatch> subAnswers) {
//        if (currentNullableMemberId == null) {
//            throw new NoSuchCookieAnswerException();
//        }
//
//        Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);
//        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
//
//        if (memberSurvey.isEmpty()) {
//            Long memberSurvey1 = createMemberSurvey(member, survey);
//
//            for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
//                createAnswerForBatch(participateAnswerDTO, memberSurvey1, constraintTypeList, subAnswers);
//            }
//            answerCustomRepository.saveAll(subAnswers);
//        } else {
//            throw new DuplicatedParticipateException();
//        }
//    }
//
//    private void handleAnonymousState(Long currentNullableMemberId, Survey survey, List<ParticipateAnswerDTO> participateAnswerDTOList,
//                                      List<ConstraintType> constraintTypeList, List<AnswerForBatch> subAnswers) {
//        if (currentNullableMemberId == null) {
//            throw new NoSuchCookieAnswerException();
//        }
//
//        Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);
//        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
//
//        if (memberSurvey.isEmpty()) {
//            Long memberSurvey1 = createMemberSurvey(member, survey);
//
//            for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
//                createAnswerForBatch(participateAnswerDTO, memberSurvey1, constraintTypeList, subAnswers);
//            }
//            answerCustomRepository.saveAll(subAnswers);
//        }
//    }
//
//    private void handleOptionalLoggedInState(Long currentNullableMemberId, Survey survey, List<ParticipateAnswerDTO> participateAnswerDTOList,
//                                             List<ConstraintType> constraintTypeList, List<AnswerForBatch> subAnswers) {
//
//        Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);
//        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
//
//        if (memberSurvey.isEmpty()) {
//            Long memberSurvey1 = createMemberSurvey(member, survey);
//
//            for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
//                createAnswerForBatch(participateAnswerDTO, memberSurvey1, constraintTypeList, subAnswers);
//            }
//            answerCustomRepository.saveAll(subAnswers);
//        } else {
//            throw new DuplicatedParticipateException();
//        }
//    }
//
//    private void handleOptionalAnonymousState(Long currentNullableMemberId, Survey survey, List<ParticipateAnswerDTO> participateAnswerDTOList,
//                                              List<ConstraintType> constraintTypeList, List<AnswerForBatch> subAnswers) {
//
//        Member member = memberRepository.findById(currentNullableMemberId).orElseThrow(UserNotFoundException::new);
//        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
//
//        if (memberSurvey.isEmpty()) {
//            Long memberSurvey1 = createMemberSurvey(member, survey);
//
//            for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
//                createAnswerForBatch(participateAnswerDTO, memberSurvey1, constraintTypeList, subAnswers);
//            }
//            answerCustomRepository.saveAll(subAnswers);
//        } else {
//            throw new DuplicatedParticipateException();
//        }
//    }
//
//    private void handleLoggedOutState(Long currentNullableMemberId, Survey survey, List<ParticipateAnswerDTO> participateAnswerDTOList,
//                                      List<ConstraintType> constraintTypeList, List<AnswerForBatch> subAnswers) {
//
//        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
//            createAnswerForBatch(participateAnswerDTO, null, constraintTypeList, subAnswers);
//        }
//        answerCustomRepository.saveAll(subAnswers);
//
//    }
//
//    private Long createMemberSurvey(Member member, Survey survey) {
//        //MemberSurvey 생성
//        MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
//                .member(member)
//                .survey(survey)
//                .build();
//
//        memberSurveyRepository.save(memberSurveyBuilder);
//        log.info("memberSurveyBuilder : {}", memberSurveyBuilder);
//
//        Optional<MemberSurvey> memberSurvey1 = memberSurveyRepository.findByMemberAndSurvey(member, survey);
//        Long id = memberSurvey1.get().getId();
//
//        return id;
//    }
//
//    private void createAnswerForBatch(ParticipateAnswerDTO participateAnswerDTO, Long memberSurveyId,
//                                      List<ConstraintType> constraintTypeList, List<AnswerForBatch> subAnswers) {
//        Long questionType = participateAnswerDTO.getQuestionType();
//        Long questionId = participateAnswerDTO.getQuestionId();
//        Boolean isNecessary = participateAnswerDTO.getIsNecessary();
//
//        if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
//            AnswerForBatch answerForBatch = new AnswerForBatch(questionId, null, participateAnswerDTO.getAnswerSentence(),
//                    memberSurveyId, questionType, isNecessary, constraintTypeList);
//
//            subAnswers.add(answerForBatch);
//        } else { // 주관식이 아닐 경우
//            for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
//                AnswerForBatch answerForBatch = new AnswerForBatch(questionId, questionOptionId, null,
//                        memberSurveyId, questionType, isNecessary, constraintTypeList);
//
//                subAnswers.add(answerForBatch);
//            }
//        }
//    }
}
