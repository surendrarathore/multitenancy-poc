package com.multitenancy1.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name="DbTenants")
@Table(name="db_tenants",schema = "public")
public class DbTenants {

    @Id
    @Column(name="tenant_id")
    private String tenantId;

    @Column(name="db_name")
    private String dbName;

    @Column(name="db_url")
    private String dbUrl;

    @Column(name="db_username")
    private String dbUsername;

    @Column(name="db_password")
    private String dbPassword;
}
