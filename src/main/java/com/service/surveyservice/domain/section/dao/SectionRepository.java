package com.service.surveyservice.domain.section.dao;

import com.service.surveyservice.domain.section.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(value = "SELECT s FROM Section s WHERE s.survey.id = :survey_id")
    List<Section> findBySurveyId(@Param("survey_id")Long survey_id);

    @Query(value = "SELECT s.id FROM Section s WHERE s.survey.id=:survey_id")
    List<Long> findIdBySurveyId(@Param("survey_id")Long survey_id);

    @Query(value = "SELECT qo.question_option_img FROM question_option as qo  WHERE qo.question_id in(\n" +
            "SELECT q.question_id FROM QUESTION as q WHERE q.section_id = 8) AND (\n" +
            "  qo.question_option_img IS NOT NULL AND qo.question_option_img <> '');",nativeQuery = true)
    List<String> findOptionImgBySectionId(Long section_id);

}
