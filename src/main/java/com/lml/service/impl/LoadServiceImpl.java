package com.lml.service.impl;

import com.lml.converter.NodeConverter;
import com.lml.domain.*;
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
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private  JsonResult jsonResult;

    //缓存变量，用于Body的创建
    List<BodyEntity> cacheList = new ArrayList<>();

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
        jsonResult = accessDataUtils.getDataOffline(type, siteId.getSiteId());
        //站点节点
        List<SiteNode> siteNodes =  jsonResult.getSiteNodes();


        //todo 1.导入节点
        //站点关系
        List<SiteRelation> siteRelations = jsonResult.getSiteRelations();

        //导入body和instance节点
        if(type.equals("body")) {
            List<BodyEntity> bodyEntityList = NodeConverter.INSTANCE.bodyEntityListMapper(siteNodes);
            cacheList = bodyEntityList;
            //加载实体间关系
            setRelation(bodyEntityList,siteRelations, BodyEntity.class);
            List<BodyEntity> bodyList = bodyRepository.saveAll(bodyEntityList);
        }else{
            List<InstanceEntity> instanceEntityList = NodeConverter.INSTANCE.instanceEntityListMapper(siteNodes);
            //加载本体间关系
            setRelation(instanceEntityList,siteRelations, InstanceEntity.class);
            //加载实体-本体间关系,使用临时变量来
            setInsToBodyRelation(instanceEntityList,cacheList);
            List<InstanceEntity> instance = instanceRepository.saveAll(instanceEntityList);
        }


        for (SiteNode node : siteNodes) {
            //todo 2.导入和标签、标签组节点
            List<LabelCollectionDTO> labelCollectionDTOList = node.getLabelCollections();
            if (labelCollectionDTOList == null) break;
            for (LabelCollectionDTO labelCollectionDTO : labelCollectionDTOList) {
                List<LabelDTO> children = labelCollectionDTO.getChildren();
                List<LabelEntity> labelEntityList = NodeConverter.INSTANCE.labelEntityListMapper(children);
                //创建所有的标签
                neo4jTemplate.saveAll(labelEntityList);
            }
            List<LabelCollectionEntity> labelCollectionEntityList = NodeConverter.INSTANCE.labelCollectionEntityListMapper(labelCollectionDTOList);
            //创建所有的标签组节点
            neo4jTemplate.saveAll(labelCollectionEntityList);

            //todo 3.导入虚拟树
            List<VirtualTreeDTO> virtualTreeDTOList = node.getVirtualTreeDTOList();
            List<VirtualTreeEntity> virtualTreeEntityList = NodeConverter.INSTANCE.virtualTreeEntityListMapper(virtualTreeDTOList);
            neo4jTemplate.saveAll(virtualTreeEntityList);
        }
        return null;
    }

    private void setInsToBodyRelation(List<InstanceEntity> instanceEntityList,List<BodyEntity> bodyEntityList) {
        ArrayList<String> nodeIdList = new ArrayList<>();
        for (BodyEntity bodyEntity : bodyEntityList) {
            nodeIdList.add(bodyEntity.getNodeId());
        }
        //一个实体节点只对应一个本体节点
        for (InstanceEntity instanceEntity : instanceEntityList) {
            String bodySiteNodeId = instanceEntity.getBodySiteNodeId();
            int index = indexOf(nodeIdList, bodySiteNodeId);
            if(index != -1) {
                BodyEntity bodyEntity = bodyEntityList.get(index);
                instanceEntity.setIsInstance(bodyEntity);
            }
        }

    }

    private <T> void setRelation(List<T> nodeList, List<SiteRelation> siteRelations, Class entityType) {
        ArrayList<String> nodeIdList = new ArrayList<>();
        try {
            Method getNodeId = entityType.getDeclaredMethod("getNodeId");
            for (T t : nodeList) {
                Object invoke = getNodeId.invoke(t);
                nodeIdList.add((String) invoke);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (SiteRelation siteRelation : siteRelations) {
            int startIndex = -1;
            int endIndex = -1;
            if (entityType.equals(BodyEntity.class)) {
                String siteNodeId = siteRelation.getSiteNodeId();
                String assSimpleSN = siteRelation.getAssSimpleSN();
                startIndex = indexOf(nodeIdList, siteNodeId);
                endIndex = indexOf(nodeIdList, assSimpleSN);
                if (startIndex != -1 && endIndex != -1) {
                    BodyEntity start = (BodyEntity) nodeList.get(startIndex);
                    BodyEntity end = (BodyEntity) nodeList.get(endIndex);
                    start.addBelongTo(end);
                }
            } else {
                String insSiteNodeId = siteRelation.getSiteNodeId();
                String pid = siteRelation.getPid();
                startIndex = indexOf(nodeIdList, insSiteNodeId);
                endIndex = indexOf(nodeIdList, pid);
                if (startIndex != -1 && endIndex != -1) {
                    InstanceEntity start = (InstanceEntity) nodeList.get(startIndex);
                    InstanceEntity end = (InstanceEntity) nodeList.get(endIndex);
                    start.addBelongTo(end);
                }
            }
        }
    }



    private int indexOf(List<String> nodeIdList, String nodeId) {
        return  nodeIdList.indexOf(nodeId);
    }


}

