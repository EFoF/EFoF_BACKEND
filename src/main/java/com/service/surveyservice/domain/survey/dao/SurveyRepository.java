package com.service.surveyservice.domain.survey.dao;

import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.ReleaseStatus;
import com.service.surveyservice.domain.survey.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long>, SurveyCustomRepository {


    @Query(value = "SELECT qoi.img_url FROM question_option qo\n" +
            "INNER JOIN question_option_img qoi ON qo.question_option_img_id = qoi.question_option_img_id\n" +
            "WHERE qo.question_id IN (\n" +
            "  SELECT q.question_id FROM question q\n" +
            "  INNER JOIN section s ON q.section_id = s.section_id\n" +
            "  WHERE s.survey_id = :survey_id\n" +
            ") AND qoi.img_url IS NOT NULL;"
            , nativeQuery = true)
    List<String> findImgUrlBySurveyId(@Param("survey_id")Long survey_id);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date " +
            "FROM survey s WHERE s.member_id = :userId",
            countQuery = "SELECT count(*) FROM survey s WHERE s.member_id = :userId", nativeQuery = true)
    Page<SurveyRepository.GetSurveyInterface> findGenerateSurveyByAuthorId(@Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date, s.release_status\n" +
            "FROM survey s WHERE s.member_id = :userId and s.release_status='PRE_RELEASE'",
            countQuery = "SELECT count(*) FROM survey s WHERE s.member_id = :userId and s.release_status='PRE_RELEASE'", nativeQuery = true)
    Page<SurveyRepository.GetSurveyInterface> findGenerateSurveyByAuthorIdMake(@Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date, s.release_status\n" +
            "FROM survey s WHERE s.member_id = :userId and s.release_status='OVER' and s.open_date>NOW()",
            countQuery = "SELECT count(*) FROM survey s WHERE s.member_id = :userId and s.release_status='OVER' and s.open_date>NOW()", nativeQuery = true)
    Page<SurveyRepository.GetSurveyInterface> findGenerateSurveyByAuthorIdPre(@Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date, s.release_status\n" +
            "FROM survey s WHERE s.member_id = :userId and s.release_status='OVER' and s.open_date<NOW() and NOW()<s.expire_date",
            countQuery = "SELECT count(*) FROM survey s WHERE s.member_id = :userId and s.release_status='OVER' and s.open_date<NOW() and NOW()<s.expire_date", nativeQuery = true)
    Page<SurveyRepository.GetSurveyInterface> findGenerateSurveyByAuthorIdPro(@Param(value = "userId") Long userId, Pageable pageable);

    @Query(value = "SELECT s.survey_id, s.title, s.description, s.member_id, s.s_imageurl, s.open_date, s.expire_date, s.release_status\n" +
            "FROM survey s WHERE s.member_id = :userId and s.release_status='OVER' and s.expire_date<NOW()",
            countQuery = "SELECT count(*) FROM survey s WHERE s.member_id = :userId and s.release_status='OVER' and s.expire_date< NOW()", nativeQuery = true)
    Page<SurveyRepository.GetSurveyInterface> findGenerateSurveyByAuthorIdOver(@Param(value = "userId") Long userId, Pageable pageable);


    interface GetSurveyInterface {
        Long getSurvey_id();

        String getTitle();

        String getDescription();

        Long getMember_id();

        String getS_imageurl();

        LocalDateTime getOpen_date();

        LocalDateTime getExpire_date();

        ReleaseStatus getReleaseStatus();

    }
}
