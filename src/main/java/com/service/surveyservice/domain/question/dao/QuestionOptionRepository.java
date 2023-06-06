package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionOptionRepository  extends JpaRepository<QuestionOption, Long> {

    @Modifying
    @Query(value = "UPDATE question_option as qo SET qo.next_section_id = null WHERE qo.next_section_id = :section_id"
            , nativeQuery = true)
    void updateNextSectionBySectionId(@Param("section_id")Long section_id);
}
