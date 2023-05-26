package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.exceptoin.exceptions.NoNecessaryAnswerException;
import com.service.surveyservice.domain.answer.model.Answer;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
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
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import static java.sql.Types.NULL;

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
    public void saveAll(List<AnswerDTO.AnswerForBatch> answerForBatchList) {
        String sql = "INSERT INTO answer (answer_sentence, member_survey_id, question_id, question_choice_id, create_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AnswerDTO.AnswerForBatch answerForBatch = answerForBatchList.get(i);

                Long questionType = answerForBatch.getQuestionType();
                log.info("questionType:{}", questionType);

                Boolean isNecessary = answerForBatch.getIsNecessary();

                LocalDateTime localDateTime = LocalDateTime.now();

                String answerSentence = answerForBatch.getAnswerSentence();
                Long questionId = answerForBatch.getQuestionId();
                Long questionOptionId = answerForBatch.getQuestionOptionId();

                List<ConstraintType> constraintTypeList = answerForBatch.getConstraintTypeList();

                boolean constraintAnonymous = constraintTypeList.contains(ConstraintType.ANONYMOUS);

                if (isNecessary) {
                    if (answerSentence != null || questionOptionId != null) {
                        setParameters(ps, answerSentence, answerForBatch.getMemberSurveyId(), questionId, questionType, questionOptionId, localDateTime, constraintAnonymous);
                    } else {
                        throw new NoNecessaryAnswerException();
                    }
                } else {
                    setParameters(ps, answerSentence, answerForBatch.getMemberSurveyId(), questionId, questionType, questionOptionId, localDateTime,  constraintAnonymous);
                }
            }

            @Override
            public int getBatchSize() {
                return answerForBatchList.size();
            }
        });
    }

    private void setParameters(PreparedStatement ps, String answerSentence, Long memberSurveyId, Long questionId, Long questionType, Long questionOptionId, LocalDateTime localDateTime, Boolean constraintAnonymous) throws SQLException {
        ps.setString(1, answerSentence);
//        if (memberSurveyId == null) { // memberSurveyId = null 일경우 => type 이 그냥 anonymous 걸려있으면 검증하면됨.
//            ps.setNull(2, NULL);
//        } else ps.setLong(2, memberSurveyId);
        ps.setLong(2, memberSurveyId);
        ps.setLong(3, questionId);
        if (questionType != 1) { // 주관식이 아닐 때
            ps.setLong(4, questionOptionId);
        } else {
            ps.setNull(4, Types.NULL); // 주관식일 때
        }
        ps.setTimestamp(5, Timestamp.valueOf(localDateTime));
    }
}

