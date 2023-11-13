package com.multitenancy1.service.impl;

import com.multitenancy1.dao.AppDao;
import com.multitenancy1.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppDao appDao;
    @Transactional("tenantTransactionManager")
    @Override
    public List<String> findAll() {
        return appDao.findAll().stream().map(m->m.get("priority", String.class)).toList();
    }
}
