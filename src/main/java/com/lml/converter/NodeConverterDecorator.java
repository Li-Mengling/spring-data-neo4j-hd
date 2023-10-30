package com.lml.converter;

import com.lml.domain.BodyEntity;
import com.lml.domain.InstanceEntity;
import com.lml.dto.SiteNode;
import com.lml.dto.VirtualTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/30/17:01
 */


public class NodeConverterDecorator implements NodeConverter{

    private final NodeConverter nodeConverter;

    public NodeConverterDecorator(NodeConverter nodeConverter) {
        this.nodeConverter = nodeConverter;
    }

    @Override
    public BodyEntity bodyEntityMapper(SiteNode siteNode) {

        List<VirtualTree> virtualTreeList = siteNode.getVirtualTreeList();
        List<String> virtualTreeIdList = new ArrayList<>();
        if (virtualTreeList != null) {
            virtualTreeIdList = virtualTreeList.stream().
                    map(VirtualTree::getNodeId).
                    collect(Collectors.toList());
        }

        BodyEntity bodyEntity = nodeConverter.bodyEntityMapper(siteNode);
        bodyEntity.setVirtualTreeList(virtualTreeIdList);
        return bodyEntity;
    }

    @Override
    public InstanceEntity instanceEntityMapper(SiteNode siteNode) {

        List<VirtualTree> virtualTreeList = siteNode.getVirtualTreeList();
        List<String> virtualTreeIdList = new ArrayList<>();
        if (virtualTreeList != null) {
            virtualTreeIdList = virtualTreeList.stream().
                    map(VirtualTree::getNodeId).
                    collect(Collectors.toList());
        }
        InstanceEntity instanceEntity = nodeConverter.instanceEntityMapper(siteNode);
        instanceEntity.setVirtualTreeList(virtualTreeIdList);
        return instanceEntity;
    }


    }
