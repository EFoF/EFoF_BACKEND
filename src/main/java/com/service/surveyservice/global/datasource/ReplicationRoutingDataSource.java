package com.service.surveyservice.global.datasource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
    private final DataSourceKey dataSourceKey;

    /**
     * 현재 요청에서 사용할 DataSource 결정할 key 값 반환
     */
    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            log.info("Connection Slave");
            return dataSourceKey.getSlaveKey();
        } else {
            log.info("Connection Master");
            return dataSourceKey.getMasterKey();
        }
    }
}
