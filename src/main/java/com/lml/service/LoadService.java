package com.lml.service;

import com.lml.pojo.LoadResult;
import com.lml.dto.SiteIdDTO;

public interface LoadService {
     /**
     * @description
     *1.获取站点数据
     *2.处理成相应对象
     *3.导入neo4j
     * @Param type: 导入类型
     * @Param siteID: 导入站点id
     * @return com.lml.pojo.LoadResult
     * @author: Leemonlin
     * @date: 2023/10/28 13:28
     */
      LoadResult load(String type, SiteIdDTO siteIdDTO);

}
