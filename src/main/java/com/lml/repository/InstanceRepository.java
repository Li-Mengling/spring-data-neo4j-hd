package com.lml.repository;

import com.lml.domain.InstanceEntity;
import com.lml.dto.SiteNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
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

    /**
     * 新建instance节点
     * @param instanceEntity
     * @return
     */
    @Override
    @NotNull
    InstanceEntity save(@NotNull InstanceEntity instanceEntity);

    /**
     * 统计站点下instance节点个数
     * @param siteId
     * @return
     */
    @Query("match (m:instance) where m.siteId=$siteId return count(m)")
    Long countBySiteId(String siteId);

}
