package com.lml.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lml.domain.BodyEntity;
import com.lml.domain.InstanceEntity;
import com.lml.domain.LabelCollectionEntity;
import com.lml.domain.LabelEntity;
import com.lml.dto.LabelCollectionDTO;
import com.lml.dto.LabelDTO;
import com.lml.dto.SiteNode;
import com.lml.dto.VirtualTree;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
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
        List<VirtualTree> virtualTreeList = siteNode.getVirtualTreeList();
        List<LabelCollectionDTO> labelCollectionDTOList = siteNode.getLabelCollections();

        //转换
        List<String> virtualTreeIdList = convertToList(virtualTreeList, VirtualTree.class);
        List<String> labelCollectionIdList = convertToList(labelCollectionDTOList, LabelCollectionDTO.class);

        BodyEntity bodyEntity = nodeConverter.bodyEntityMapper(siteNode);

        //set
        bodyEntity.setVirtualTreeList(virtualTreeIdList);
        bodyEntity.setLabelCollectionList(labelCollectionIdList);
        return bodyEntity;
    }

    @Override
    public InstanceEntity instanceEntityMapper(SiteNode siteNode) {
        //获取虚拟树List对象和标签组List对象
        List<VirtualTree> virtualTreeList = siteNode.getVirtualTreeList();
        List<LabelCollectionDTO> labelCollectionDTOList = siteNode.getLabelCollections();

        //转换
        List<String> virtualTreeIdList = convertToList(virtualTreeList, VirtualTree.class);
        List<String> labelCollectionIdList = convertToList(labelCollectionDTOList, LabelCollectionDTO.class);

        InstanceEntity instanceEntity = nodeConverter.instanceEntityMapper(siteNode);

        instanceEntity.setVirtualTreeList(virtualTreeIdList);
        instanceEntity.setLabelCollectionList(labelCollectionIdList);
        return instanceEntity;
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

