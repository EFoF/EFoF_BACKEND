package com.service.surveyservice.domain.survey.dao;


import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.survey.model.Survey;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import static com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.*;

public interface MemberSurveyRepository extends JpaRepository<MemberSurvey, Long>, MemberSurveyCustomRepository {

    Optional<MemberSurvey> findById(Long id);
    @Query(value = "SELECT new com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.MemberSurveyInfoDTO(ms.id, ms.member.id, ms.survey.id)" +
            "FROM MemberSurvey ms WHERE ms.member.id = :id", nativeQuery = true)
    List<MemberSurveyInfoDTO> findByMemberId(Long id);
    List<MemberSurvey> findBySurveyId(Long id);

    Optional<MemberSurvey> findByMemberAndSurvey(Member member, Survey survey);


    @Query(value = "SELECT count(*) FROM MemberSurvey ms WHERE ms.survey.id=:survey_id")
    Integer countParticipantBySurveyId(Long survey_id);
}
