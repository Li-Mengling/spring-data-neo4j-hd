package com.lml.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("labelCollection")
public class LabelCollectionEntity {
    @Id
    private String nodeId;

    @Property("classificationIds")
    private List<String> classificationIds;

    @Property("labelIds")
    private List<String> labelIds;

    @Property("nodeName")
    private String nodeName;

    @Property("organizationId")
    private String organizationId;

    @Property("siteId")
    private String siteId;

    @Property("type")
    private String type;
}
