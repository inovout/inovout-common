package org.inovout.test;

import static org.junit.Assert.assertNotNull;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.inovout.kafka.KafkaProducerFactory;
import org.junit.Before;
import org.junit.Test;

public class TestKafkaProducerFactory {
	private Producer<String,String> producer = null;

	public TestKafkaProducerFactory() {
		
	}
	
	@Before
	public void setUp() throws Exception {
		producer=null;
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNewInstance() throws Exception {
		producer = (Producer<String,String>)KafkaProducerFactory.instance.newInstance();
		KeyedMessage<String,String> data = new KeyedMessage<String,String>(
				"monitoring-data-receiver","wei");
		producer.send(data);
		assertNotNull("Producer对象不为空", producer);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testKafkaTemplate() throws Exception {
		producer = (Producer<String,String>)KafkaProducerFactory.getProducer("zkclient-template");
		assertNotNull("CuratorFramework对象不为空", producer);
	}


}
