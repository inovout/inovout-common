package org.inovout.test;

import static org.junit.Assert.assertNotNull;

import org.inovout.mongodb.MongoDbClient;
import org.inovout.mongodb.MongoDbClientFactory;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class TestMongoDbClient {

	private static MongoDbClient mongoDbClient;
	
	static{
		mongoDbClient =MongoDbClientFactory.getClient(TestMongoDbClient.class);
	}
	
	@Test
	public void testGetDatabase(){
	  DB huxiDb = mongoDbClient.getDatabase("HuXi");
	  assertNotNull("通过类文件获取的db 为空",huxiDb);
	}
	
	@Test
	public void testGetCollection(){
		DBCollection huxiCollections=mongoDbClient.getCollection("HuXi","shengshuju");
		assertNotNull("通过类文件获取的Collections 为空",huxiCollections);
	  
	}
}
