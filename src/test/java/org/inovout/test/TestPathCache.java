package org.inovout.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.util.JsonUtils;
import org.inovout.util.Time;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestPathCache {

	private static String rootPath = "/3cloud/datastore/";
	private static String regionName = "document";
	private static final PathCache writeablePathCache;
	private static final PathCache readablePathCache;
	static {
		readablePathCache = CacheFactory.builderPathCache()
				.setRootPath(rootPath).setRegionName(regionName)
				.setAccessType(AccessType.READ_ONLY).build();

		writeablePathCache = CacheFactory.builderPathCache()
				.setRootPath(rootPath).setRegionName(regionName)
				.setAccessType(AccessType.WRITE_ONLY).build();
	}
	

	@Test
	public void testPutObject() {
		String key = Time.now().toString();
		String value = "hello";
		writeablePathCache.put(key, value);

	}

	@Test
	public void testGetObject() {
		String key = Time.now().toString();
		String value = "hello";
		writeablePathCache.put(key, value);
		assertEquals(value, readablePathCache.get(key).toString());
	}

	@Test
	public void testPutComplexObject() throws IOException {
		Book book = new Book();
		book.SetName("math");
		book.SetCategory(2);
		writeablePathCache.put("book", JsonUtils.objectToJson(book));
	}

	@Test
	public void testGetComplexObject() throws InterruptedException {
		String result = readablePathCache.getString("book");
		Book book = (Book) JsonUtils.jsonToObject(result, Book.class);
		assertNotNull("book 对象不为空!", book);

	}
	
   class Book {
		private String name;
		@JsonProperty
		public void SetName(String name) {
			this.name = name;
		}

		public String GetName() {
			return this.name;
		}

		private int category;
		@JsonProperty
		public void SetCategory(int category) {
			this.category = category;
		}

		public int GetCategory() {
			return this.category;
		}
	}


}
