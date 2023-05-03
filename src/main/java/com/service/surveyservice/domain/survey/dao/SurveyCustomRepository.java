package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

public interface SurveyCustomRepository {

    Page<SurveyInfoDTO> findSurveyInfoDTOByAuthorId(Long authorId, Pageable pageable);


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
