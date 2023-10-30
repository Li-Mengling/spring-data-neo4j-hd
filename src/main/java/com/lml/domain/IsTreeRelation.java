package com.lml.domain;

import lombok.Data;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.schema.*;

/**
 * Created with IntelliJ IDEA.
 * 用于描述虚拟树与根节点之间的关系
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/29/10:54
 */
@Data
@RelationshipProperties
public class IsTreeRelation {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private BodyEntity bodyEntity;
}
