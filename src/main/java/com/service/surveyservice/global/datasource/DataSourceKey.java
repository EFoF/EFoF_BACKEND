package com.service.surveyservice.global.datasource;

import org.springframework.stereotype.Component;

@Component
public class DataSourceKey {
    private static final String MASTER_KEY = "master";
    private static final String SLAVE_KEY = "slave";

    public String getMasterKey() {
        return MASTER_KEY;
    }

    public String getSlaveKey() {
        return SLAVE_KEY;
    }
}
