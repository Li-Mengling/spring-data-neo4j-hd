package com.lml.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Description: 将json对象转换成java对象
 * @Author: leemonlin
 * @Date: 2023/10/29/12:46
 */

@Data
public class JsonResult {
    @JSONField(name = "siteNodeList")
    private List<SiteNode> siteNodes;

    @JSONField(name = "snRelationList")
    private List<SiteRelation> siteRelations;

}
