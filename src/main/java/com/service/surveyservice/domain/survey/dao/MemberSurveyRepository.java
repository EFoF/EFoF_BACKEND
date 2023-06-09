package com.service.surveyservice.domain.survey.dao;


import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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
    Integer countParticipantBySurveyId(@Param("survey_id") Long survey_id);

//    Select count(*) from MemberSurvey ms where 3 in (SELECT ms.member_id FROM MemberSurvey ms WHERE ms.survey_id=1)
    // 설문 통계를 조회할 때, 통계 보기 constraint가 걸려있는 경우, 해당 설문에 참여한 사람인지 조회
    @Query(value = "Select count(*) from MemberSurvey ms where :member_id\n" +
            "        in (SELECT ms.member.id FROM MemberSurvey ms WHERE ms.survey.id= :survey_id)")
    int findMemberIdBySurveyId(@Param("survey_id") Long survey_id,@Param("member_id") Long member_id);

//    boolean existsByMemberAndSurvey(Member member, Survey survey); // findByMemberAndSurvey().isPresent 로 해결할 수 있을듯?


    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date " +
            "FROM survey s WHERE s.survey_id in (SELECT ms.survey_id FROM member_survey as ms WHERE ms.member_id= :userId)",
            countQuery = "SELECT COUNT(*) FROM survey s WHERE s.survey_id IN (SELECT ms.survey_id FROM member_survey ms WHERE ms.member_id = :userId)", nativeQuery = true)
    Page<MemberSurveyRepository.GetSurveyInterface> findMemberSurveyByMemberId(@org.springframework.data.repository.query.Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date " +
            "FROM survey s WHERE s.survey_id in (SELECT ms.survey_id FROM member_survey as ms WHERE ms.member_id= :userId)" +
            "and s.release_status='OVER' and open_date<CONVERT_TZ(NOW(), '+00:00', '+09:00') and CONVERT_TZ(NOW(), '+00:00', '+09:00')<expire_date",
            countQuery = "SELECT COUNT(*) FROM survey s WHERE s.survey_id IN (SELECT ms.survey_id FROM member_survey ms WHERE ms.member_id = :userId) and s.release_status='OVER'  and open_date<CONVERT_TZ(NOW(), '+00:00', '+09:00') and CONVERT_TZ(NOW(), '+00:00', '+09:00')<expire_date", nativeQuery = true)
    Page<MemberSurveyRepository.GetSurveyInterface> findMemberSurveyByMemberIdPro(@org.springframework.data.repository.query.Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date " +
            "FROM survey s WHERE s.survey_id in (SELECT ms.survey_id FROM member_survey as ms WHERE ms.member_id= :userId)" +
            "and s.release_status='OVER' and expire_date<CONVERT_TZ(NOW(), '+00:00', '+09:00')",
            countQuery = "SELECT COUNT(*) FROM survey s WHERE s.survey_id IN (SELECT ms.survey_id FROM member_survey ms WHERE ms.member_id = :userId) and s.release_status='OVER' and expire_date< CONVERT_TZ(NOW(), '+00:00', '+09:00')", nativeQuery = true)
    Page<MemberSurveyRepository.GetSurveyInterface> findMemberSurveyByMemberIdOver(@Param(value = "userId") Long userId, Pageable pageable);


    interface GetSurveyInterface {
        Long getSurvey_id();

        String getTitle();

        String getDescription();

        Long getMember_id();

        String getS_imageurl();

        LocalDateTime getOpen_date();

        LocalDateTime getExpire_date();

    }
}
