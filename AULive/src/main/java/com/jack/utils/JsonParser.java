package com.jack.utils;

import com.google.myjson.Gson;
import com.google.myjson.JsonSyntaxException;
import java.lang.reflect.Type;

public class JsonParser {
	public static Gson gson = new Gson();

	/** 返回json转换后的对象. */
	public static <T> T deserializeByJson(String data, Type type) {
		return gson.fromJson(data, type);
	}

	/** 返回json转换后的对象. */
	public static <T> T deserializeByJson(String data, Class<T> clz) {
		try {
			T t = gson.fromJson(data, clz);
			return t;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/** 返回json字符�? */
	public static <T> String serializeToJson(T t) {
		return gson.toJson(t);
	}

}