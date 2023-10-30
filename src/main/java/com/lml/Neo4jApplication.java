package com.lml;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.lml.config.Neo4jProperties;
import com.lml.dto.JsonResult;
import com.lml.utils.AccessDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;

@SpringBootApplication
public class Neo4jApplication {


	public static void main(String[] args) {
		SpringApplication.run(Neo4jApplication.class, args);
	}

}
