package com.lml.service;

import com.lml.dto.NodeAndRelationList;
import com.lml.dto.SiteIdDTO;

public interface TransferService {

    NodeAndRelationList getData(String type, SiteIdDTO siteIdDTO);

}
