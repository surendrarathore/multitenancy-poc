package com.multitenancy1.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppDao {
    private final EntityManager entityManager;

    public AppDao(@Qualifier("tenantEntityManagerFactory")  EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Tuple> findAll(){
      return entityManager.createNativeQuery("select schema_name as priority from information_schema.schemata",Tuple.class).getResultList();
    }

}
