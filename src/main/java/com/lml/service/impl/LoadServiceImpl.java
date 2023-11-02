package com.lml.service.impl;

import com.lml.converter.NodeConverter;
import com.lml.domain.BodyEntity;
import com.lml.domain.InstanceEntity;
import com.lml.domain.LabelCollectionEntity;
import com.lml.domain.LabelEntity;
import com.lml.dto.*;
import com.lml.pojo.LoadResult;
import com.lml.pojo.SiteId;
import com.lml.repository.BodyRepository;
import com.lml.repository.InstanceRepository;
import com.lml.service.LoadService;
import com.lml.service.ToCsvService;
import com.lml.utils.AccessDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class LoadServiceImpl implements LoadService {

    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("body","getBodyNodeAndRelation");
        typeMap.put("instance","getInstanceNodeAndRelation");
    }

    //json转换成java对象工具类
    private final AccessDataUtils accessDataUtils;

    private final Neo4jTemplate neo4jTemplate;

    private final BodyRepository bodyRepository;


    private final InstanceRepository instanceRepository;

    private final ToCsvService toCsvService;

    public LoadServiceImpl(ToCsvService toCsvService, BodyRepository bodyRepository, InstanceRepository instanceRepository, AccessDataUtils accessDataUtils, Neo4jTemplate neo4jTemplate) {
        this.toCsvService = toCsvService;
        this.bodyRepository = bodyRepository;
        this.instanceRepository = instanceRepository;
        this.accessDataUtils = accessDataUtils;
        this.neo4jTemplate = neo4jTemplate;
    }


    @Override
    @Transactional //开启事务，保证导入要么全部执行，要么全部不执行
    public LoadResult load(String type, @NotNull SiteId siteId) {
        JsonResult jsonResult = accessDataUtils.getDataOffline(type, siteId.getSiteId());
        List<SiteNode> siteNodes =  jsonResult.getSiteNodes();
        for (SiteNode node : siteNodes) {
            if(type.equals("body")) {
                BodyEntity bodyEntity = NodeConverter.INSTANCE.bodyEntityMapper(node);
                BodyEntity body = bodyRepository.save(bodyEntity);

            }else{
                InstanceEntity instanceEntity = NodeConverter.INSTANCE.instanceEntityMapper(node);
                InstanceEntity instance = instanceRepository.save(instanceEntity);
            }
            //todo 1.导入和标签、标签组节点
            List<LabelCollectionDTO> labelCollectionDTOList = node.getLabelCollections();

            for (LabelCollectionDTO labelCollectionDTO : labelCollectionDTOList) {
                List<LabelDTO> children = labelCollectionDTO.getChildren();
                List<LabelEntity> labelEntityList = NodeConverter.INSTANCE.labelEntityListMapper(children);
                //创建所有的标签
                neo4jTemplate.saveAll(labelEntityList);
            }
            List<LabelCollectionEntity> labelCollectionEntityList = NodeConverter.INSTANCE.labelCollectionEntityListMapper(labelCollectionDTOList);
            //创建所有的标签组节点
            neo4jTemplate.saveAll(labelCollectionEntityList);

            //todo 2.导入虚拟树
            node.getVirtualTreeList();

        }

        //todo 2.导入节点之间的关系
        return null;
    }



    private <T> void TemplateLoad(T node) {
        neo4jTemplate.save(node);
    }
}

