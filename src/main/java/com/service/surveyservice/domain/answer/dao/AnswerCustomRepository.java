package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerCustomRepository {
//    List<SurveyDTO.SurveyInfoDTO> findSurveyInfoDTOBySurveyIdQuery(Long surveyId);
    void saveAll(List<AnswerDTO.AnswerForBatch> answerForBatchList);
}
