package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.model.Answer;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerCustomRepository{
    // 사용자가 참여한 설문(MemberSurvey)의 질문에 대한 답
    // queryDSL 사용하는 걸로 함 바꿔야 함.
//    Optional<List<Answer>> findByMemberSurveyAndQuestionAAndQuestionOption(
//            MemberSurvey memberSurvey, Question question, QuestionOption questionOption);
}
