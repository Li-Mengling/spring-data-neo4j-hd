package com.lml.repository;

import com.lml.domain.BodyEntity;
import com.lml.dto.SiteNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Leemonlin
 */
@Repository
public interface BodyRepository extends Neo4jRepository<BodyEntity,String> {

    /**
     * 新建body节点
     * @param bodyEntity
     * @return
     */
    @Override
    @NotNull
    BodyEntity save(@NotNull BodyEntity bodyEntity);

    /**
     * 统计站点下body节点个数
     * @param siteId
     * @return
     */
    @Query("match (m:body) where m.siteId=$siteId return count(m)")
    Long countBySiteID(String siteId);

}
