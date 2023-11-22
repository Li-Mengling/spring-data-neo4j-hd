package com.lml.controller;

import com.lml.constant.NodeConstant;
import com.lml.pojo.LoadResult;
import com.lml.pojo.Result;
import com.lml.dto.SiteIdDTO;
import com.lml.service.LoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 导入相关接口
 * @author Leemonlin
 */
@RestController
@Slf4j
public class LoadController {
    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    /**
     * 图数据库导入接口
     */
    @PostMapping("/loadItem")
    public Result load(@RequestBody SiteIdDTO siteIdDTO){
        log.info("导入程序正在运行...");
        log.info("导入站点：{}", siteIdDTO);
        LoadResult body = loadService.load(NodeConstant.BODY, siteIdDTO);
        LoadResult instance = loadService.load(NodeConstant.INSTANCE, siteIdDTO);
        return Result.success(body);
    }
}
