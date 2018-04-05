package com.jack.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class PixelDpHelper {

	private static PixelDpHelper sInstance;

	public static PixelDpHelper getInstance() {
		if (sInstance == null) {
			sInstance = new PixelDpHelper();
		}
		return sInstance;
	}

	/** * 根据手机的分辨率�?dp 的单�?转成�?px(像素) */
	public static int dip2px(Context context, float dpValue) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final float scale = dm.density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率�?px(像素) 的单�?转成�?dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
