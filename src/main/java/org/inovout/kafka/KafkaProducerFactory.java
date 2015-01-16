package org.inovout.kafka;


import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inovout.BaseFactory;
import org.inovout.config.Configuration;


public class KafkaProducerFactory extends BaseFactory<Producer<?,?>>{
	private static final Log LOG = LogFactory.getLog(KafkaProducerFactory.class);
	private KafkaProducerFactory() {
		
	}

	public static final KafkaProducerFactory instance = new KafkaProducerFactory();

	public static Producer<?,?> getProducer(Class<?> clazz) {
		return instance.getInstance(clazz);
	}

	public static Producer<?,?> getProducer(String name) {
		return instance.getInstance(name);
	}
	

	
	private static final String METADATA_BROKER_LIST_KEY = "metadata.broker.list";
	private static final String SERIALIZER_CLASS_KEY = "serializer.class";
	private static final String DEFAULT_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";


	
	private static final Configuration configuration;
	static {
		configuration = new Configuration();
		configuration.addResource("kafka-producer-site.xml");
	}
	
	
	
	@Override
	public Producer<?,?>  newInstance(String name){
		Properties props = new Properties();
		props.put(METADATA_BROKER_LIST_KEY, configuration.get(METADATA_BROKER_LIST_KEY));
		props.put(SERIALIZER_CLASS_KEY, configuration.get(SERIALIZER_CLASS_KEY,DEFAULT_SERIALIZER_CLASS));
		ProducerConfig config = new ProducerConfig(props);
		Producer<?,?> producer = new Producer<String,String>(config);
		return producer;
	    
	}
}
