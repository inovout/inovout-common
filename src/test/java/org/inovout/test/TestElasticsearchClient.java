package org.inovout.test;

import org.inovout.elasticsearch.ElasticsearchClient;
import org.inovout.elasticsearch.ElasticsearchClientFactory;

public class TestElasticsearchClient {

	private static ElasticsearchClient elasticsearchClient;

	static {
		elasticsearchClient = ElasticsearchClientFactory
				.getClient(TestElasticsearchClient.class);
	}

	/*
	 * @Test public void testGetIndexRequestBuilder() { IndexRequestBuilder
	 * indexRequestBuilder = elasticsearchClient
	 * .getIndexRequestBuilder("device", "MonitorNode");
	 * assertNotNull("通过类文件获取的indexRequestBuilder 为空", indexRequestBuilder); }
	 */
}
