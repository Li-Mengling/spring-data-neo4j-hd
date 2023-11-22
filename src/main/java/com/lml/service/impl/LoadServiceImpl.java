package com.lml.service.impl;

import com.lml.converter.NodeConverter;
import com.lml.domain.*;
import com.lml.dto.*;
import com.lml.pojo.LoadResult;
import com.lml.dto.SiteIdDTO;
import com.lml.repository.BodyRepository;
import com.lml.repository.InstanceRepository;
import com.lml.service.LoadService;
import com.lml.utils.AccessDataUtils;
import com.lml.constant.NodeConstant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Leemonlin
 */
@Service
@Slf4j
public class LoadServiceImpl implements LoadService {

    private static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put(NodeConstant.BODY,"getBodyNodeAndRelation");
        TYPE_MAP.put(NodeConstant.INSTANCE,"getInstanceNodeAndRelation");
    }

    /**
     * json转换成java对象工具类
     */
    private final AccessDataUtils accessDataUtils;

    private  JsonResult jsonResult;

    //缓存变量，用于Body的创建
    List<BodyEntity> cacheList = new ArrayList<>();

    private final Neo4jTemplate neo4jTemplate;

    private final BodyRepository bodyRepository;


    private final InstanceRepository instanceRepository;


    public LoadServiceImpl(BodyRepository bodyRepository, InstanceRepository instanceRepository, AccessDataUtils accessDataUtils, Neo4jTemplate neo4jTemplate) {
        this.bodyRepository = bodyRepository;
        this.instanceRepository = instanceRepository;
        this.accessDataUtils = accessDataUtils;
        this.neo4jTemplate = neo4jTemplate;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoadResult load(String type, @NotNull SiteIdDTO siteIdDTO) {
        jsonResult = accessDataUtils.getDataOffline(type, siteIdDTO.getSiteId());
        //站点节点
        List<SiteNode> siteNodes =  jsonResult.getSiteNodes();


        //todo 1.导入节点
        //站点关系
        List<SiteRelation> siteRelations = jsonResult.getSiteRelations();

        //导入body和instance节点
        if(type.equals(NodeConstant.BODY)) {
            List<BodyEntity> bodyEntityList = NodeConverter.INSTANCE.bodyEntityListMapper(siteNodes);
            cacheList = bodyEntityList;
            //加载实体间关系
            setRelation(bodyEntityList,siteRelations, BodyEntity.class);
            List<BodyEntity> bodyList = bodyRepository.saveAll(bodyEntityList);
            log.info("导入实体节点:{}",bodyList.size());
        }else if(type.equals(NodeConstant.INSTANCE)){
            List<InstanceEntity> instanceEntityList = NodeConverter.INSTANCE.instanceEntityListMapper(siteNodes);
            //加载本体间关系
            setRelation(instanceEntityList,siteRelations, InstanceEntity.class);
            //加载实体-本体间关系,使用临时变量来
            setInsToBodyRelation(instanceEntityList,cacheList);
            List<InstanceEntity> instanceList = instanceRepository.saveAll(instanceEntityList);
            log.info("导入实体节点:{}",instanceList.size());
        }


        for (SiteNode node : siteNodes) {
            //todo 2.导入和标签、标签组节点
            List<LabelCollectionDTO> labelCollectionDTOList = node.getLabelCollections();
            if (labelCollectionDTOList == null) {
                break;
            }

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

    /**
     * 设置节点关系
     * @param siteRelations
     * @param entityType
     * @param <T>
     */
    private <T> void setRelation(List<T> nodeList, List<SiteRelation> siteRelations, Class entityType) {
        ArrayList<String> nodeIdList = new ArrayList<>();

        //获取节点id集合
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

