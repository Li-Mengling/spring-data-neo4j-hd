package com.lml.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadResult {
    private Integer nodeNum;
    private Integer relationNum;
    private List<String> nodeInfo;
    private List<String> relationInfo;
}
