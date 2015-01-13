package org.inovout.util;

public class Contracts {
	public static void RequiresForString(String value,String message){
		if(value==null||value.isEmpty()){
			throw new IllegalArgumentException(message);
		}	
	}
}
