package org.inovout.util;

import org.inovout.InovoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static byte[] getBytes(Object obj) {
		try {
			return MAPPER.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new InovoutException(e);
		}
	}
}
