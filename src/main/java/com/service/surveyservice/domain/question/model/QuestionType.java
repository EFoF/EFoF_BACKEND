package com.service.surveyservice.domain.question.model;

import lombok.Getter;
import lombok.Setter;

@Getter

public enum QuestionType {

    ONE_CHOICE(0,"one_choice"),
    LONG_ANSWER(1,"long_answer"),

    MULTIPLE_CHOICE(2,"multiple_choice"),
    TRUE_FALSE(3,"true_false"),
    FILE_ANSWER(4,"file_answer");

    private long id;
    private String name;

    QuestionType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static QuestionType fromId(long id) {
        for (QuestionType type : QuestionType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid QuestionType id: " + id);
    }
}
