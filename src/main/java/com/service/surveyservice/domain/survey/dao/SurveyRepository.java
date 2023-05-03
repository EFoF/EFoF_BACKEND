package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

public interface SurveyRepository extends JpaRepository<Survey, Long>, SurveyCustomRepository {

    @Query(value = "SELECT new com.service.surveyservice.domain.survey.dto.SurveyDTO.SurveyInfoDTO(s.title, s.description, s.author.id, s.sImageURL, s.surveyStatus)" +
            "FROM Survey s WHERE s.id = :id", nativeQuery = true)
    Optional<SurveyInfoDTO> findSurveyInfoDTOById(Long id);

    @Query(value = "SELECT qoi.img_url FROM question_option qo\n" +
            "INNER JOIN question_option_img qoi ON qo.question_option_img_id = qoi.question_option_img_id\n" +
            "WHERE qo.question_id IN (\n" +
            "  SELECT q.question_id FROM question q\n" +
            "  INNER JOIN section s ON q.section_id = s.section_id\n" +
            "  WHERE s.survey_id = :survey_id\n" +
            ") AND qoi.img_url IS NOT NULL;"
            , nativeQuery = true)
    List<String> findImgUrlBySurveyId(@Param("survey_id")Long survey_id);
}
