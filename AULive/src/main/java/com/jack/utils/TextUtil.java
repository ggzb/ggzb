package com.jack.utils;

import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * @ClassName: TextUtil
 * @Description:
 * @author jack.long
 * @date 2014-3-18 下午5:21:48
 * 
 */
public class TextUtil {
	/**
	 * @param view
	 *            textview you want to truncate,
	 * @param maxLine
	 *            maxLines you want to display in textview you could not
	 *            setEllipse() or android:lines in xml just use this method to
	 *            truncate
	 */
	public static void truncate(final TextView view, final int maxLine) {
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			public void onGlobalLayout() {
				if (view.getLineCount() > maxLine) {
					int lineEndIndex = view.getLayout().getLineEnd(maxLine - 1);
					String text = view.getText().subSequence(0,
							lineEndIndex - 3)
							+ "...";
					view.setText(text);
				}
			}
		});
	}

	/**
	 * edittext是否为空
	 */
	public static boolean isNotNull(EditText... text) {
		for (int i = 0; i < text.length; i++) {
			if (text[i].getText() == null
					|| "".equals(text[i].getText().toString().trim())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 输入多个内容是否相等
	 */
	public static boolean isTextEquals(EditText... text) {
		if (text.length > 1)
			for (int i = 0; i < text.length - 1; i++) {
				if (!text[i].getText().toString().trim()
						.equals(text[i + 1].getText().toString().trim())) {
					return false;
				}
			}
		return true;
	}

	/**
	 * �?��集合是否有效
	 */
	public static boolean isValidate(Collection list) {
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �?��map集合是否有效
	 */
	public static boolean isValidate(Map map) {
		if (map != null && map.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是有效字符串
	 */
	public static boolean isValidate(String str) {
		if (str != null && !"".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String getText(EditText text) {
		return text.getText().toString();
	}
}
