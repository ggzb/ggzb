package com.ilikezhibo.ggzb.tool;

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
	public static final String SHARE_DATA_FILE_NAME = "jiesihuo";
	public static final String COOKIE_KEY = "JIESIHUO_phone.app100646015.twsapp.com";
	public static final String MSG_NOTICE_COUNT_KEY = "JIESIHUO_msg_notice_count_key";
	public static final String REG_UID = "JIESIHUO_reg_uid";

	public static final String IS_HAS_NEW_APP = "JIESIHUO_is_has_new_app";

	public static final String LOGIN_USER_PHONE = "JIESIHUO_login_phone";

	public static final String VIP_INFO = "JIESIHUO_vip_info";

	// 聊天背景图片
	public static final String CHAT_BG_FILE_PATH = "JIESIHUO_CHAT_BG_FILE_PATH";

	public static final String INVITE_CODE_KEY = "JIESIHUO_INVITE_CODE_KEY";

	// 版本号
	public static final String CITYLOVE_LAST_GUIDE_VERSION = "JIESIHUO_LAST_GUIDE_VERSION";

	public static final String REG_PHONE = "JIESIHUO_register_phone";

	public static final String REG_FINISH_FIRST = "JIESIHUO_REG_FINISH_FIRST";

	public static final String CHAT_IS_VOICE_KEY = "JIESIHUO_CHAT_IS_VOICE_KEY";

	public static final String INVITE_GUIDE_KEY = "JIESIHUO_INVITE_GUIDE_KEY";

	public static final String USER_PHOTOS_KEY = "JIESIHUO_USER_PHOTOS_KEY";// 用户相册列表
	public static final String IS_UPDATE_PHOTOS = "JIESIHUO_IS_UPDATE_PHOTOS";// 是否更新相册

	public static final String LOGIN_USER_ID = "JIESIHUO_login_user_id";// 登陆的用户id

	public static final String USER_SPACE_MY_INFO = "JIESIHUO_user_space_my_info";

	public static final String CREDIT_INFO = "JIESIHUO_credit_info";// 信誉度

	public static final String IS_CREATE_SHUT_APP = "JIESIHUO_IS_CREATE_SHUT_APP";

	public static final String JIESIHUO_OVER_MSG_DETAIL = "JIESIHUO_OVER_MSG_DETAIL";

	public static final String THEME_VERSION = "JIESIHUO_THEME_VERSION";// 主题版本
	public static final String CACHE_THEME_STR = "JIESIHUO_CACHE_THEME_STR";// 缓存主题

	public static final String GIFT_VERSION = "JIESIHUO_GIFT_VERSION";// 礼物版本
	public static final String GIFT_CONTENT_STR = "JIESIHUO_GIFT_CONTENT_STR";// 礼物内容

	public static final String CACHE_WALLET_LST_STR = "JIESIHUO_CACHE_WALLET_LST_STR";// 我的钱包

	public static final String PUBLISH_VIDEO_SHARE_CACHE = "JIESIHUO_PUBLISH_VIDEO_SHARE_CACHE";// 发布视频渠道记录

	public static final String PUBISH_MY_VIDEO = "JIESIHUO_PUBISH_MY_VIDEO";// 发布视频

	public static String NEW_MSG_NOTICE_KEY = "NEW_MSG_NOTICE_KEY";// 接收消息但不提醒
	public static String NO_MSG_RECIEVED_KEY = "NO_MSG_RECIEVED_KEY";// 不接收消息


	public static String ALIPAY_ACCOUNT = "alipay_acconut";// 支付宝账号
	public static String ALIPAY_REALNAME = "alipay_realname";// 支付宝真实姓名

	public static String BANK_NUM = "bank_num";
	public static String BANK_REALNAME = "bank_realname";

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

	public void saveInt(String key, int value) {
		getEditor().putInt(key, value).commit();
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

	public int getInt(String key, int defValue) {
		return instance.mSharedPreferences.getInt(key, defValue);
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