package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.model.Question;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<Section> sectionList, SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto) {
        String sql = "INSERT INTO question (question_Text,question_Type,is_Necessary,section_id)  " +
                "VALUES (?,?,?,?)";

        for (int j=0; j<sectionList.size(); j++) {
            List<QuestionDTO.SaveQuestionRequestDto> questionList = saveSurveyRequestDto.getSections().get(j).getQuestionList();
            Section section = sectionList.get(j);
            jdbcTemplate.batchUpdate(sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Question question = questionList.get(i).toEntity(section);
                            ps.setString(1,question.getQuestionText());
                            ps.setString(2,question.getQuestionType().getName());
                            ps.setBoolean(3,question.isNecessary());
                            ps.setLong(4,section.getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return questionList.size();
                        }
                    });

        }


    }
}
