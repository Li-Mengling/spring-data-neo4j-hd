package com.lml.utils;


import com.google.common.collect.Maps;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * csv文件读写工具类
 *
 * @author by YangLD
 * @date 2018/7/10
 */
public class CSVUtils {

    private static final Logger logger = LoggerFactory.getLogger("CSVUtils.class");

    /**
     * 写csv文件 (一次性写  数据不宜过大)
     *
     * @param objectList 对象
     * @param fileHeader 头说明
     * @param fileName   文件名称(不要后缀.csv)
     * @return File 文件
     * @throws IOException 异常
     */
    public static  File writeCsv(List<Object> objectList, String[] fileHeader, String fileName) throws IOException {
        // 这里显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
        CSVFormat format = CSVFormat.DEFAULT.withHeader(fileHeader).withRecordSeparator("\n");
        // 这个是定位   判断某个字段的数据应该放在records数组中的那个位子
        Map<String, Integer> map = Maps.newHashMap();
        for (int i = 0; i < fileHeader.length; i++) {
            map.put(fileHeader[i], i);
        }

        File csvFile = new File(fileName);
        try {
            // 获取对象的PropertyDescriptor
            Map<String, PropertyDescriptor> descriptorMap = null;
            // 附加
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"));
            CSVPrinter printer = new CSVPrinter(bw, format);
            for (Object object : objectList) {
                if (CheckUtils.isEmpty(descriptorMap)) {
                    descriptorMap = CSVUtils.getCsvFieldMapPropertyDescriptor(object.getClass());
                }
                String[] records = new String[fileHeader.length];
                for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
                    if (descriptorMap.containsKey(stringIntegerEntry.getKey())) {
                        records[map.get(stringIntegerEntry.getKey())] = (String) descriptorMap.get(stringIntegerEntry.getKey()).getReadMethod().invoke(object);
                    }
                }
                printer.printRecord(Arrays.asList(records));
            }
            bw.flush();
            bw.close();
            printer.close();
        } catch (Exception e) {
            logger.error("CsvUtils.writeCsv,写csv文件失败,message:{}", e.getMessage(), e);
            throw new IOException();
        }
        return csvFile;
    }

    /**
     * 获取对应对象中包含CsvCsvField字段的 PropertyDescriptor
     *
     * @param tClass 对象的class
     * @return Map
     * @throws Exception 异常
     */
    public static Map<String, PropertyDescriptor> getCsvFieldMapPropertyDescriptor(Class tClass) throws Exception {
        Map<String, PropertyDescriptor> descriptorMap = Maps.newHashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(tClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            // 获取该字段赋值过来的  字段名称
            if (propertyDescriptor.getWriteMethod() == null) {
                continue;
            }
            Field field = tClass.getDeclaredField(propertyDescriptor.getName());
            CSVField csvField = field.getAnnotation(CSVField.class);
            if (csvField == null) {
                continue;
            }
            String fieldMetaName = csvField.name();
            if (CheckUtils.isEmpty(fieldMetaName)) {
                continue;
            }
            descriptorMap.put(fieldMetaName, propertyDescriptor);
        }
        return descriptorMap;
    }
}

