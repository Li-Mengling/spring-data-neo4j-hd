package com.lml.converter;

import com.lml.domain.BodyEntity;
import com.lml.domain.InstanceEntity;
import com.lml.dto.SiteNode;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: 将SiteNode对象转换成对应的BodyEntity或InstanceEntity
 * @Author: leemonlin
 * @Date: 2023/10/30/16:56
 */
@Mapper
@DecoratedWith(NodeConverterDecorator.class)
public interface NodeConverter {
    NodeConverter INSTANCE = Mappers.getMapper(NodeConverter.class);

    /**
     * 将SiteNode映射为BodyEntity
     * @param siteNode
     * @return
     */
    @Mapping(target = "virtualTreeList", ignore = true)
    BodyEntity bodyEntityMapper(SiteNode siteNode);

    /**
     * 将SiteNode映射为InstanceEntity
     * @param siteNode
     * @return
     */
    @Mapping(target = "virtualTreeList", ignore = true)
    InstanceEntity instanceEntityMapper(SiteNode siteNode);
}
