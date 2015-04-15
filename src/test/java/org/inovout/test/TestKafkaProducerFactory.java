package org.inovout.test;

import static org.junit.Assert.assertNotNull;
import kafka.javaapi.producer.Producer;

import org.inovout.kafka.KafkaProducerFactory;
import org.junit.Before;
import org.junit.Test;

public class TestKafkaProducerFactory {
	private Producer<Long, byte[]> producer = null;

	public TestKafkaProducerFactory() {

	}

	@Before
	public void setUp() throws Exception {
		producer = null;
	}

	@Test
	public void testNewInstance() throws Exception {
		producer =  KafkaProducerFactory.instance
				.newInstance();
		assertNotNull("Producer对象不为空", producer);
	}

	@Test
	public void testKafkaTemplate() throws Exception {
		producer = KafkaProducerFactory.getProducer("zkclient-template");
		assertNotNull("CuratorFramework对象不为空", producer);
	}

}
