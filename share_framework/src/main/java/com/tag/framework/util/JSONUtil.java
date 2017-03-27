package com.tag.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);
	public static JSONObject getJsonObject(String jsonStr) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.parseObject(jsonStr);
		} catch (Exception e) {
			LOGGER.error("parse jsonObject error:", e);
		}
		return jsonObject;
	}
	
	public static Object getJsonStringKey(String jsonStr, String key) {
		JSONObject jsonObject = JSONUtil.getJsonObject(jsonStr);
		Object result = null ;
		if(null != jsonObject){
			result = jsonObject.get(key);
		}
		return result;
	}
	
	public static String toString(Object object){
		String str = null;
		try {
			str = JSONObject.toJSONString(object);
		} catch (Exception e) {
			LOGGER.error("parse jsonObject error:", e);
		}
		return str;
	}
	
	public static String jsonToString(Object object) {
		String str= null;
		try {
			str = JSONObject.toJSONString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}