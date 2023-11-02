package com.lml.dto;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *LabelDTO
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/31/10:15
 */

@Data
public class LabelDTO {

    private String id;

    private String name;

    private String dataType;

    private String type;

    private String remark;

    private Object rule;

    private String enumerate;

    private String icon;

    private List<String> classificationIds;

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
