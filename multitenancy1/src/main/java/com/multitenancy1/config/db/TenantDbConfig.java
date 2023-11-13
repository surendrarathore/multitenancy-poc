package com.multitenancy1.config.db;

import com.multitenancy1.config.repo.DbTenantsRepository;
import com.multitenancy1.pojo.DbTenants;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager",
        basePackages = {"com.multitenancy1.pojo" ,"com.multitenancy1.service","com.multitenancy1.dao"})
public class TenantDbConfig {

    @Autowired
    private DbTenantsRepository dbTenantsRepository;

    @Bean("dataSourceTenant")
    public DataSource dataSourceTenant(){
        var allDataSourceMap = getDbDataSourceMap();
        var tenantRoutingDataSource = new TenantRoutingDataSource();
        tenantRoutingDataSource.setTargetDataSources(allDataSourceMap);
        tenantRoutingDataSource.setDefaultTargetDataSource(allDataSourceMap.get("CORE"));
        tenantRoutingDataSource.setDataSourceLookup(dataSourceName->{
            var lookUpDataSourceMap =  getDbDataSourceMap();
            if(lookUpDataSourceMap.containsKey(dataSourceName)){
                return (DataSource) lookUpDataSourceMap.get(dataSourceName);
            }else{
                throw new RuntimeException("DataSource not found for "+dataSourceName);
            }
        });

        return tenantRoutingDataSource;
    }

    private Map<Object, Object> getDbDataSourceMap() {
        var allDbList =  dbTenantsRepository.findAll();
        return allDbList.stream().collect(Collectors.toMap(DbTenants::getTenantId, v-> DataSourceBuilder.create().url(v.getDbUrl()).username(v.getDbUsername()).password(v.getDbPassword()).build()));
    }

    @Bean("tenantEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder tenantEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }
    @Bean("tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(@Qualifier("tenantEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("dataSourceTenant") DataSource dataSourceTenant){
        return  builder.dataSource(dataSourceTenant).packages("com.multitenancy1.*").build();
    }
    @Bean("tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
