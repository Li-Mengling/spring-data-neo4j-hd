package com.lml.dto;

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
public class SiteRelation {
    private String id;
    private String name;
    private String relationType;
    private List<String> labelList;
    private String assSimpleSN;
    private String assSimpleSNName;
    private List<String> assStaticSNList;
    private List<String> assStaticSNNameList;
    private List<String> assStaticSNRList;
    private String siteNodeId;
    private String siteNodeName;
    private List<String> disableSNList;
    private String type;
    private String siteId;
    private List<String> quoteRelationId;
    private String createTime;
    private String updateTime;
    private List<String> treeId;
    private String treeName;
    private Boolean isConnectedStructured;
    private Boolean isDimension;
    private List<String> structureList;
}
