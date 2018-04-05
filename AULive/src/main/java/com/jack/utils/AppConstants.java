package com.jack.utils;

/**
 * @author jack.long
 * @version create time：Sep 14, 2013 3:36:49 PM
 */
public class AppConstants {


	/** 聊天类型 */
	public static final int PRIV_TYPE = 0;// 私聊
	public static final int GROUP_TYPE = 1;// 群聊

	/** 聊天消息类型 */
	public static final int MSG_TYPE = 0;
	public static final int PIC_TYPE = 1;
	public static final int POSTION_TYPE = 2;
	public static final int RECORD_TYPE = 3;
	public static final int VIDEO_TYPE = 4;
	public static final int ANIM_PIC_TYPE = 5;
	public static final int WEB_TYPE = 6;
	public static final int GIFT_TYPE = 7;
	public static final int ATTENT_TYPE = 101;
	public static final int TIPS_TYPE = 100;

	/** 邀请类型 */

	/** 消息发送状态 */
	public static final int SEND_FAIL = 0;
	public static final int SEND_ING = 1;
	public static final int SEND_SUCC = 2;

	/** 消息读取状态 */
	public static final int NO_READ = 0;
	public static final int ALREADY_READ = 1;
	/** end */

	/** 对象选择 */
	public static final String OBJ_FEMALE = "限男生";
	public static final String OBJ_MALE = "限女生";
	public static final String OBJ_ALL = "不限";

	/** 买单类型 */
	public static final String TYPE_PAY_ME = "我买单";
	public static final String TYPE_PAY_OTHER = "求请客";
	public static final String TYPE_PAY_AA = "AA";

	public static final int NATIVE_PUSH_NOTIFICATION_ID = 100;
	public static final int SYSTEM_PUSH_NOTIFICATION_ID = 101;
	public static final int DATE_NOTIFICATION_ID = 102;

	public static final String KEY_COOKIE_INFO = "cookie";// cookie

	public static final String KEY_CURRENT_USERINFO = "curr_userinfo_key";

	public static final String KEY_NOTIFY_MID = "key_mid";

	public static final String KEY_OPEN_MSG = "key_open_msg";

	// public static final String BAIDU_KEY = "7kvNe7wLZGdPQ7XZ5jTpu3RW";
	public static final String BAIDU_KEY = "9UbSXqaq4BRD6Bkyx2OE30UB";

	public static final String WEI_XIN_ID = "wxa46e2a70064a100c";
	public static final String WEI_XIN_SCRET = "5dac5159ff2c3e031a48ca9d16e52797";

	/** 商家向财付通申请的商家id */
	public static final String WEI_XIN_PARTNER_ID = "1490612852";

	public static final String QQ_APP_ID = "1106216115";

	public static final String SINA_APP_ID = "1560991402";

	public static final String SINA_APP_SECRET = "c3eb87e29d32604c6cb9d9256b497ebb";

	/** share channel start */
	public static final String SHARE_QQ_OK = "qq";// qq好友
	public static final String SHARE_QQZONE_OK = "qzone";// qq空间
	public static final String SHARE_WXSESSION_OK = "weixin";// 微信
	public static final String SHARE_WXCIRCLE_OK = "friend_circle";// 微信朋友圈
	/** share channel end */

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final String UMENG_ID = "536ca41f56240b0a7e0816b7";

	/** 邀请的头 */
	public static final String SHARE_INIVITE_HEAD = "http://www.yuanphone.com/down/invite";
	/** 分享头部地址 */
	public static final String SHARE_URL_HEAD = "http://www.yuanphone.com/down";
	/** 分享视频头部 */
	public static final String SHARE_VIDEO_HEAD = "http://www.yuanphone.com/down/video";

	public static final String PUSH_AIP_KEY = "hnwatVc2Lo3x9hfqSn2bRPbQ";

	public static final String PUSH_secret_key = "YXbb11Es1u98G2acmH5omIsHOZw8jRCR";

	public static final String FACE_PLUS_APIKEY = "396d5a51db89e3b8a528616f9a5ed885";
	public static final String FACE_PLUS_SECRET = "ubDoEF11s0tNirZ8BEdI4xNaablE-3On";

	public static final String RECIVER_MSG_NOTICE_KEY = "RECIVER_MSG_NOTICE_KEY";// 接收新消息通知
	public static final String VOICE_NOTIFY_KEY = "VOICE_NOTIFY_KEY";// 声音消息提示
	public static final String VIBRATE_NOTIFY_KEY = "VIBRATE_NOTIFY_KEY";// 振动消息提示

	public static final String IS_ALLOW_MATCH_FACE_KEY = "IS_ALLOW_MATCH_FACE_KEY";// 是否允许匹配
	public static final String IS_ALLOW_SEARCH_YOU_KEY = "IS_ALLOW_SEARCH_YOU_KEY";// 是否允许检索
	public static final String IS_HIDEN_DRACE_KEY = "IS_HIDEN_DRACE_KEY"; // 是否隐藏踪迹
	public static final String IS_PHONE_AUTH_KEY = "IS_PHONE_AUTH_KEY";// 手机认证
	public static final String IS_EMAIL_AUTH_KEY = "IS_EMAIL_AUTH_KEY";// 邮箱认证

	public static final String UPFACE_URL = "face_url";

	public static final String SPLIT_JING = "#";// 用户信息分割
}
