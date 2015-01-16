package org.inovout.elasticsearch;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

public class ElasticsearchClient {
	private TransportClient client;

	public ElasticsearchClient(String servers, String clusterName) {
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", clusterName).build();
		client = new TransportClient(settings);
		client.addTransportAddresses(getTransportAddresses(servers));

	}

	private TransportAddress[] getTransportAddresses(String servers) {
		String[] serverArray = servers.split(",");
		TransportAddress[] transportAddrs = new TransportAddress[serverArray.length];
		for (int i = 0; i < transportAddrs.length; i++) {
			String[] ipOrPort = serverArray[i].split(":");
			transportAddrs[i] = new InetSocketTransportAddress(ipOrPort[0],
					Integer.parseInt(ipOrPort[1]));
		}
		return transportAddrs;
	}

	public IndexRequestBuilder getIndexRequestBuilder(String index,String type) {
		return client.prepareIndex(index, type);
	}

	void dispose() throws Exception {
		client.close();
	}
}
