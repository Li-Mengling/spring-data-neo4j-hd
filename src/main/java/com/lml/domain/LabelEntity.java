package com.lml.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/31/12:46
 */

@Data
@Node("label")
public class LabelEntity {
    @Id
    private String id;

    @Property("nodeName")
    private String nodeName;

    @Property("dataType")
    private String dataType;

    @Property("type")
    private String type;

    @Property("classificationIds")
    private List<String> classificationIds;

    @Property("organizationId")
    private String organizationId;

    
    private String createBy;

    private String updateBy;

    private String createTime;

    private String updateTime;

    private Object associationClassification;

    private boolean formItemFlag;

    private Object options;

    private String labelIdPath;

    private String value;

    private String pid;

    private String labelCollectionId;

    private String dataIndex;

}
