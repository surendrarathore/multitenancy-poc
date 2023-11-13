package com.multitenancy1.controller;

import com.multitenancy1.config.db.DataSourceContextHolder;
import com.multitenancy1.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RequestMapping("/home")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final AppService appService;
    @GetMapping
    public List<String> getDataList(){
        DataSourceContextHolder.setDataSourceKey("CONSUMER");
        return appService.findAll();
    }
}
