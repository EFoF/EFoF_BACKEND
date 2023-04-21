package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

public interface SurveyRepository extends JpaRepository<Survey, Long>, SurveyCustomRepository {

    @Query(value = "SELECT new com.service.surveyservice.domain.survey.dto.SurveyDTO.SurveyInfoDTO(s.title, s.description, s.author.id, s.sImageURL, s.surveyStatus)" +
            "FROM Survey s WHERE s.id = :id", nativeQuery = true)
    Optional<SurveyInfoDTO> findSurveyInfoDTOById(Long id);
}
