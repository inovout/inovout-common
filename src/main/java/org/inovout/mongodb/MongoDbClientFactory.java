package org.inovout.mongodb;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inovout.BaseFactory;
import org.inovout.InovoutException;
import org.inovout.config.Configuration;


import com.mongodb.ServerAddress;

public class MongoDbClientFactory extends BaseFactory<MongoDbClient> {
	private static final Log LOG = LogFactory.getLog(MongoDbClientFactory.class);
	private MongoDbClientFactory() {
	}

	public static final MongoDbClientFactory instance = new MongoDbClientFactory();

	public static MongoDbClient getClient(Class<?> clazz) {
		return instance.getInstance(clazz);
	}

	public static MongoDbClient getClient(String name) {
		return instance.getInstance(name);
	}

	private static final String SERVER_ADDRESS_STRING_KEY = "mongodb.server.address";
	private static final String DEFAULT_SERVER_ADDRESS_STRING = getLocalAddress()
			+ ":27017";

	private static final Configuration configuration;
	static {
		configuration = new Configuration();
		configuration.addResource("mongodb-site.xml");
	}

	private static String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "127.0.0.1";
	}

	public static List<ServerAddress> getServerAddresses() {
		String serverAddressString = configuration.get(
				SERVER_ADDRESS_STRING_KEY, DEFAULT_SERVER_ADDRESS_STRING);
		String[] serverAddressArray = serverAddressString.split(",");
		List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>(
				serverAddressArray.length);
		try {
			for (String serverAddress : serverAddressArray) {

				serverAddressList.add(new ServerAddress(serverAddress));

			}
		} catch (UnknownHostException e) {
			throw new InovoutException(e);
		}
		return serverAddressList;
	}

	@Override
	public MongoDbClient newInstance() {
		LOG.info("MongoDbClientFactoryBuildInfo: serverAddress:"+getServerAddresses());
		return new MongoDbClient(getServerAddresses());
	}

}
