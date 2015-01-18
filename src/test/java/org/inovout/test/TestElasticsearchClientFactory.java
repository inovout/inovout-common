package org.inovout.test;

import static org.junit.Assert.assertNotNull;

import org.inovout.elasticsearch.ElasticsearchClientFactory;
import org.junit.Test;

public class TestElasticsearchClientFactory {
	@Test
	public void testNewInstance(){		
	 Object client=	ElasticsearchClientFactory.instance.newInstance();
	 assertNotNull("通过类文件获取的client 为空",client);
	}
}
