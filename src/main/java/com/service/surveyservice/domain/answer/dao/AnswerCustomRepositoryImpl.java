package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.model.Answer;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    public void save(Survey survey) {
        em.persist(survey);
    }

    public Survey findOne(Long id) {
        return em.find(Survey.class, id);
    }

    public List<Survey> findAllSurvey() {
        return em.createQuery("select s from Survey s", Survey.class)
                .getResultList();
    }
    // implements 때문에..
//    @Override
//    public List<SurveyDTO.SurveyInfoDTO> findSurveyInfoDTOBySurveyIdQuery(Long surveyId) {
//        return null;
//    }

    @Override
    public void saveAll(List<AnswerDTO.AnswerForBatch> answerForBatchList){
        String sql = "INSERT INTO answer (answer_sentence, member_survey_id, question_id, question_choice_id, create_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AnswerDTO.AnswerForBatch answerForBatch = answerForBatchList.get(i);
                Long questionType = answerForBatch.getQuestionType();
                LocalDateTime localDateTime = LocalDateTime.now();
                log.info("questionType:{}",questionType);
                ps.setString(1, answerForBatch.getAnswerSentence());
                ps.setLong(2, answerForBatch.getMemberSurveyId());
                ps.setLong(3, answerForBatch.getQuestionId());
                if (questionType != 1)
                    ps.setLong(4, answerForBatch.getQuestionOptionId());
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(localDateTime));
            }

            @Override
            public int getBatchSize() {
                return answerForBatchList.size();
            }
        });
    }
}

