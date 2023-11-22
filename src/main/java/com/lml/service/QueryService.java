package com.lml.service;

import com.lml.dto.SiteIdDTO;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/11/03/10:09
 */

public interface QueryService {

    public long countBySiteId(SiteIdDTO siteIdDTO);
}
