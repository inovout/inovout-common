package org.inovout.test;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Path;

import java.nio.file.Paths;

import org.inovout.config.Configuration;
import org.junit.Before;
import org.junit.Test;

public class TestConfiguration {

	private Configuration conf;
	final static String CONFIG = new File(".\\conf\\test.xml")
			.getAbsolutePath();
	final static String CONFIG_PROPERTIES = new File(".\\conf\\test.properties")
			.getAbsolutePath();

	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
	}

	@Test
	public void testConfigFile() {
		Path path = Paths.get(CONFIG);
		conf.addResource(path);
		String userName = conf.get("username");
		assertEquals("qianwei", userName);
	}

	@Test
	public void testPropertityFile() {
		Path path = Paths.get(CONFIG_PROPERTIES);
		conf.addResource(path);
		String userName = conf.get("username");
		assertEquals("qianwei", userName);
	}
}
