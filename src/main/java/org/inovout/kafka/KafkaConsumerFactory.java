package org.inovout.kafka;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inovout.BaseFactory;
import org.inovout.config.Configuration;
import org.inovout.zookeeper.ZooKeeperClientFactory;

public class KafkaConsumerFactory extends BaseFactory<ConsumerConnector> {
	private static final Log LOG = LogFactory.getLog(KafkaConsumerFactory.class);
	private KafkaConsumerFactory() {

	}

	public static final KafkaConsumerFactory instance = new KafkaConsumerFactory();

	public static ConsumerConnector getConsumer(Class<?> clazz) {
		return instance.getInstance(clazz);
	}

	public static ConsumerConnector getConsumer(String name) {
		return instance.getInstance(name);
	}

	private static final String ZOOKEEPER_CONNECT_KEY = "zookeeper.connect";

	private static final String GROUP_ID_KEY = "group.id";
	private static final String DEFAULT_GROUP_ID = "group_test";

	private static final String ZOOKEEPER_SESSION_TIMEOUT_MS_KEY = "zookeeper.session.timeout.ms";


	private static final String ZOOKEEPER_SYNC_TIME_MS_KEY = "zookeeper.sync.time.ms";


	private static final String AUTO_COMMIT_INTERVAL_MS_KEY = "auto.commit.interval.ms";
	private static final String DEFAULT_AUTO_COMMIT_INTERVAL_MS = "1000";

	private static final String AUTO_OFFSET_RESET_KEY = "auto.offset.reset";
	private static final String DEFAULT_AUTO_OFFSET_RESET = "smallest";

	

	private static final Configuration configuration;
	static {
		configuration = new Configuration();
		configuration.addResource("kafka-consumer-site.xml");
	}

	@Override
	public ConsumerConnector newInstance(String name) {
		ConsumerConnector consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig());
		LOG.info("**********consumer created successfully**************");
		return consumer;
	}

	private ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put(ZOOKEEPER_CONNECT_KEY,ZooKeeperClientFactory.getConnectionString());
		props.put(GROUP_ID_KEY, configuration.get(GROUP_ID_KEY,DEFAULT_GROUP_ID));
		props.put(ZOOKEEPER_SESSION_TIMEOUT_MS_KEY,String.valueOf(ZooKeeperClientFactory.getSessionTimeOutMs()));
		props.put(ZOOKEEPER_SYNC_TIME_MS_KEY, String.valueOf(ZooKeeperClientFactory.getSyncTimeMs()));
		props.put(AUTO_COMMIT_INTERVAL_MS_KEY, configuration.get(AUTO_COMMIT_INTERVAL_MS_KEY,DEFAULT_AUTO_COMMIT_INTERVAL_MS));	
		props.put(AUTO_OFFSET_RESET_KEY, configuration.get(AUTO_OFFSET_RESET_KEY,DEFAULT_AUTO_OFFSET_RESET));

		return new ConsumerConfig(props);
	}
}
