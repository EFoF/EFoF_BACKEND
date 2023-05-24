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

import javax.servlet.http.Cookie;
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
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final MemberSurveyRepository memberSurveyRepository;
    private final SectionRepository sectionRepository;
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

        // sectionRepository에서 가져올 것
        String questionOrder = sectionRepository.findQuestionOrderById(sectionId);
        log.info(questionOrder);

        // questionRepository에서 가져올 것

        return null;
    }

    // 설문 참여 응답 저장
    @Transactional
    public String participateForm(ParticipateAnswerListDTO participateAnswerListDTO, Cookie cookie) {
        Long surveyId = participateAnswerListDTO.getSurveyId();
        log.info("surveyID : {}", surveyId);

        List<ParticipateAnswerDTO> participateAnswerDTOList = participateAnswerListDTO.getParticipateAnswerDTOList();

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundAnswerException::new);
        log.info("survey : {}", survey);

        //survey로 제약조건 조사
        List<ConstraintOptions> constraintOptions = constraintRepository.findBySurvey(survey);
        log.info("constraintOptions: {}", constraintOptions.toString());

        //bulk insert 할 List 생성
        List<AnswerForBatch> subAnswers = new ArrayList<>();

        boolean constraintLoggedIn = constraintOptions.contains(ConstraintType.LOGGED_IN);
        boolean constraintEmail = constraintOptions.contains(ConstraintType.EMAIL_CONSTRAINT);
        boolean constraintAnonymous = constraintOptions.contains(ConstraintType.ANONYMOUS);

        if (constraintLoggedIn || constraintEmail) { // 로그인 여부 or Email 걸려있는 survey 일 때 => 로그인 필수
            if (!constraintAnonymous) { // 로그인 여부 or Email 걸려있고 익명이 아닌 상태
                if (cookie == null) { // 토큰이 있는 지 검증
                    throw new NoSuchCookieAnswerException(); //로그인 필요하다는 예외
                }
                String jwt = cookie.getValue(); //쿠키 값 꺼내오기

                //토큰이 괜찮은 놈인지 검증
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    //로그인이 정상적으로 돼 있을 때 로직
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    Long memberId = Long.valueOf(authentication.getName());
                    Member member = memberRepository.findById(memberId).orElseThrow(UserNotFoundException::new);

                    //어떤 유저가 참여한 어떤 설문인지 특정한다.
                    Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
                    log.info("memberSurveyId : {}", memberSurvey.get().getId());

                    if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                        //MemberSurvey 생성
                        MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                                .member(member)
                                .survey(survey)
                                .build();

                        memberSurveyRepository.save(memberSurveyBuilder);
                        log.info("memberSurveyBuilder : {}", memberSurveyBuilder);


                        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                            Long questionType = participateAnswerDTO.getQuestionType();
                            Long questionId = participateAnswerDTO.getQuestionId();
                            Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                            if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .answerSentence(participateAnswerDTO.getAnswerSentence())
                                        .memberSurveyId(memberSurvey.get().getId())
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .build());
                            } else { // 주관식이 아닐 경우
                                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .questionOptionId(questionOptionId)
                                            .memberSurveyId(memberSurvey.get().getId())
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .build());
                                }
                            }
                        }
                        answerCustomRepository.saveAll(subAnswers);
                    } else // 이미 참여한 설문인 경우
                        throw new DuplicatedParticipateException();
                } else { //토큰 값이 이상한 경우
                    throw new ExpiredAccessTokenAnswerException();
                }
            } else { // 로그인 여부 or Email 걸려있고 익명인 상태
                if (cookie == null) { // 토큰이 있는 지 검증
                    throw new NoSuchCookieAnswerException(); //로그인 필요하다는 예외
                }

                String jwt = cookie.getValue(); //쿠키 값 꺼내오기

                //토큰이 괜찮은 놈인지 검증
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    //로그인이 정상적으로 돼 있을 때 로직
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    Long memberId = Long.valueOf(authentication.getName());
                    Member member = memberRepository.findById(memberId).orElseThrow(UserNotFoundException::new);

                    //어떤 유저가 참여한 어떤 설문인지 특정한다.
                    Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
                    log.info("memberSurveyId : {}", memberSurvey.get().getId());

                    if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                            Long questionType = participateAnswerDTO.getQuestionType();
                            Long questionId = participateAnswerDTO.getQuestionId();
                            Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                            if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .answerSentence(participateAnswerDTO.getAnswerSentence())
//                                .memberSurveyId(memberSurvey.get().getId())
                                        .memberSurveyId(null)
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .build());
                            } else { // 주관식이 아닐 경우
                                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .questionOptionId(questionOptionId)
//                                    .memberSurveyId(memberSurvey.get().getId())
                                            .memberSurveyId(null)
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .build());
                                }
                            }
                        }
                        answerCustomRepository.saveAll(subAnswers);
                    } else // 이미 참여한 설문인 경우
                        throw new DuplicatedParticipateException();
                } else { //토큰 값이 이상한 경우
                    throw new ExpiredAccessTokenAnswerException();
                }
            }
        } else { // 로그인 여부 & Email 안 걸려있을 때 => 로그인 optional
            if (!constraintAnonymous) { // 로그인 여부 or Email optional 이고 익명이 아닌 상태
                // 로그인 한 상태로 설문에 참여한 경우
                if (cookie != null) { // 토큰이 있는 지 검증
                    String jwt = cookie.getValue(); //쿠키 값 꺼내오기

                    //토큰이 괜찮은 놈인지 검증
                    if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                        //로그인이 정상적으로 돼 있을 때 로직
                        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                        Long memberId = Long.valueOf(authentication.getName());
                        Member member = memberRepository.findById(memberId).orElseThrow(UserNotFoundException::new);

                        //어떤 유저가 참여한 어떤 설문인지 특정한다.
                        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
//                        log.info("memberSurveyId : {}", memberSurvey.get().getId());

                        if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.

                            //MemberSurvey 생성
                            MemberSurvey memberSurveyBuilder = MemberSurvey.builder()
                                    .member(member)
                                    .survey(survey)
                                    .build();

                            memberSurveyRepository.save(memberSurveyBuilder);
                            log.info("memberSurveyBuilder : {}", memberSurveyBuilder);

                            for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                                Long questionType = participateAnswerDTO.getQuestionType();
                                Long questionId = participateAnswerDTO.getQuestionId();
                                Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                                if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .answerSentence(participateAnswerDTO.getAnswerSentence())
                                            .memberSurveyId(memberSurvey.get().getId())
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .build());
                                } else { // 주관식이 아닐 경우
                                    for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                        subAnswers.add(AnswerForBatch.builder()
                                                .questionId(questionId)
                                                .questionOptionId(questionOptionId)
                                                .memberSurveyId(memberSurvey.get().getId())
                                                .questionType(questionType)
                                                .isNecessary(isNecessary)
                                                .build());
                                    }
                                }
                            }

                            answerCustomRepository.saveAll(subAnswers);
                        } else // 이미 참여한 설문인 경우
                            throw new DuplicatedParticipateException();

                    } else { // 토큰이 존재하지만, 토큰 값이 이상한 경우 => 로그인을 하지 않은 상태로 설문에 참여한 경우
                        // 로그인 안 했을 때 로직
                        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                            Long questionType = participateAnswerDTO.getQuestionType();
                            Long questionId = participateAnswerDTO.getQuestionId();
                            Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                            if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .answerSentence(participateAnswerDTO.getAnswerSentence())
//                                .memberSurveyId(memberSurvey.get().getId())
                                        .memberSurveyId(null)
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .build());
                            } else { // 주관식이 아닐 경우
                                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .questionOptionId(questionOptionId)
//                                    .memberSurveyId(memberSurvey.get().getId())
                                            .memberSurveyId(null)
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .build());
                                }
                            }
                        }

                        answerCustomRepository.saveAll(subAnswers);

                    }
                } else { // 토큰이 존재하지 않는 경우 => 로그인을 하지 않은 상태로 설문에 참여한 경우
                    // 로그인 안 했을 때 로직
                    for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                        Long questionType = participateAnswerDTO.getQuestionType();
                        Long questionId = participateAnswerDTO.getQuestionId();
                        Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                        if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(questionId)
                                    .answerSentence(participateAnswerDTO.getAnswerSentence())
//                                .memberSurveyId(memberSurvey.get().getId())
                                    .memberSurveyId(null)
                                    .questionType(questionType)
                                    .isNecessary(isNecessary)
                                    .build());
                        } else { // 주관식이 아닐 경우
                            for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .questionOptionId(questionOptionId)
//                                    .memberSurveyId(memberSurvey.get().getId())
                                        .memberSurveyId(null)
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .build());
                            }
                        }
                    }

                    answerCustomRepository.saveAll(subAnswers);

                }
            } else { // 로그인 여부 or Email optional 이고 익명인 상태
                // 로그인 한 상태로 설문에 참여한 경우
                if (cookie != null) { // 토큰이 있는 지 검증
                    String jwt = cookie.getValue(); //쿠키 값 꺼내오기

                    //토큰이 괜찮은 놈인지 검증
                    if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                        //로그인이 정상적으로 돼 있을 때 로직
                        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                        Long memberId = Long.valueOf(authentication.getName());
                        Member member = memberRepository.findById(memberId).orElseThrow(UserNotFoundException::new);

                        //어떤 유저가 참여한 어떤 설문인지 특정한다.
                        Optional<MemberSurvey> memberSurvey = memberSurveyRepository.findByMemberAndSurvey(member, survey);
                        log.info("memberSurveyId : {}", memberSurvey.get().getId());

                        if (memberSurvey.isEmpty()) { // 설문에 참여한 적이 없는 유저만 참여 가능함.
                            for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                                Long questionType = participateAnswerDTO.getQuestionType();
                                Long questionId = participateAnswerDTO.getQuestionId();
                                Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                                if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .answerSentence(participateAnswerDTO.getAnswerSentence())
                                            .memberSurveyId(null)
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .build());
                                } else { // 주관식이 아닐 경우
                                    for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                        subAnswers.add(AnswerForBatch.builder()
                                                .questionId(questionId)
                                                .questionOptionId(questionOptionId)
                                                .memberSurveyId(null)
                                                .questionType(questionType)
                                                .isNecessary(isNecessary)
                                                .build());
                                    }
                                }
                            }
                            answerCustomRepository.saveAll(subAnswers);
                        } else // 이미 참여한 설문인 경우
                            throw new DuplicatedParticipateException();
                    } else { // 토큰이 존재하지만, 토큰 값이 이상한 경우 => 로그인을 하지 않은 상태로 설문에 참여한 경우
                        // 로그인 안 했을 때 로직
                        for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                            Long questionType = participateAnswerDTO.getQuestionType();
                            Long questionId = participateAnswerDTO.getQuestionId();
                            Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                            if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .answerSentence(participateAnswerDTO.getAnswerSentence())
//                                .memberSurveyId(memberSurvey.get().getId())
                                        .memberSurveyId(null)
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .build());
                            } else { // 주관식이 아닐 경우
                                for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                    subAnswers.add(AnswerForBatch.builder()
                                            .questionId(questionId)
                                            .questionOptionId(questionOptionId)
//                                    .memberSurveyId(memberSurvey.get().getId())
                                            .memberSurveyId(null)
                                            .questionType(questionType)
                                            .isNecessary(isNecessary)
                                            .build());
                                }
                            }
                        }

                        answerCustomRepository.saveAll(subAnswers);

                    }
                } else { // 토큰이 존재하지 않는 경우 => 로그인을 하지 않은 상태로 설문에 참여한 경우
                    // 로그인 안 했을 때 로직
                    for (ParticipateAnswerDTO participateAnswerDTO : participateAnswerDTOList) {
                        Long questionType = participateAnswerDTO.getQuestionType();
                        Long questionId = participateAnswerDTO.getQuestionId();
                        Boolean isNecessary = participateAnswerDTO.getIsNecessary();

                        if (questionType == QuestionType.LONG_ANSWER.getId()) { // 주관식 일 경우
                            subAnswers.add(AnswerForBatch.builder()
                                    .questionId(questionId)
                                    .answerSentence(participateAnswerDTO.getAnswerSentence())
//                                .memberSurveyId(memberSurvey.get().getId())
                                    .memberSurveyId(null)
                                    .questionType(questionType)
                                    .isNecessary(isNecessary)
                                    .build());
                        } else { // 주관식이 아닐 경우
                            for (Long questionOptionId : participateAnswerDTO.getQuestionChoiceId()) {
                                subAnswers.add(AnswerForBatch.builder()
                                        .questionId(questionId)
                                        .questionOptionId(questionOptionId)
//                                    .memberSurveyId(memberSurvey.get().getId())
                                        .memberSurveyId(null)
                                        .questionType(questionType)
                                        .isNecessary(isNecessary)
                                        .build());
                            }
                        }
                    }
                    answerCustomRepository.saveAll(subAnswers);
                }
            }
        }
        return CREATED;
    }
}
