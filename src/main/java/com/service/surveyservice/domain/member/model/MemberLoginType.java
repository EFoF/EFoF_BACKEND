package com.service.surveyservice.domain.member.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MemberLoginType {
    DOKSEOL_LOGIN(1, "LOCAL"),
    GOOGLE_LOGIN(2, "GOOGLE");

    private long id;
    private String type;

    MemberLoginType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static MemberLoginType LabelToValue(String label) {
        return Arrays.stream(values()).filter(value -> value.type.equals(label)).findAny().orElse(null);
    }
}
