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


//BODY类用于表示
@Data
@Node("body")
public class BodyEntity {

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

    @Property("labelCollectionList")
    private List<String> labelCollectionList;

    @Property("virtualTreeList")
    private List<String> virtualTreeList;

    @Property("structureList")
    private List<String> structureList;
    //关系

    @Relationship(type = "is_instance",direction = Relationship.Direction.OUTGOING)
    private Set<InstanceEntity> instances = new HashSet<>();

    @Relationship(type = "belong_to",direction = Relationship.Direction.INCOMING)
    private Set<BodyEntity> bodyEntities = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BodyEntity that = (BodyEntity) o;
        return Objects.equals(nodeId, that.nodeId);
    }

}
