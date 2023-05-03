package com.service.surveyservice.domain.question.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionImg {

    @Id
    @Column(name = "questionOptionImg_id")
    @GeneratedValue
    private Long id;

    // QuestionOptionImg 에서는 QuestionOption 필드를 가지고 있을 이유가 없어보여서 단방향으로 설정하겠다.
    private String imgUrl;

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }


}
