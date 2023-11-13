package com.multitenancy1.config.repo;

import com.multitenancy1.pojo.DbTenants;
import org.springframework.data.repository.ListCrudRepository;
public interface DbTenantsRepository extends ListCrudRepository<DbTenants,String> {
}
