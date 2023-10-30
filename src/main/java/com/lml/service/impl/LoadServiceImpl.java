package com.lml.service.impl;

import com.lml.converter.NodeConverter;
import com.lml.domain.BodyEntity;
import com.lml.domain.InstanceEntity;
import com.lml.dto.*;
import com.lml.pojo.LoadResult;
import com.lml.pojo.SiteId;
import com.lml.repository.BodyRepository;
import com.lml.repository.InstanceRepository;
import com.lml.service.LoadService;
import com.lml.service.ToCsvService;
import com.lml.utils.AccessDataUtils;
import lombok.extern.slf4j.Slf4j;
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

    private final BodyRepository bodyRepository;

    private final InstanceRepository instanceRepository;

    private final ToCsvService toCsvService;

    public LoadServiceImpl(ToCsvService toCsvService, BodyRepository bodyRepository, InstanceRepository instanceRepository, AccessDataUtils accessDataUtils) {
        this.toCsvService = toCsvService;
        this.bodyRepository = bodyRepository;
        this.instanceRepository = instanceRepository;
        this.accessDataUtils = accessDataUtils;
    }


    @Override
    @Transactional //开启事务，保证导入要么全部执行，要么全部不执行
    public LoadResult load(String type, SiteId siteId) {
        String typeName = typeMap.get(type);
        JsonResult jsonResult = accessDataUtils.getDataOffline(type, siteId.getSiteId());
        List<SiteNode> siteNodes =  jsonResult.getSiteNodes();
        for (SiteNode node : siteNodes) {
            if(type.equals("body")) {
                BodyEntity bodyEntity = NodeConverter.INSTANCE.bodyEntityMapper(node);
                BodyEntity body = bodyRepository.save(bodyEntity);
                System.out.println(body);
            }else{
                InstanceEntity instanceEntity = NodeConverter.INSTANCE.instanceEntityMapper(node);
                InstanceEntity instance = instanceRepository.save(instanceEntity);
                System.out.println(instance);
            }
        }

        return null;
    }




}

