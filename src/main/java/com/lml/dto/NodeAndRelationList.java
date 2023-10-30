package com.lml.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: 集合类 用于创建Body,instance集合
 * @Author: leemonlin
 * @Date: 2023/10/28/13:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeAndRelationList {
    @JSONField(name = "snRelationList")
    private List<Object> nodeList;

    @JSONField(name = "snRelationList")
    private List<Object> relationList;
}
