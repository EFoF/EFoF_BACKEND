package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>{

    @Query(value = "SELECT qo.question_option_img FROM question_option as qo  WHERE qo.question_id = :question_id AND (\n" +
            "  qo.question_option_img IS NOT NULL AND qo.question_option_img <> '');"
    , nativeQuery = true)
    List<String> findImgUrlByQuestionId(@Param("question_id")Long question_id);
}
