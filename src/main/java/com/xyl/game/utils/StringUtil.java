package com.xyl.game.utils;

public class StringUtil {

	public static String initialsUpper(String str){
		if(Character.isLowerCase(str.charAt(0))){
			str = Character.toString(Character.toUpperCase(str.charAt(0)))
					+str.substring(1, str.length());
		}
		return str;
	}
}
