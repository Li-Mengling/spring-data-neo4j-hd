package com.lml.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/31/10:08
 */

@Data
public class LabelCollectionDTO {
    @JSONField(name = "id")
    private String nodeId;


    private String name;

    private List<String> labelIds;

    private String type;

    private String remark;

    private List<String> classificationIds;

    private String organizationId;


    private List<LabelDTO> children;

    private String createBy;

    private String updateBy;

    private String createTime;

    private String updateTime;

}
