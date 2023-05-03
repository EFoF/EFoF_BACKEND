package com.service.surveyservice.domain.question.model;

import com.service.surveyservice.domain.section.model.Section;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class QuestionOption {

    @Id
    @Column(name = "questionChoice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionText;

    @ManyToOne
    @JoinColumn(name = "nextSection_id")
    private Section nextSection;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne
    @Nullable
    @JoinColumn(name = "questionOptionImg_id")
    private QuestionOptionImg questionOptionImg;


    public void setQuestionOptionImg(QuestionOptionImg questionOptionImg) {
        this.questionOptionImg = questionOptionImg;
    }

    public void setQuestionOptionText(String optionText) {
        this.optionText = optionText;
    }

    public void setQuestionOptionNextSection(Section nextSection) {
        this.nextSection = nextSection;
    }


}

