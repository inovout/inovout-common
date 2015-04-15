package org.inovout.util;

import java.io.IOException;

import org.inovout.InovoutException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public class JsonUtils {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static byte[] getBytes(Object obj) {
		try {
			return MAPPER.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new InovoutException(e);
		}
	}

	public static String getString(byte[] obj) {
		try {
			return MAPPER.readValue(obj, String.class);
		} catch (JsonProcessingException e) {
			throw new InovoutException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new InovoutException(e);
		}
	}

	public static String objectToJson(Object object) {
		String str = null;
		try {
			MAPPER.setVisibilityChecker(VisibilityChecker.Std.defaultInstance()
					.withFieldVisibility(Visibility.ANY));
			str = MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new InovoutException(e);
		}
		return str;
	}

	public static Object jsonToObject(String json, Class<?> clazz) {
		Object object = null;
		try {
			MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			object = MAPPER.readValue(json, clazz);
		} catch (JsonParseException e) {

			throw new InovoutException(e);
		} catch (JsonMappingException e) {

			throw new InovoutException(e);
		} catch (IOException e) {

			throw new InovoutException(e);
		}
		return object;
	}
}
