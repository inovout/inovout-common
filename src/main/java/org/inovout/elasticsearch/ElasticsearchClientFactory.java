package org.inovout.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inovout.BaseFactory;
import org.inovout.config.Configuration;

public class ElasticsearchClientFactory extends
		BaseFactory<ElasticsearchClient> {
	private static final Log LOG = LogFactory.getLog(ElasticsearchClientFactory.class);
	private ElasticsearchClientFactory() {
	}

	public static final ElasticsearchClientFactory instance = new ElasticsearchClientFactory();

	public static ElasticsearchClient getClient(Class<?> clazz) {
		return instance.getInstance(clazz);
	}

	public static ElasticsearchClient getClient(String name) {
		return instance.getInstance(name);
	}

	private static final String CLUSTER_NAME_KEY = "es.cluster.name";
	private static final String DEFAULT_CLUSTER_NAME = "elasticsearch";
	private static final String SERVER_ADDRESS_STRING_KEY = "es.server.address";
	private static final String DEFAULT_SERVER_ADDRESS_STRING = getLocalAddress()
			+ "9300";

	private static final Configuration configuration;
	static {
		configuration = new Configuration();
		configuration.addResource("elasticsearch-site.xml");
	}

	private static String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "127.0.0.1";
	}

	@Override
	public ElasticsearchClient newInstance() {
		String servers = configuration.get(SERVER_ADDRESS_STRING_KEY,
				DEFAULT_SERVER_ADDRESS_STRING);
		String clusterName = configuration.get(CLUSTER_NAME_KEY,
				DEFAULT_CLUSTER_NAME);
		LOG.info("ElasticsearchClientFactoryBuildInfo: servers:"+servers+";clusterName:"+clusterName);
		return new ElasticsearchClient(servers, clusterName);
	}

}
