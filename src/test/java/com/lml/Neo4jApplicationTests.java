package com.lml;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.lml.dto.JsonResult;
import com.lml.dto.LabelCollectionDTO;
import com.lml.dto.SiteNode;
import com.lml.service.ToCsvService;
import com.lml.service.TransferService;
import com.lml.utils.AccessDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class Neo4jApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private AccessDataUtils accessDataUtils;

	@Test
	void testJson(){
		JsonResult jsonResult = accessDataUtils.getDataOffline("body",
													"64e309dff53c3715653685cb");
		for (SiteNode siteNode : jsonResult.getSiteNodes()) {
			for (LabelCollectionDTO labelCollection : siteNode.getLabelCollections()) {
				System.out.println(labelCollection.getChildren());
			}
		}
	}

	@Autowired
	private TransferService transferService;
	@Test
	void serviceTest(){
		try(FileReader fileReader = new FileReader("D:\\JavaDir\\neo4j\\src\\main\\resources\\static\\64e309dff53c3715653685cb\\body.json")){
			JSONReader jsonReader = new JSONReader(fileReader);
			JSONObject data;
			HashMap hashMap = jsonReader.readObject(HashMap.class);
			System.out.println(hashMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Autowired
	private ToCsvService toCsvService;


	
	@Test
	void testUrl(){
		JsonResult jsonResult = accessDataUtils.getDataOffline("getBodyNodeAndRelation", "64e309dff53c3715653685cb");
		List<SiteNode> siteNodes = jsonResult.getSiteNodes();
		for (SiteNode node : siteNodes) {
			System.out.println(node);
		}

	}


}
