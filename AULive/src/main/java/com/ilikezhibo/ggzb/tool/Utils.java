package com.ilikezhibo.ggzb.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jack.utils.FileUtil;
import com.jack.utils.MobileConfig;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author jack.long
 *
 *         工具
 */
public class Utils {
	private static final String HEAD_FACE_URL = "http://img.yuanphone.com/";
	public static boolean isBackVideoHall = false;// 是否返回主播大厅
	public static boolean isUpdateUserInfo = false;// 是否更新用户信息
	public static boolean isInitVideoEnvorinment = false;// 是否已经初始化环境
	public static boolean isSocketConnected = false;// socket是否连接成功状态
	public static boolean isSocketLogin = false;// 用户是否登录成功
	public static boolean isAppForGround = false;// 是否在前台运行

	public static boolean isVistorUI = false;// 是否在访问者的UI
	public static boolean isShowNewVistor = false;// 是否显示访问者
	public static boolean isShowVIP = false;// 显示vip的状态
	public static boolean isShowCredit = false;// 显示访问者的状态
	public static boolean isOnMyInfoFragmentUI = false;// 是否在我的菜單下 標誌new是否現

	public static boolean isAgainLogin = false;

	public static boolean isAttent = false;

	public static boolean isCoupleFace = false;// 是否当前在夫妻相界面

	public static boolean isCreditFace = false;

	public static boolean IS_CLICK = false;

	public static int CURR_DATES_DETAIL_ID;// 当前约会详情id

	public static boolean isExitLogin;// 退出登陆

	// push utils
	public static final String TAG = "PushDemoActivity";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	public static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	// 检查网络是否可用
	public static boolean checkNetworkIsAvailable() {
		ConnectivityManager manager = (ConnectivityManager) AULiveApplication.mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {// 当前网络不可用
			return false;
		} else {
			return true;
		}
	}

	// 获取AppKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String key = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				key = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return key;
	}

	public static String trans(int id) {
		if (id == 0) {
			return null;
		}
		return AULiveApplication.mContext.getString(id);
	}

	public static String trans(int id, Object... args) {
		if (id == 0) {
			return null;
		}
		return AULiveApplication.mContext.getString(id, args);
	}

	public static String getImgUrl(String imgUrl) {
		if (imgUrl != null && imgUrl.contains("http")) {
			return imgUrl;
		} else {
			return HEAD_FACE_URL + imgUrl;
		}
	}

	public static int getStringResId(String name) {
		return AULiveApplication.mContext.getResources().getIdentifier(name,
				"string", AULiveApplication.mContext.getPackageName());
	}

	public static String translateCustomerInformation(String description) {
		String pluralName = "SyncObjectKPI_" + description;
		Resources resources = AULiveApplication.mContext.getResources();
		int id = resources.getIdentifier(pluralName, "string",
				"com.coresuite.android");
		if (id != 0) {
			return trans(id);
		}
		return pluralName;
	}

	/** make sure all the given object params is null */
	public static boolean assertAllNULL(Object... objs) {
		for (Object obj : objs) {
			if (obj != null) {
				return false;
			}
		}
		return true;
	}

	public static void showMessage(String msg) {
		// Trace.d("msg:" + msg);
		try {
			Toast.makeText(AULiveApplication.mContext, msg,
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showCenterMessage(String msg) {
		// Trace.d("msg:" + msg);
		try {
			Toast toast = Toast.makeText(AULiveApplication.mContext, msg,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showMessageLong(String msg) {
		// Trace.d("msg:" + msg);
		Toast.makeText(AULiveApplication.mContext, msg, Toast.LENGTH_LONG)
				.show();
	}

	public static int dip2px(Context paramContext, float paramFloat) {
		return (int) (paramFloat * paramContext.getResources()
				.getDisplayMetrics().density);
	}

	public static String getResorcString(int resId) {
		return AULiveApplication.mContext.getResources().getString(resId);
	}

	/** 推出动画 */
	public static void exitAnimation(Activity activity) {
		activity.overridePendingTransition(R.anim.activity_close_enter,
				R.anim.activity_close_exit);
	}

	/** 清空缓存图片 */
	public static void clearCacheImg() {
		new Thread() {
			public void run() {
				FileUtil.deleteFileDir(FileUtil.ROOTPATH, true);
			}
		}.start();
	}

	/** 时间转换（return yyyy-MM-dd || HH:mm:ss） */
	public static String convertDate(long dateTime) {
		String result = "";
		SimpleDateFormat sdfDetail = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String oldTime = sdfDetail.format(new Date(dateTime * 1000));

		String currTime = sdfDetail.format(new Date().getTime());

		String[] oldStr = oldTime.split(" ");
		String[] newStr = currTime.split(" ");
		String oldYearTime = oldStr[0];
		String oldHourTime = oldStr[1];

		String newYearTime = newStr[0];
		String newHourTime = newStr[1];

		if (oldYearTime.equals(newYearTime)) {
			// String hour = oldHourTime.substring(0, 2);
			// if (Integer.parseInt(hour) <= 5) {
			// result += "凌晨 ";
			// } else if (Integer.parseInt(hour) <= 12) {
			// result = "上午 ";
			// } else if (Integer.parseInt(hour) <= 13) {
			// result = "中午 ";
			// } else if (Integer.parseInt(hour) <= 18) {
			// result = "下午 ";
			// } else {
			// result = "晚上 ";
			// }

			String[] timeStrs = oldHourTime.split(":");
			String oldHourStr = timeStrs[0];
			String oldMinStr = timeStrs[1];
			// String[] newTimeSts = newHourTime.split(":");
			// String newHourStr = newTimeSts[0];
			// String newMinStr = newTimeSts[1];
			// if (oldHourStr.equals(newHourStr)) {
			// int time = Integer.parseInt(newMinStr)
			// - Integer.parseInt(oldMinStr);
			// if (time <= 5) {
			// result = "刚刚";
			// } else {
			// result = time + "分钟前";
			// }
			// } else {
			// int hour = Integer.parseInt(newHourStr)
			// - Integer.parseInt(oldHourStr);
			// result = hour + "小时前";
			// }
			result = oldHourStr + ":" + oldMinStr;
		} else {
			String[] oldYearDetailStrs = oldYearTime.split("-");
			String[] newYearDetailStrs = newYearTime.split("-");
			String[] timeStrs = oldHourTime.split(":");
			String oldHourStr = timeStrs[0];
			String oldMinStr = timeStrs[1];
			// if (oldYearDetailStrs[0].equals(newYearDetailStrs[0])
			// && oldYearDetailStrs[1].equals(newYearDetailStrs[1])
			// && Integer.parseInt(oldYearDetailStrs[2]) == (Integer
			// .parseInt(newYearDetailStrs[2]) - 1)) {
			// result = oldYearDetailStrs[1] + "-" + oldYearDetailStrs[2]
			// +" "+oldHourTime;
			//
			// // String hour = oldHourTime.substring(0, 2);
			// // if (Integer.parseInt(hour) <= 5) {
			// // result += " 凌晨 ";
			// // } else if (Integer.parseInt(hour) <= 12) {
			// // result += " 上午 ";
			// // } else if (Integer.parseInt(hour) <= 13) {
			// // result += " 中午 ";
			// // } else if (Integer.parseInt(hour) <= 18) {
			// // result += " 下午 ";
			// // } else {
			// // result += " 晚上 ";
			// // }
			// //
			// // result += oldHourTime;
			// } else {
			if (Integer.parseInt(newYearDetailStrs[0]) > Integer
					.parseInt(oldYearDetailStrs[0])) {
				result = oldYearTime + " " + oldHourStr + ":" + oldMinStr;
			} else {
				result = oldYearDetailStrs[1] + "-" + oldYearDetailStrs[2]
						+ " " + oldHourStr + ":" + oldMinStr;
			}
			// }
		}

		return result;
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Context activity) {
		ActivityManager activityManager = (ActivityManager) activity
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = activity.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * getChatMid:
	 * 
	 * @Description: 组合聊天的mid
	 * 
	 * @return 聊天mid
	 */
	public static String getChatMid(String recvId, String uid) {
		if (recvId == null || uid == null) {
			return null;
		} else {
			if (Integer.parseInt(recvId) > Integer.parseInt(uid)) {
				return uid + "." + recvId;
			} else {
				return recvId + "." + uid;
			}
		}
	}

	/**
	 * 半角转换为全角(优化排版)
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		if (input == null || input.trim().length() <= 0) {
			return input;
		}

		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/** 获取朋友id */
	public static String getFriendUid(String mid) {
		if (mid == null || mid.trim().length() <= 0) {
			return null;
		}
		String[] strs = mid.split("\\.");
		if (strs.length != 2) {
			return null;
		} else {
			if (strs[0].equals(AULiveApplication.getUserInfo().getUid())) {
				return strs[1];
			} else {
				return strs[0];
			}
		}
	}

	/**
	 * 
	 * @param plainText
	 *            明文
	 * @return 32位密文
	 */
	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	public static void updateApp(final Activity activity) {
		// 所有网络情况都可以更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// 检测版本更新
		UmengUpdateAgent.update(activity);

		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int arg0, UpdateResponse arg1) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0: // has update
					SharedPreferenceTool.getInstance().saveBoolean(
							SharedPreferenceTool.IS_HAS_NEW_APP, true);
					UmengUpdateAgent.showUpdateDialog(activity, arg1);
					break;
				case 1: // has no update
					SharedPreferenceTool.getInstance().saveBoolean(
							SharedPreferenceTool.IS_HAS_NEW_APP, false);
					Trace.d("已经是最新版本了.");
					break;
				// case 2: // none wifi
				// Utils.showMessage("没有wifi连接， 只在wifi下更新");
				// break;
				case 3: // time out
					// Utils.showMessage("超时");
					break;
				}
			}
		});
	}

	public static int getMarriageValue(String marriageName) {
		if ("未婚".equals(marriageName)) {
			return 0;
		} else if ("已婚".equals(marriageName)) {
			return 1;
		} else if ("离异".equals(marriageName)) {
			return 2;
		} else if ("丧偶".equals(marriageName)) {
			return 3;
		}

		return 0;
	}

	public static String getMarriageName(int value) {
		String result = "未婚";
		switch (value) {
		case 0:
			result = "未婚";
			break;
		case 1:
			result = "已婚";
			break;
		case 2:
			result = "离异";
			break;
		case 3:
			result = "丧偶";
			break;

		}
		return result;
	}

	/**
	 * check the app is installed
	 */
	public static boolean isAppInstalled(Context context, String packagename) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			// System.out.println("没有安装");
			return false;
		} else {
			// System.out.println("已经安装");
			return true;
		}
	}

	/**
	 * 回收LAYOUT
	 * 
	 * @param view
	 */
	public static void unbindLayout(View view) {
		unbindDrawables(view);
		System.gc();
		// Runtime.getRuntime().gc();
	}

	private static void unbindDrawables(View view) {
		if (view == null) {
			return;
		}

		try {
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}

			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}

				if (view != null) {
					try {
						((ViewGroup) view).removeAllViews();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * getMapUrl:
	 * 
	 * @Description: 获取静态图片url
	 * 
	 * @author jack.long
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static String getMapUrl(String lat, String lon, int width,
			int height, String address) {
		Trace.d("");
		// http://api.map.baidu.com/staticimage?center=116.403874,39.914889&width=400&height=300&zoom=11&markers=116.288891,40.004261
		String path = "http://api.map.baidu.com/staticimage?center="
				+ lon
				+ ","
				+ lat
				+ "&zoom=11&width="
				+ width
				* MobileConfig.getMobileConfig(AULiveApplication.mContext)
						.getDensity()
				+ "&height="
				+ height
				* MobileConfig.getMobileConfig(AULiveApplication.mContext)
						.getDensity() + "&markers=" + lon + "," + lat
				+ "&labels=" + lon + "," + lat + "&labelStyles=" + address
				+ ",1,12,0xff000000,0xff00,1";
		Trace.d("map path:" + path);
		return path;
	}

	/***
	 * 
	 * changeFonts:
	 * 
	 * @Description: 改变字体
	 * 
	 * @author jack.long
	 * @param root
	 * @param act
	 */
	public static void changeFonts(ViewGroup root, Activity act) {

		Typeface tf = Typeface
				.createFromAsset(act.getAssets(), "fonts/xxx.ttf");

		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act);
			}
		}
	}

	/** 魅力级别 */
	public static int getCharmLevelResourceId(int charmLevelId) {
		int indentify = AULiveApplication.mContext.getResources()
				.getIdentifier("charm_level_" + charmLevelId, "drawable",
						AULiveApplication.mContext.getPackageName());
		if (indentify > 0) {
			return indentify;
		}
		return 0;
	}

	public static boolean isLogin(Activity activity) {
		String uid = AULiveApplication.getUserInfo().getUid();
		String nickname = AULiveApplication.getUserInfo().getNickname();
		if (AULiveApplication.getUserInfo() == null || uid == null
				|| uid.equals("") || uid.equals("0")) {
			Intent login_intent = new Intent(activity, LoginActivity.class);
			login_intent.putExtra(LoginActivity.back_home_key, false);
			activity.startActivity(login_intent);
			return false;
		}
		return true;
	}
//	HH:mm:ss
	public static String timeExchange(long time) {
		SimpleDateFormat format =  new SimpleDateFormat("yyyy年-MM月-dd日");
		time *= 1000L;
		Date date = new Date(time);
		String string = format.format(date);
		return string;
	}
}
