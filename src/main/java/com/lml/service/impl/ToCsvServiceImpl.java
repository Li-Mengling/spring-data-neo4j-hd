package com.lml.service.impl;


import com.lml.config.Neo4jProperties;
import com.lml.service.ToCsvService;
import com.lml.service.TransferService;
import com.lml.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.FileNameMap;
import java.util.Arrays;
import java.util.List;


@Service
public class ToCsvServiceImpl implements ToCsvService {

    //neo4j路径配置信息
    private final Neo4jProperties neo4jProperties;


    private final TransferService transferService;

    public ToCsvServiceImpl(TransferService transferService, Neo4jProperties neo4jProperties) {
        this.transferService = transferService;
        this.neo4jProperties = neo4jProperties;
    }




    @Override
    public <T> File toCsv(String siteID,List<Object> entities, Class<T> entityType) {
        //导入路径
        String neo4JImportPath = neo4jProperties.getNEO4J_IMPORT_PATH();
        String fileDir = neo4JImportPath +"/"+siteID;
        File file;
        if(! (file=new File(fileDir)).exists())
            file.mkdirs();
        //获取类名
        String[] childDir = entityType.getName().split("\\.");
        //导入的路径
        String fileName = fileDir+"/"+childDir[childDir.length-1]+".csv";

        String[] fileHeader = getHeader(entityType);
        try {
            return CSVUtils.writeCsv(entities,
                    fileHeader,
                    fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String[] getHeader(Class<T> entityType){
        Field[] fields = entityType.getDeclaredFields();
        String[] fieldNames = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }
}
