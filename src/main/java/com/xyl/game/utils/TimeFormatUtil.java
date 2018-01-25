package com.xyl.game.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 时间工具类
 * @author dazhi
 *
 */
public class TimeFormatUtil {
	
	public static String getTimeStr(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.format(date);
	}
	
	public static Date getDateForStr(String time){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date parse = null;
		try {
			parse = dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}
}
