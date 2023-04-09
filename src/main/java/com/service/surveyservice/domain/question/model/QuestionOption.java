package com.service.surveyservice.domain.question.model;

import com.service.surveyservice.domain.section.model.Section;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {
    @Id
    @Column(name = "question_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long index;

    private String optionText;

    @OneToOne
    @JoinColumn
    private Section nextSection;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne
    @JoinColumn
    private QuestionOptionImg questionOptionImg;


}

