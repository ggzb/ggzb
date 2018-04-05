//package com.qixi.jiesihuo;
//
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.jack.lib.AppException;
//import com.jack.lib.net.RequestInformation;
//import com.jack.lib.net.callback.StringCallback;
//import com.jack.utils.Trace;
//import com.jack.utils.UrlHelper;
//import com.qixi.jiesihuo.msg.DealSocketMsg;
//import com.qixi.jiesihuo.msg.socket.SocketMsgFilter;
//
///**
// * Push消息处理receiver
// */
//
///*
// * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
// * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
// * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
// * 
// * 返回值中的errorCode，解释如下：0 - Success10001 - Network Problem10101 Integrate Check
// * Error30600 - Internal Server Error30601 - Method Not Allowed30602 - Request
// * Params Not Valid30603 - Authentication Failed30604 - Quota Use Up Payment
// * Required30605 -Data Required Not Found30606 - Request Time Expires Timeout
// * 30607 - Channel Token Timeout30608 - Bind Relation Not Found30609 - Bind
// * Number Too Many
// * 
// * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
// */
//
//public class MyPushMessageReceiver extends PushMessageReceiver {
//	/** TAG to Log */
//	public static final String TAG = MyPushMessageReceiver.class
//			.getSimpleName();
//
//	/**
//	 * 调用PushManager.startWork后，sdk将对push
//	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
//	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
//	 *
//	 * @param context
//	 *            BroadcastReceiver的执行Context
//	 * @param errorCode
//	 *            绑定接口返回值，0 - 成功
//	 * @param appid
//	 *            应用id。errorCode非0时为null
//	 * @param userId
//	 *            应用user id。errorCode非0时为null
//	 * @param channelId
//	 *            应用channel id。errorCode非0时为null
//	 * @param requestId
//	 *            向服务端发起的请求id。在追查问题时有用；
//	 * @return none
//	 */
//	@Override
//	public void onBind(Context context, int errorCode, String appid,
//			String userId, String channelId, String requestId) {
//		String responseString = "onBind errorCode=" + errorCode + " appid="
//				+ appid + " userId=" + userId + " channelId=" + channelId
//				+ " requestId=" + requestId;
//		Log.d(TAG, responseString);
//
//		if (errorCode == 0) {
//			// 绑定成功
//		}
//		Trace.d("baidu push error:" + errorCode + " appid:" + appid
//				+ " userId:" + userId + " channelId:" + channelId
//				+ " requestId:" + requestId);
//
//		if (userId != null
//				&& userId.trim().length() > 0
//				&& (AULiveApplication.getUserInfo() == null
//						|| AULiveApplication.getUserInfo().getDevice_token() == null || (AULiveApplication
//						.getUserInfo().getDevice_token() != null && !userId
//						.equals(AULiveApplication.getUserInfo()
//								.getDevice_token())))) {
//			Trace.d("上传百度token citylove token:" + userId);
//			if (AULiveApplication.getUserInfo() != null) {
//				Trace.d("FangChanApplication.getUserInfo():null");
//			}
//
//			if (AULiveApplication.getUserInfo() != null
//					&& AULiveApplication.getUserInfo().getDevice_token() != null) {
//				Trace.d("citylove token:"
//						+ AULiveApplication.getUserInfo().getDevice_token());
//			}
//			uploadUserId(userId);
//		} else {
//			if (userId != null) {
//				Trace.d("不上传百度token:" + userId);
//			}
//		}
//	}
//
//	/**
//	 * 接收透传消息的函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param message
//	 *            推送的消息
//	 * @param customContentString
//	 *            自定义内容,为空或者json字符串
//	 */
//	@Override
//	public void onMessage(Context context, String message,
//			String customContentString) {
//		String messageString = "透传消息 message=\"" + message
//				+ "\" customContentString=" + customContentString;
//		Log.d(TAG, messageString);
//
//		Trace.d("baidu push  onMessage message:" + message
//				+ " customeContentString:" + customContentString);
//		if (message != null && message.trim().length() > 0) {
//			DealSocketMsg dealMsg = new DealSocketMsg();
//			dealMsg.setNotifyContext(context);
//			SocketMsgFilter msgFilter = new SocketMsgFilter(new Handler(),
//					dealMsg);
//			msgFilter.filterMsg(message);
//		}
//
//		// // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
//		// if (!TextUtils.isEmpty(customContentString)) {
//		// JSONObject customJson = null;
//		// try {
//		// customJson = new JSONObject(customContentString);
//		// String myvalue = null;
//		// if (!customJson.isNull("mykey")) {
//		// myvalue = customJson.getString("mykey");
//		// }
//		// } catch (JSONException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// }
//		//
//		// // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		// updateContent(context, messageString);
//	}
//
//	/**
//	 * 接收通知点击的函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param title
//	 *            推送的通知的标题
//	 * @param description
//	 *            推送的通知的描述
//	 * @param customContentString
//	 *            自定义内容，为空或者json字符串
//	 */
//	@Override
//	public void onNotificationClicked(Context context, String title,
//			String description, String customContentString) {
//		String notifyString = "通知点击 title=\"" + title + "\" description=\""
//				+ description + "\" customContent=" + customContentString;
//		Log.d(TAG, notifyString);
//
//		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
//		if (!TextUtils.isEmpty(customContentString)) {
//			JSONObject customJson = null;
//			try {
//				customJson = new JSONObject(customContentString);
//				String myvalue = null;
//				if (!customJson.isNull("mykey")) {
//					myvalue = customJson.getString("mykey");
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, notifyString);
//	}
//
//	/**
//	 * 接收通知到达的函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param title
//	 *            推送的通知的标题
//	 * @param description
//	 *            推送的通知的描述
//	 * @param customContentString
//	 *            自定义内容，为空或者json字符串
//	 */
//
//	@Override
//	public void onNotificationArrived(Context context, String title,
//			String description, String customContentString) {
//
//		String notifyString = "onNotificationArrived  title=\"" + title
//				+ "\" description=\"" + description + "\" customContent="
//				+ customContentString;
//		Log.d(TAG, notifyString);
//
//		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
//		if (!TextUtils.isEmpty(customContentString)) {
//			JSONObject customJson = null;
//			try {
//				customJson = new JSONObject(customContentString);
//				String myvalue = null;
//				if (!customJson.isNull("mykey")) {
//					myvalue = customJson.getString("mykey");
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		// 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
//		updateContent(context, notifyString);
//	}
//
//	/**
//	 * setTags() 的回调函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
//	 * @param successTags
//	 *            设置成功的tag
//	 * @param failTags
//	 *            设置失败的tag
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onSetTags(Context context, int errorCode,
//			List<String> sucessTags, List<String> failTags, String requestId) {
//		String responseString = "onSetTags errorCode=" + errorCode
//				+ " sucessTags=" + sucessTags + " failTags=" + failTags
//				+ " requestId=" + requestId;
//		Log.d(TAG, responseString);
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * delTags() 的回调函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
//	 * @param successTags
//	 *            成功删除的tag
//	 * @param failTags
//	 *            删除失败的tag
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onDelTags(Context context, int errorCode,
//			List<String> sucessTags, List<String> failTags, String requestId) {
//		String responseString = "onDelTags errorCode=" + errorCode
//				+ " sucessTags=" + sucessTags + " failTags=" + failTags
//				+ " requestId=" + requestId;
//		Log.d(TAG, responseString);
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * listTags() 的回调函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示列举tag成功；非0表示失败。
//	 * @param tags
//	 *            当前应用设置的所有tag。
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onListTags(Context context, int errorCode, List<String> tags,
//			String requestId) {
//		String responseString = "onListTags errorCode=" + errorCode + " tags="
//				+ tags;
//		Log.d(TAG, responseString);
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * PushManager.stopWork() 的回调函数。
//	 *
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onUnbind(Context context, int errorCode, String requestId) {
//		String responseString = "onUnbind errorCode=" + errorCode
//				+ " requestId = " + requestId;
//		Log.d(TAG, responseString);
//
//		if (errorCode == 0) {
//			// 解绑定成功
//		}
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	private void updateContent(Context context, String content) {
//		Log.d(TAG, "updateContent");
//		// String logText = "" + Utils.logStringCache;
//		//
//		// if (!logText.equals("")) {
//		// logText += "\AllPoint";
//		// }
//		//
//		// SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
//		// logText += sDateFormat.format(new Date()) + ": ";
//		// logText += content;
//		//
//		// Utils.logStringCache = logText;
//		//
//		// Intent intent = new Intent();
//		// intent.setClass(context.getApplicationContext(),
//		// PushDemoActivity.class);
//		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// context.getApplicationContext().startActivity(intent);
//	}
//
//	private boolean isUpload = false;
//
//	private void uploadUserId(String userId) {
//		if (isUpload) {
//			return;
//		}
//		isUpload = true;
//		RequestInformation request = new RequestInformation(
//				UrlHelper.getUploadBaiduUser_Id(userId),
//				RequestInformation.REQUEST_METHOD_GET);
//		Trace.d("upload baidu token url:"
//				+ UrlHelper.getUploadBaiduUser_Id(userId));
//		request.setCallback(new StringCallback() {
//
//			@Override
//			public void onFailure(AppException e) {
//				// TODO Auto-generated method stub
//				isUpload = false;
//			}
//
//			@Override
//			public void onCallback(String callback) {
//				// TODO Auto-generated method stub
//				isUpload = false;
//
//				if (callback.indexOf("200") > 0) {
//					Trace.d("上传token成功");
//				} else {
//					Trace.d(callback);
//				}
//			}
//		});
//		request.execute();
//	}
//}
//
//// public class MyPushMessageReceiver extends PushMessageReceiver {
////
//// private NotificationCompat.Builder mBuilder;
////
//// /** Notification管理 */
//// public NotificationManager mNotificationManager;
////
//// private int notifyId = 100;
////
//// public static String backh_home_key = "BackHome";
//// public static String back_home_index_key = "back_home_index_key";
////
//// private boolean isUpload = false;
////
//// private void uploadUserId(String userId) {
//// if (isUpload) {
//// return;
//// }
//// isUpload = true;
//// RequestInformation request = new RequestInformation(
//// UrlHelper.getUploadBaiduUser_Id(userId),
//// RequestInformation.REQUEST_METHOD_GET);
//// Trace.d("upload baidu token url:"
//// + UrlHelper.getUploadBaiduUser_Id(userId));
//// request.setCallback(new StringCallback() {
////
//// @Override
//// public void onFailure(AppException e) {
//// // TODO Auto-generated method stub
//// isUpload = false;
//// }
////
//// @Override
//// public void onCallback(String callback) {
//// // TODO Auto-generated method stub
//// isUpload = false;
////
//// if (callback.indexOf("200") > 0) {
//// Trace.d("上传token成功");
//// } else {
//// Trace.d(callback);
//// }
//// }
//// });
//// request.execute();
//// }
////
//// @Override
//// public void onBind(Context context, int errorCode, String appid,
//// String userId, String channelId, String requestId) {
//// // TODO Auto-generated method stub
//// // context BroadcastReceiver的执行Context
//// // errorCode 绑定接口返回值，0 - 成功
//// // appid 应用id，errorCode非0时为null
//// // userId 应用user id，errorCode非0时为null
//// // channelId 应用channel id，errorCode非0时为null
//// // requestId 向服务端发起的请求id，在追查问题时有用
//// Trace.d("baidu push error:" + errorCode + " appid:" + appid
//// + " userId:" + userId + " channelId:" + channelId
//// + " requestId:" + requestId);
////
//// if (userId != null
//// && userId.trim().length() > 0
//// && (AULiveApplication.getUserInfo() == null
//// || AULiveApplication.getUserInfo().getDevice_token() == null ||
//// (AULiveApplication
//// .getUserInfo().getDevice_token() != null && !userId
//// .equals(AULiveApplication.getUserInfo()
//// .getDevice_token())))) {
//// Trace.d("上传百度token citylove token:" + userId);
//// if (AULiveApplication.getUserInfo() != null) {
//// Trace.d("FangChanApplication.getUserInfo():null");
//// }
////
//// if (AULiveApplication.getUserInfo() != null
//// && AULiveApplication.getUserInfo().getDevice_token() != null) {
//// Trace.d("citylove token:"
//// + AULiveApplication.getUserInfo().getDevice_token());
//// }
//// uploadUserId(userId);
//// } else {
//// if (userId != null) {
//// Trace.d("不上传百度token:" + userId);
//// }
//// }
////
//// }
////
//// @Override
//// public void onDelTags(Context arg0, int arg1, List<String> arg2,
//// List<String> arg3, String arg4) {
//// // TODO Auto-generated method stub
//// Trace.d("baidu push onDelTags");
//// }
////
//// @Override
//// public void onListTags(Context arg0, int arg1, List<String> arg2,
//// String arg3) {
//// // TODO Auto-generated method stub
//// Trace.d("baidu push onListTags");
//// }
////
//// @Override
//// public void onMessage(Context context, String message,
//// String customContentString) {
//// // TODO Auto-generated method stub
//// // message 推送的消息
//// // customContentString 自定义内容，为空或者json字符串
////
//// }
////
//// @Override
//// public void onNotificationArrived(Context arg0, String arg1, String arg2,
//// String arg3) {
//// // TODO Auto-generated method stub
//// Trace.d("baidu push onNotificationArrived");
//// }
////
//// @Override
//// public void onNotificationClicked(Context arg0, String arg1, String arg2,
//// String arg3) {
//// // TODO Auto-generated method stub
//// Trace.d("baidu push onNotificationClicked");
//// }
////
//// @Override
//// public void onSetTags(Context arg0, int arg1, List<String> arg2,
//// List<String> arg3, String arg4) {
//// // TODO Auto-generated method stub
//// Trace.d("baidu push onSetTags");
//// }
////
//// @Override
//// public void onUnbind(Context arg0, int arg1, String arg2) {
//// // TODO Auto-generated method stub
//// Trace.d("baidu push onUnbind");
//// }
////
// // }
