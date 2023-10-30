package com.lml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Author: leemonlin
 * @Date: 2023/10/28/13:46
 */


@Component
@Data
@ConfigurationProperties(prefix = "deep.file.url")
public class DataUrlProperties {
    private String itemUrl;
    private String packageUrl;
}
