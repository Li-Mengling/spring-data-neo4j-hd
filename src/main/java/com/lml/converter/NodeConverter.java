package com.lml.converter;

import com.lml.domain.*;
import com.lml.dto.LabelCollectionDTO;
import com.lml.dto.LabelDTO;
import com.lml.dto.SiteNode;
import com.lml.dto.VirtualTreeDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
     * @return
     */

    @Mapping(target = "virtualTreeList", ignore = true)
    @Mapping(target = "labelCollectionList", ignore = true)
    BodyEntity bodyEntityMapper(SiteNode siteNode);


    List<BodyEntity> bodyEntityListMapper(List<SiteNode> siteNodeList);


    /**
     * 将SiteNode映射为InstanceEntity
     */
    @Mapping(target = "virtualTreeList", ignore = true)
    @Mapping(target = "labelCollectionList", ignore = true)
    InstanceEntity instanceEntityMapper(SiteNode siteNode);

    List<InstanceEntity> instanceEntityListMapper(List<SiteNode> siteNodeList);

    /**
     *映射LabelCollectionEntity
     */
    @Mapping(source = "name", target = "nodeName")
    LabelCollectionEntity labelCollectionEntityMapper(LabelCollectionDTO labelCollectionDTO);

    @Mapping(source = "children",target = "label")
    List<LabelCollectionEntity> labelCollectionEntityListMapper(List<LabelCollectionDTO> labelCollectionDTOList);

    /**
     * 映射LabelEntity

     */
    @Mapping(target = "options", ignore = true)
    @Mapping(source = "name", target = "nodeName")
    LabelEntity labelEntityMapper(LabelDTO labelDTO);

    @Mapping(source = "children", target = "label")
    List<LabelEntity> labelEntityListMapper(List<LabelDTO> labelDTOList);

    /**
     * 映射virtualTree
     */

    VirtualTreeEntity virtualTreeEntityMapper(VirtualTreeDTO virtualTreeDTO);


    List<VirtualTreeEntity> virtualTreeEntityListMapper(List<VirtualTreeDTO> virtualTreeDTOList);

}
