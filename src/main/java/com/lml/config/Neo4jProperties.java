package com.lml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "neo4j.path")
public class Neo4jProperties {
    private	String NEO4J_PATH;
    private String NEO4J_IMPORT_PATH;
}
