package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
    @Query(value = "select a.question_id, a.answer_sentence from answer a" +
            "where a.answer_sentence is not null and a.answer_sentence <> ''", nativeQuery = true)
    List<AnswerDTO.LongAnswerDto> findLongAnswerByQuestionId(List<Long> questionOrderList);

    @Query(value = "SELECT aws.question_choice_id,aws.sum,qo.option_text,qo.question_option_img,qo.question_id FROM (SELECT\n" +
            "    question_choice_id, count(*)as sum FROM answer\n" +
            "WHERE question_id IN (7, 8, 9)\n" +
            "group by question_choice_id) as aws left join question_option qo on aws.question_choice_id = qo.question_choice_id\n" +
            "where aws.question_choice_id IS NOT NULL;", nativeQuery = true)
    List<AnswerDTO.ChoiceAnswerDto> findChoiceAnswerByQuestionId(List<Long> questionOrderList);

}
