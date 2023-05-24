package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>{

    @Query(value = "SELECT qo.question_option_img FROM question_option as qo  WHERE qo.question_id = :question_id AND (\n" +
            "  qo.question_option_img IS NOT NULL AND qo.question_option_img <> '');"
    , nativeQuery = true)
    List<String> findImgUrlByQuestionId(@Param("question_id")Long question_id);

    //    section_id에 대한 question info 리스트
    @Query(value = "select q.question_id,q.question_text,q.question_type," +
            "(select count(*) from answer where answer.question_id = q.question_id) as participant_num_question " +
            "from question q where q.section_id = :section_id", nativeQuery = true)
    List<questionInfoByIdDtoI> findQuestionInfoById(@Param("section_id") Long section_id);

    interface questionInfoByIdDtoI {
        Long getQuestion_id();

        String getQuestion_text();

         QuestionType getQuestion_type();

        int getParticipant_num_question();
    }


}
