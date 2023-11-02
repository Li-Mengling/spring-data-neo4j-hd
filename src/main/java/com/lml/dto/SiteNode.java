package com.lml.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/29/12:59
 */

@Data
public class SiteNode {
    @JSONField(name = "id")
    private String nodeId;

    private String siteId;

    private String siteNodeId;

    private String nodeName;

    private String bodySiteNodeId;

    private String attribute;

    private String type;

    private String lastSiteNodeId;

    private int snType;

    private boolean isCreatedSite;

    private List<LabelCollectionDTO> labelCollections;

    private String instanceType;

    @JSONField(name = "virtualTreeList")
    private List<VirtualTreeDTO> virtualTreeDTOList;

    private List<String> structureList;

    private String fileType;

    private List<String> virtualRootList;
}
