package org.inovout.test;

import static org.junit.Assert.assertNotNull;

import org.inovout.mongodb.MongoDbClientFactory;
import org.junit.Test;

public class TestMongoDbClientFactory {

	@Test
	public void testNewInstance(){		
	 Object client=	MongoDbClientFactory.instance.newInstance();
	 assertNotNull("通过类文件获取的client 为空",client);
	}
}
