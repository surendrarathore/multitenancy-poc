package com.multitenancy1.config.db;

import com.multitenancy1.config.repo.DbTenantsRepository;
import com.multitenancy1.pojo.DbTenants;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "coreEntityManagerFactory",
        transactionManagerRef = "coreTransactionManager",
        basePackages = {"com.multitenancy1.pojo" ,"com.multitenancy1.config.repo"})
public class CoreDbConfig {
    @Value("${spring.datasource.db1.url}")
    private String dbUrl;
    @Value("${spring.datasource.db1.username}")
    private String dbUserName;
    @Value("${spring.datasource.db1.password}")
    private String dbUserPassword;

    @Bean("dataSourceCore")
    public DataSource dataSourceCore(){
        return  DataSourceBuilder.create().url(dbUrl).username(dbUserName).password(dbUserPassword).build();
    }

    @Bean("coreEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder coreEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), Map.of("hibernate.show_sql",true), null);
    }

    @Bean("coreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory(@Qualifier("coreEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("dataSourceCore") DataSource dataSourceCore){
        return  builder.dataSource(dataSourceCore).packages(DbTenants.class.getName(), DbTenantsRepository.class.getName(),"com.multitenancy1.pojo").persistenceUnit("core").build();
    }

    @Bean("coreTransactionManager")
    public PlatformTransactionManager coreTransactionManager(@Qualifier("coreEntityManagerFactory") EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
