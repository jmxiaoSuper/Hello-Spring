package com.hello.spring.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

	public static Map<String, Object> asMap(Object... arr){
		Map<String, Object> map = new HashMap<String, Object>();
		
		int len = arr.length % 2 == 0 ? arr.length : arr.length - 1;
		for (int i = 0; i < len; i = i+2) {
			map.put(arr[i].toString(), arr[i+1]);
		}
		return map;
	}
	
}
