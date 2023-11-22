package com.lml.controller;

import com.lml.pojo.Result;
import com.lml.dto.SiteIdDTO;
import com.lml.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *查询
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/11/03/10:14
 */
@RestController
@Slf4j
@RequestMapping("/query")
public class QueryController {

    private QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("/count")
    public Result count(@RequestBody SiteIdDTO siteIdDTO){
        log.info("节点个数统计");
        long count = queryService.countBySiteId(siteIdDTO);
        return Result.success(count);
    }
}
