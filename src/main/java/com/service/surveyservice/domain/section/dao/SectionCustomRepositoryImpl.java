package com.service.surveyservice.domain.section.dao;

import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SectionCustomRepositoryImpl implements SectionCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<SectionDTO.SaveSectionRequestDto> sections, Survey survey) {

        String sql = "INSERT INTO section (survey_id)  " +
                "VALUES (?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1,survey.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return sections.size();
                    }
                });

    }


}
