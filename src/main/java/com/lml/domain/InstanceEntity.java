package com.lml.domain;

import com.lml.utils.CSVField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * instance实体类
 * @author Leemonlin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("instance")
public class InstanceEntity {

    @Id
    private String nodeId;

    //属性
    @Property("siteId")
    private String siteId;

    @Property("nodeName")
    private String nodeName;

    @Property("type")
    private String type;

    @Property("lastSiteNodeId")
    private String lastSiteNodeId;

    @Property("snType")
    private Integer snType;

    @Property("fileType")
    private String fileType;

    private String bodySiteNodeId;

    @Property("labelCollectionList")
    private List<String> labelCollectionList;

    @Property("virtualTreeList")
    private List<String> virtualTreeList;

    @Property("structureList")
    private List<String> structureList;

    //实体节点与实体节点之间的关系
    @Relationship(type = "belong_to", direction = Relationship.Direction.INCOMING)
    private Set<InstanceEntity> belongTo = new HashSet<>();

    //实体节点与本体节点之间的关系
    @Relationship(type = "is_instance",direction = Relationship.Direction.OUTGOING)
    private BodyEntity isInstance;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceEntity that = (InstanceEntity) o;
        return Objects.equals(nodeId, that.nodeId);
    }

    /**
     * 根据id寻找InstanceEntity对象
     * @param nodeId
     * @return
     */
    public InstanceEntity getInstanceEntity(String nodeId){
        return this.nodeId.equals(nodeId) ? this: null;
    }

    public void addBelongTo(InstanceEntity instanceEntity) {
        belongTo.add(instanceEntity);
    }

}
