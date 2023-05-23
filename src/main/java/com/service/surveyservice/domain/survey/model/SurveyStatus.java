package com.service.surveyservice.domain.survey.model;

public enum SurveyStatus {
    PRE_LELEASE(1, "설문 배포 전"), PROGRESS(2, "설문 진행 중"), OVER(3, "설문 마감");

    private final long id;

    private final String name;

    SurveyStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SurveyStatus findById(Long id) {
        for(SurveyStatus status : SurveyStatus.values()) {
            if(status.getId().equals(id))
                return status;
        }
        throw new IllegalArgumentException("SurveyStatus에 해당하지 않는 id값");
    }
}
