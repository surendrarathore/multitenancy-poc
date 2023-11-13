package com.multitenancy1.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
public class TenantRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceKey();
    }
}
