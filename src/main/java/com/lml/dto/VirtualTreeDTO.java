package com.lml.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

@Data
public class VirtualTreeDTO {

    @Id
    @JSONField(name = "id")
    private String nodeId;

    @Property
    private String nodeName;
    private String virtualTreeType;
    private String type;
    private int snType;
    private String siteId;
    private String lastTreeId;
    private String remark;
    private String createBy;
    private String updateBy;
    private String createTime;
    private String updateTime;
}
