package com.service.surveyservice.domain.survey.dao;


import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
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

    @Query(value = "SELECT count(*) FROM MemberSurvey ms WHERE ms.survey.id=:survey_id")
    Integer countParticipantBySurveyId(Long survey_id);

//    Select count(*) from MemberSurvey ms where 3 in (SELECT ms.member_id FROM MemberSurvey ms WHERE ms.survey_id=1)
    // 설문 통계를 조회할 때, 통계 보기 constraint가 걸려있는 경우, 해당 설문에 참여한 사람인지 조회
    @Query(value = "Select count(*) from MemberSurvey ms where :member_id\n" +
            "        in (SELECT ms.member.id FROM MemberSurvey ms WHERE ms.survey.id= :survey_id)")
    int findMemberIdBySurveyId(Long survey_id, Long member_id);
}
