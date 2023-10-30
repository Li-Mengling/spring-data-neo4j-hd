package com.lml.repository;

import com.lml.domain.InstanceEntity;
import com.lml.dto.SiteNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/28/15:16
 */
@Repository
public interface InstanceRepository extends Neo4jRepository<InstanceEntity,String> {

    InstanceEntity save(InstanceEntity instanceEntity);

}
