package com.lml.test;

import com.lml.converter.NodeConverter;
import com.lml.domain.BodyEntity;
import com.lml.dto.SiteNode;
import com.lml.dto.VirtualTree;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/30/14:44
 */

public class testMapStruct {

    public static void main(String[] args) {
        SiteNode siteNode = new SiteNode();
        siteNode.setNodeId("node");
        VirtualTree virtualTree = new VirtualTree();
        virtualTree.setNodeId("123");
        siteNode.setVirtualTreeList(List.of(virtualTree));
        BodyEntity bodyEntity = NodeConverter.INSTANCE.bodyEntityMapper(siteNode);
        System.out.println(bodyEntity);
    }
}
