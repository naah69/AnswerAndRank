package com.xyl.game.utils;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * JsonUtils
 *
 * @author Naah
 * @date 2017年11月20日
 *
 */
public class JsonUtils {
    private static Gson gson = new Gson();
	public static <T> T jsonToObject(String json, Class<T> clazz) {

		return gson.fromJson(json, clazz);
	}

	public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
		T[] array = gson.fromJson(json, clazz);
		return  new CopyOnWriteArrayList<>(Arrays.asList(array));
	}

	public static <T> T[] jsonToArray(String json, Class<T[]> clazz) {
		T[] array = gson.fromJson(json, clazz);
		return array;
	}

	public static String objectToJSON(Object obj) {
		return gson.toJson(obj).toString();
	}

	public static void main(String[] args) {
	}

}
