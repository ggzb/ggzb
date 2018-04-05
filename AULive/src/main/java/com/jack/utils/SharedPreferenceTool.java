package com.jack.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.ilikezhibo.ggzb.AULiveApplication;

/**
 * A share preference helper class for accessing preference saved in
 * core_share.xml
 * 
 */
public class SharedPreferenceTool {
	/** share preference file to save */
	public static final String SHARE_DATA_FILE_NAME = "piaoke";
	public static final String COOKIE_KEY = "phone.app100646015.twsapp.com";
	public static final String REG_UID = "reg_uid";
	public static final String REG_PHONE = "register_phone";
	public static final String REG_FINISH_FIRST = "REG_FINISH_FIRST";

	public static final String LOGIN_USER_PHONE = "login_phone";

	// >帐号是否首次登录1v1版本
	public static final String IS_FIRST_ONETONE = "IS_FIRST_ONETONE";
	// >帐号是否开通1v1
	public static final String OPEN_ONETONE = "OPEN_ONETONE";
	// >显示私聊提示对话框
	public static final String SHOW_SOLO_DIALOG = "SHOW_SOLO_DIALOG";

	public static final String BIND_WX_PUBLIC = "BIND_WX_PUBLIC";



	public static String NEW_MSG_NOTICE_KEY = "NEW_MSG_NOTICE_KEY";// 接收消息但不提醒
	public static String NO_MSG_RECIEVED_KEY = "NO_MSG_RECIEVED_KEY";// 不接收消息
   public static String NO_LOCATION_KEY = "NO_LOCATION_KEY";// 不显示定位
	private static SharedPreferenceTool instance = null;
	private SharedPreferences mSharedPreferences;

	private SharedPreferenceTool() {
	}

	public synchronized static SharedPreferenceTool getInstance() {
		if (instance == null) {
			instance = new SharedPreferenceTool();
			Context applictationContext = AULiveApplication.mContext;
			instance.mSharedPreferences = applictationContext
					.getSharedPreferences(SHARE_DATA_FILE_NAME,
							Context.MODE_PRIVATE);
		}
		return instance;
	}

	public void saveString(String key, String value) {
		getEditor().putString(key, value).commit();
	}

	public void saveBoolean(String key, boolean value) {
		getEditor().putBoolean(key, value).commit();
	}

	/**
	 * read the value from preference settings for the given key
	 * 
	 * @param key
	 *            which preference key to search for
	 * @param defValue
	 *            return when the given key not found, if not specified, "" will
	 *            return.
	 * @return the result value for the given key.
	 */
	public String getString(String key, String... defValue) {
		String defaultValue = "";
		if (null == defValue) {
			defaultValue = null;
		} else if (null != defValue && defValue.length > 0) {
			defaultValue = defValue[0];
		}
		return instance.mSharedPreferences.getString(key, defaultValue);
	}

	public boolean getBoolean(String key, boolean... defValue) {
		boolean def = false;
		if (null != defValue && defValue.length > 0) {
			def = defValue[0];
		}
		return instance.mSharedPreferences.getBoolean(key, def);
	}

	public void remove(String key) {
		if (instance.mSharedPreferences.contains(key)) {
			getEditor().remove(key).commit();
		}
	}

	private Editor getEditor() {
		return instance.mSharedPreferences.edit();
	}

	public static void release() {
		instance = null;
	}
}