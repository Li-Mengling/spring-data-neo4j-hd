package com.lml.repository;

import com.lml.domain.BodyEntity;
import com.lml.dto.SiteNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyRepository extends Neo4jRepository<BodyEntity,String> {

    BodyEntity save(BodyEntity bodyEntity);

}
