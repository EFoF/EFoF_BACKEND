package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Survey survey) {
        em.persist(survey);
    }

    public Survey findOne(Long id) {
        return em.find(Survey.class, id);
    }

    public List<Survey> findAll() {
        return em.createQuery("select s from Survey s", Survey.class)
                .getResultList();
    }

    public List<Survey> findById(Long id) {
        return em.createQuery("select s from Survey s where s.id = :id", Survey.class)
                .setParameter("id", id)
                .getResultList();
    }

    // implements 때문에..
//    @Override
//    public List<SurveyDTO.SurveyInfoDTO> findSurveyInfoDTOBySurveyIdQuery(Long surveyId) {
//        return null;
//    }
}

