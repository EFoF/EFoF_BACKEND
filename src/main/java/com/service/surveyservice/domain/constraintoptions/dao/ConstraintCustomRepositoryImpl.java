package com.service.surveyservice.domain.constraintoptions.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.surveyservice.domain.answer.dao.AnswerCustomRepository;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.QConstraintOptions;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.service.surveyservice.domain.constraintoptions.model.QConstraintOptions.constraintOptions;
import static com.service.surveyservice.domain.survey.model.QMemberSurvey.memberSurvey;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ConstraintCustomRepositoryImpl implements ConstraintCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ConstraintOptions> findConstraintBySurveyId(Long surveyId) {

        List<ConstraintOptions> constraintOptionsList = jpaQueryFactory
                .select(constraintOptions)
                .from(constraintOptions)
                .where(constraintOptions.survey.id.eq(surveyId)).fetch();

        return constraintOptionsList;
    }
}

