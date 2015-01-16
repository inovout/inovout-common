package org.inovout.test;

import static org.junit.Assert.*;

import org.inovout.BaseFactory;
import org.junit.Test;

public class TestBaseFactory {

	private  BaseFactory<?> baseFactory;
	
	public TestBaseFactory(){
		baseFactory =new ObjectFactory();
	}
	
	@Test
	public void testGetClientByName() throws Exception {	
		Object client= baseFactory.getInstance("Object");
		assertNotNull("通过字符串获取的client 不为空",client);
	}

	@Test
	public void testGetClientByClass() throws Exception {	
		Object client= baseFactory.getInstance(Object.class);
		assertNotNull("通过类文件获取的client 不为空",client);
	}
	
	@Test
	public void testSameClient() throws Exception {	
		Object client1= baseFactory.getInstance("Object");
		Object client2= baseFactory.getInstance(Object.class);
		assertNotSame("两个对象是不是同一对象",client1,client2);
	}
	
	
	public class ObjectFactory extends BaseFactory<Object>{
		public ObjectFactory(){
			
		}
		@Override
		public  Object newInstance(String name){
			return new Object();
		}
	}

}
