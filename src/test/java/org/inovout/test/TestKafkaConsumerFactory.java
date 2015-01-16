package org.inovout.test;

import static org.junit.Assert.assertNotNull;
import kafka.javaapi.consumer.ConsumerConnector;

import org.inovout.kafka.KafkaConsumerFactory;
import org.junit.Before;
import org.junit.Test;

public class TestKafkaConsumerFactory {
	private ConsumerConnector consumer = null;

	public TestKafkaConsumerFactory() {
		
	}
	
	@Before
	public void setUp() throws Exception {
		consumer=null;
	}

	@Test
	public void testNewInstance() throws Exception {
		consumer = KafkaConsumerFactory.instance.newInstance("consumer-client");
		assertNotNull("consumer对象为空", consumer);
	}

	
}
