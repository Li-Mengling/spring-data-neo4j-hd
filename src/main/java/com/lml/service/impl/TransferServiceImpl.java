package com.lml.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.lml.domain.BodyEntity;
import com.lml.domain.InstanceEntity;
import com.lml.domain.LabelCollectionEntity;
import com.lml.domain.VirtualTreeEntity;
import com.lml.dto.BodyToInsRelationDTO;
import com.lml.dto.JsonResult;
import com.lml.dto.NodeAndRelationList;
import com.lml.pojo.SiteId;
import com.lml.service.TransferService;
import com.lml.utils.AccessDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final AccessDataUtils accessDataUtils;
    private final boolean IS_TREE = true;

    public TransferServiceImpl( AccessDataUtils accessDataUtils) {
        this.accessDataUtils = accessDataUtils;
    }

    /**
     * 将对应数据转换成PO对象
     *
     * @param type
     * @param siteId
     * @return
     */
    public NodeAndRelationList getData(String type, SiteId siteId) {
        String id = siteId.getSiteId();
        JsonResult jsonData = accessDataUtils.getDataOffline(type, id);
        return null;
    }


    /**
     * @description 用于生成BodyEntity或InstanceEntity对象
     * @Param node: 单个node，json对象
     * @Param returnType: 需要生成对象的class对象
     * @return T 生成的节点对象
     * @author: Leemonlin
     * @date: 2023/10/28 21:59
    */
    private <T> T buildEntity(JSONObject node,Class<T> returnType){

        String nodeId = node.getString("siteNodeId");

        String siteID = node.getString("siteId");

        String nodeName  = node.getString("nodeName");

        String type = node.getString("type");

        String lastSiteNodeId = node.getString("lastSiteNodeId");

        Integer snType = node.getInteger("snType");

        String fileType  = node.getString("fileType");;


        JSONArray labelCollections1 = node.getJSONArray("labelCollections");
        //将jsonArray对象转换成List对象，同时对于每一个表结构还要对应的生成表结构节点对象
        List<String> labelCollections = convertToList(labelCollections1,!IS_TREE);

        JSONArray virtualTreeList1 = node.getJSONArray("virtualTreeList");
        //将jsonArray对象转换成List对象，同时对于每一个虚拟树还要对应的生成虚拟树节点对象
        List<String> virtualTreeList = convertToList(virtualTreeList1,IS_TREE);

        JSONArray structureList1 = node.getJSONArray("structureList");
        List<String> structureList = structureList1==null?null:structureList1.toJavaList(String.class);
        //通过反射动态创建实例
        try {
            Constructor<T> constructor = returnType.getConstructor(
                    String.class, String.class, String.class,
                    String.class, String.class, Integer.class,
                    String.class, List.class, List.class, List.class);
            return constructor.newInstance(
                    nodeId, siteID,
                    nodeName, type,
                    lastSiteNodeId, snType,
                    fileType, labelCollections,
                    virtualTreeList, structureList
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   private BodyToInsRelationDTO buildRelation(JSONObject node){
       String startID = node.getString("siteNodeId");

       String endId = node.getString("bodySiteNodeId");

       return new BodyToInsRelationDTO(startID,endId);
   }

    public LabelCollectionEntity buildLabelCollection(JSONObject labelCollectionObject, String nodeId) {
        //labelCollectionObject.getString()
        return null;
    }



    public VirtualTreeEntity buildVirtualTree(JSONObject virtualTreeObject, String nodeId) {
        //虚拟树id
        String id = virtualTreeObject.getString("id");
        //todo
        return null;
    }

    /**
     * @description
     * 用于将JSONArray类型的虚拟树json数据和表结构json数据转换成对应的IdList
     * @Param jsonArray:
     * @return java.util.List<java.lang.String>
     * @author: Leemonlin
     * @date: 2023/10/28 21:29
    */
    private List<String> convertToList(JSONArray jsonArray,boolean is_tree){
        List<String> list = new ArrayList<>();
        if(jsonArray != null){
            for (Object item : jsonArray) {
                JSONObject itemObject = (JSONObject) item;
                String id = itemObject.getString("id");
                Object nodeObject = is_tree ? buildVirtualTree(itemObject, id) : buildLabelCollection(itemObject, id);
                list.add(id);
            }
        }
        return list;
    }

    private String convertToString(JSONArray jsonArray){
        return jsonArray==null ? null:jsonArray.
                stream().
                map(Objects::toString).
                collect(Collectors.joining(","));
    }

}
