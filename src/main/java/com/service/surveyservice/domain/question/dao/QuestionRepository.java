package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>{

    @Query(value = "select img_url from\n" +
            "(select  from question_option as qo where qo.question_id = :question_id)as qo_List\n" +
            "             right outer join question_option_img as qoi ON qo_List.question_option_img_id = qoi.question_option_img_id;"
    , nativeQuery = true)
    List<String> findImgUrlByQuestionId(@Param("question_id")Long question_id);
}
