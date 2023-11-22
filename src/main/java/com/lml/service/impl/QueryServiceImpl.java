package com.lml.service.impl;

import com.lml.dto.SiteIdDTO;
import com.lml.repository.BodyRepository;
import com.lml.repository.InstanceRepository;
import com.lml.service.QueryService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/11/03/10:09
 */
@Service
public class QueryServiceImpl implements QueryService {

    private final BodyRepository bodyRepository;

    private final InstanceRepository instanceRepository;

    public QueryServiceImpl(BodyRepository bodyRepository, InstanceRepository instanceRepository) {
        this.bodyRepository = bodyRepository;
        this.instanceRepository = instanceRepository;
    }

    /**
     * 统计实体节点与本体节点个数
     * @param siteIdDTO siteId
     * @return long
     */
    @Override
    public long countBySiteId(SiteIdDTO siteIdDTO) {
        String siteId1 = siteIdDTO.getSiteId();
        long bodyCount = bodyRepository.countBySiteID(siteId1);
        long insCount = instanceRepository.countBySiteId(siteId1);
        return bodyCount+insCount;
    }
}
