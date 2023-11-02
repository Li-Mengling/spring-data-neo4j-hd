package com.lml.converter;

import com.lml.domain.*;
import com.lml.dto.LabelCollectionDTO;
import com.lml.dto.LabelDTO;
import com.lml.dto.SiteNode;
import com.lml.dto.VirtualTreeDTO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * DTO对象到DO对象转换的实现类
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
        //获取虚拟树List对象和标签组List对象
        List<VirtualTreeDTO> virtualTreeDTOList = siteNode.getVirtualTreeDTOList();
        List<LabelCollectionDTO> labelCollectionDTOList = siteNode.getLabelCollections();

        //转换
        List<String> virtualTreeIdList = convertToList(virtualTreeDTOList, VirtualTreeDTO.class);
        List<String> labelCollectionIdList = convertToList(labelCollectionDTOList, LabelCollectionDTO.class);

        BodyEntity bodyEntity = nodeConverter.bodyEntityMapper(siteNode);

        //set
        bodyEntity.setVirtualTreeList(virtualTreeIdList);
        bodyEntity.setLabelCollectionList(labelCollectionIdList);
        return bodyEntity;
    }

    @Override
    public List<BodyEntity> bodyEntityListMapper(List<SiteNode> siteNodeList) {
        return siteNodeList.stream().
                map(this::bodyEntityMapper).
                collect(Collectors.toList());
    }

    @Override
    public InstanceEntity instanceEntityMapper(SiteNode siteNode) {
        //获取虚拟树List对象和标签组List对象
        List<VirtualTreeDTO> virtualTreeDTOList = siteNode.getVirtualTreeDTOList();
        List<LabelCollectionDTO> labelCollectionDTOList = siteNode.getLabelCollections();

        //转换
        List<String> virtualTreeIdList = convertToList(virtualTreeDTOList, VirtualTreeDTO.class);
        List<String> labelCollectionIdList = convertToList(labelCollectionDTOList, LabelCollectionDTO.class);

        InstanceEntity instanceEntity = nodeConverter.instanceEntityMapper(siteNode);

        instanceEntity.setVirtualTreeList(virtualTreeIdList);
        instanceEntity.setLabelCollectionList(labelCollectionIdList);
        return instanceEntity;
    }

    @Override
    public List<InstanceEntity> instanceEntityListMapper(List<SiteNode> siteNodeList) {
        return siteNodeList.stream().
                map(this::instanceEntityMapper).
                collect(Collectors.toList());
    }

    @Override
    public LabelCollectionEntity labelCollectionEntityMapper(LabelCollectionDTO labelCollectionDTO) {
        return nodeConverter.labelCollectionEntityMapper(labelCollectionDTO);
    }

    @Override
    public List<LabelCollectionEntity> labelCollectionEntityListMapper(List<LabelCollectionDTO> labelCollectionDTOList) {
        return labelCollectionDTOList.stream().
                map(this::labelCollectionEntityMapper).
                collect(Collectors.toList());
    }

    @Override
    public LabelEntity labelEntityMapper(LabelDTO labelDTO) {
        return nodeConverter.labelEntityMapper(labelDTO);
    }

    @Override
    public List<LabelEntity> labelEntityListMapper(List<LabelDTO> labelDTOList) {
        return labelDTOList.stream().
                map(this::labelEntityMapper).
                collect(Collectors.toList());
    }

    @Override
    public VirtualTreeEntity virtualTreeEntityMapper(VirtualTreeDTO virtualTreeDTO) {
        return nodeConverter.virtualTreeEntityMapper(virtualTreeDTO);
    }

    @Override
    public List<VirtualTreeEntity> virtualTreeEntityListMapper(List<VirtualTreeDTO> virtualTreeDTOList) {
        return virtualTreeDTOList != null?virtualTreeDTOList.stream().
                map(this::virtualTreeEntityMapper).
                collect(Collectors.toList()) :
                new ArrayList<>();
    }

    private <T> List<String> convertToList(List<T> extraNodeList, Class<T> nodeType) {
        Method getNodeId = null;
        try {
            getNodeId = nodeType.getMethod("getNodeId");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        List<String> idList = new ArrayList<>();
        if (extraNodeList != null) {
            for (T node : extraNodeList) {
                String id;
                try {
                    id = (String) getNodeId.invoke(node);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                idList.add(id);
            }
        }
        return idList;
    }
}

