package com.lml.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("virtualTree")
public class VirtualTreeEntity {
    @Id
    private String nodeId;

    @Property("nodeName")
    private String nodeName;

    @Property("siteID")
    private String siteId;
}
