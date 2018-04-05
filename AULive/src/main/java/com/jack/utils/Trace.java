package com.jack.utils;

import android.util.Log;

/**
 * 
 * @ClassName: Trace
 * @Description: Log日志
 * @author jack.long
 * @date 2014-3-18 下午5:20:45
 * 
 */
public class Trace {
	public static final String TAG = "big";
	public static boolean DEBUG = false;// false发布版本模式（记得改变市场名称�?版本号） true 调试模式

	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void w(String msg) {
		if (DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}
}
