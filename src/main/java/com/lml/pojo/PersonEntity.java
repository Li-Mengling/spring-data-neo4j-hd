package com.lml.pojo;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
@Data
@Node("person")
public class PersonEntity {
    @Id
    @GeneratedValue
    private Long id;
    private final String name;
    private final Integer born;

}
