package com.service.surveyservice.domain.survey.model;

public enum SurveyStatus {
    MAKING(1, "making"), PRE_RELEASE(2, "prerelease"), PROGRESS(3, "progress"), OVER(4, "over");

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
