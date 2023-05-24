package com.service.surveyservice.domain.answer.dao;

import com.service.surveyservice.domain.answer.dto.AnswerDTO;
import com.service.surveyservice.domain.answer.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
    // questionOrderList <- [8, 7, 9]

    // 객관식
    @Query(value = "SELECT qo.question_id, aws.question_choice_id, aws.participant_num_question_option, qo.option_text,qo.question_option_img " +
            "FROM (SELECT question_choice_id, count(*)as participant_num_question_option FROM answer\n" +
            "WHERE question_id IN (select q.question_id from question q where q.section_id = :section_id)\n" +
            "group by question_choice_id) as aws left join question_option qo on aws.question_choice_id = qo.question_choice_id\n" +
            "where aws.question_choice_id IS NOT NULL;", nativeQuery = true)
    List<choiceAnswerResponseDtoI> findChoiceAnswerByQuestionId(Long section_id);
    interface choiceAnswerResponseDtoI {
        Long getQuestion_id();
        Long getQuestion_choice_id();
        int getParticipant_num_question_option();
        String getOption_text();
        String getQuestion_option_img();
    }

    // 주관식
    @Query(value = "SELECT a.question_id, a.answer_sentence\n" +
            "FROM answer a\n" +
            "JOIN question q ON a.question_id = q.question_id\n" +
            "WHERE q.section_id = :section_id\n" +
            "AND a.answer_sentence IS NOT NULL AND a.answer_sentence <> '';", nativeQuery = true)
    List<longAnswerResponseDtoI> findLongAnswerByQuestionId(Long section_id);
    interface longAnswerResponseDtoI {
        Long getQuestion_id();
        String getAnswer_sentence();
    }
}
