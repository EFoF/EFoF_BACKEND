package com.service.surveyservice.domain.answer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerDTO { //아직 구현 안 함
    private Long id;
    private Long question; // question_id
    private String memberSurvey; // 특정 사용자가 참여한 특정 설문
    private String questionOption; // 객관식 답변
    private String answerSentence; // 주관식 답변

    @Builder
    public AnswerDTO(Long id, Long question, String memberSurvey, String questionOption, String answerSentence) {
        this.id = id;
        this.question = question;
        this.memberSurvey = memberSurvey;
        this.questionOption = questionOption;
        this.answerSentence = answerSentence;
    }
}
