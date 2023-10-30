package com.lml.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface ToCsvService {

    <T> File toCsv(String siteID, List<Object> entity, Class<T> entityType);
}
