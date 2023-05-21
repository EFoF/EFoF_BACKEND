package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.dto.QuestionOptionDTO;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class QuestionOptionCustomRepositoryImpl implements QuestionOptionCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<String> optionList, Long question_id) {

        String sql = "INSERT INTO question_option (option_text,question_id)  " +
                "VALUES (?,?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1,optionList.get(i));
                        ps.setLong(2,question_id);
                    }

                    @Override
                    public int getBatchSize() {
                        return optionList.size();
                    }
                });

    }

}
