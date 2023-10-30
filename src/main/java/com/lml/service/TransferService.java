package com.lml.service;

import com.lml.dto.NodeAndRelationList;
import com.lml.pojo.SiteId;

public interface TransferService {

    NodeAndRelationList getData(String type, SiteId siteId);

}
