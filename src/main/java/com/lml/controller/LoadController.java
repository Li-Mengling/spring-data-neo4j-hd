package com.lml.controller;

import com.alibaba.fastjson.JSONObject;
import com.lml.pojo.LoadResult;
import com.lml.pojo.Result;
import com.lml.pojo.SiteId;
import com.lml.service.LoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j

public class LoadController {
    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    //图数据库导入接口
    @PostMapping("/loadItem")
    public Result load(@RequestBody SiteId siteId){
        log.info("导入程序正在运行...");
        log.info("导入站点：{}",siteId);
        LoadResult body = loadService.load("body", siteId);
        LoadResult instance = loadService.load("instance", siteId);
        return Result.success(body);
    }
}
