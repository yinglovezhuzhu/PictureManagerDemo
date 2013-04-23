package com.example.picturemanagerdemo.util;

import android.util.Log;
/**
 * 日志工具类
 * 加入打印标致，一处修改，全部可以取消打印信息
 * @author xiaoying
 *
 */
public class LogUtil {
	private static boolean mPrint = true;
	
	public static void i(String tag, String msg) {
		if(mPrint) {
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if(mPrint) {
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if(mPrint) {
			Log.e(tag, msg);
		}
	}
}
