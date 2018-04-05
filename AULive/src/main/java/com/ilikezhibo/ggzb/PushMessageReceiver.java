package com.ilikezhibo.ggzb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.baidu.android.pushservice.PushConstants;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		if (intent == null) {
			return;
		}

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// 获取消息内容
			String message = null;
			try {
				message = intent.getExtras().getString(
						PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 消息的用户自定义内容读取方式
			// Trace.d("onMessage: " + message);
			//
			// // 自定义内容的json串
			// Trace.d("EXTRA_EXTRA = "
			// + intent.getStringExtra(PushConstants.EXTRA_EXTRA));
			// Prepare intent which is triggered if the
			// notification is selected
			Trace.d("push message:" + message);

			// if (SharedPreferenceTool.getInstance().getBoolean(
			// Constants.RECIVER_MSG_NOTICE_KEY, false)) {
			// startNotification(message, context);
			// }
			// String msg = null;
			// String nickname = null;
			// int msgType = 0;
			// int recv = 0;
			// int uid = 0;
			// String id = null;
			// String time = null;
			// try {
			// JSONObject json = new JSONObject(message);
			// msg = json.optString("msg");
			// nickname = json.optString("nickname");
			// msgType = json.optInt("msg_type");
			// recv = json.optInt("recv");
			// uid = json.optInt("uid");
			// id = json.optString("id");
			// time = json.optString("time");
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			//
			// String mid = Utils.getChatMid(recv + "", uid + "");
			//
			//
			// // 存本地
			// DBChatMsgEntity chatEntity = new DBChatMsgEntity();
			// chatEntity
			// .setAdd_time(time);
			// chatEntity.setMsg(msg);
			// chatEntity
			// .setSendState(DBChatMsgEntity.SENDING_MSG);
			// chatEntity.setId(id);
			// chatEntity.setSend_uid(uid+"");
			// chatEntity.setType(msgType);
			// ChatMsgDBManager.getInstance().insertChatMsgToDb(chatEntity,
			// mid);
			//
			// DBChatLstEntity chatRoomEntity =
			// DBChatLstManager.getInstance().getCacheEntity(uid);
			// if (chatRoomEntity != null) {
			//
			// }else{
			//
			// }

			// mSocketMsgFilter.filterMsg(message);
			// startNotification(message, context);

			// 用户在此自定义处理消息,以下代码为demo界面展示用
			// Intent responseIntent = null;
			// responseIntent = new Intent(Utils.ACTION_MESSAGE);
			// responseIntent.putExtra(Utils.EXTRA_MESSAGE, message);
			// responseIntent.setClass(context, HomeActivity.class);
			// responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(responseIntent);

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// 处理绑定等方法的返回数据
			// PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到

			// 获取方法
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			// 方法返回错误码。若绑定返回错误（非0），则应用将不能正常接收消息。
			// 绑定失败的原因有多种，如网络原因，或access token过期。
			// 请不要在出错时进行简单的startWork调用，这有可能导致死循环。
			// 可以通过限制重试次数，或者在其他时机重新调用来解决。
			int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
					PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				// 返回内容
				content = new String(
						intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}

			// 用户在此自定义处理消息,以下代码为demo界面展示用
			// Trace.d("onMessage: method : " + method);
			// Trace.d("onMessage: result : " + errorCode);
			// Trace.d("onMessage: content : " + content);

			try {
				if ("method_bind".equals(method)) {
					JSONObject json = new JSONObject(content);
					Trace.d("百度推送  content:" + content);
					String userId = json.getJSONObject("response_params")
							.optString("user_id");

					if (userId != null
							&& userId.trim().length() > 0
							&& (AULiveApplication.getUserInfo() == null
									|| AULiveApplication.getUserInfo()
											.getDevice_token() == null || (AULiveApplication
									.getUserInfo().getDevice_token() != null && !userId
									.equals(AULiveApplication.getUserInfo()
											.getDevice_token())))) {
						Trace.d("上传百度token citylove token:");
						if (AULiveApplication.getUserInfo() != null
								&& AULiveApplication.getUserInfo()
										.getDevice_token() != null) {
							Trace.d("citylove token:"
									+ AULiveApplication.getUserInfo()
											.getDevice_token());
						}
						uploadUserId(userId);
					} else {
						if (userId != null) {
							Trace.d("不上传百度token:" + userId);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Toast.makeText(context, "method : " + method + "\AllPoint result: " +
			// errorCode + "\AllPoint content = " + content,
			// Toast.LENGTH_SHORT).show();

			// Intent responseIntent = null;
			// responseIntent = new Intent(Utils.ACTION_RESPONSE);
			// responseIntent.putExtra(Utils.RESPONSE_METHOD, method);
			// responseIntent.putExtra(Utils.RESPONSE_ERRCODE,
			// errorCode);
			// responseIntent.putExtra(Utils.RESPONSE_CONTENT, content);
			// responseIntent.setClass(context, HomeActivity.class);
			// responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(responseIntent);

			// 可选。通知用户点击事件处理
		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			Trace.d("intent=" + intent.toUri(0));

			// 自定义内容的json串
			Trace.d("EXTRA_EXTRA = "
					+ intent.getStringExtra(PushConstants.EXTRA_EXTRA));

			// Intent aIntent = new Intent();
			// aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// aIntent.setClass(context, HomeActivity.class);
			// String title = intent
			// .getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
			// aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
			// String content = intent
			// .getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
			// aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT,
			// content);
			//
			// context.startActivity(aIntent);
		}
	}

	private boolean isUpload = false;

	private void uploadUserId(String userId) {
		if (isUpload) {
			return;
		}
		isUpload = true;
		RequestInformation request = new RequestInformation(
				UrlHelper.getUploadBaiduUser_Id(userId),
				RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new StringCallback() {

			@Override
			public void onFailure(AppException e) {
				isUpload = false;
			}

			@Override
			public void onCallback(String callback) {
				isUpload = false;

				if (callback.indexOf("200") > 0) {
					Trace.d("上传token成功");
				} else {
					Trace.d(callback);
				}
			}
		});
		request.execute();
	}

	// private void startNotification(String message, Context context) {
	// //
	// message:{"msg":"估计","recv":16044508,"msg_type":0,"type":"priv","uid":17312604,"time":1402388778,"id":"140238877838817312604","nickname":"妖娆"}
	// //
	// {"msg":"腊八","recv":16044508,"msg_type":0,"type":"priv","uid":10000,"time":1403006826,"id":"140300682662910000","nickname":"同城热恋","badge":1}
	//
	// NotificationManager mNotifMan = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Intent intent = new Intent(context, CityLoveHomeActivity.class);
	// intent.putExtra(Constants.KEY_OPEN_MSG, true);
	// // intent.putExtra(CityLoveHomeActivity.INTENT_CHAT_MID_KEY, mid);
	// // intent.putExtra(CityLoveHomeActivity.INTENT_FRIEND_UID_KEY, uid +
	// // "");
	// // intent.putExtra(CityLoveHomeActivity.INTENT_FRIEND_NAME_KEY,
	// // nickname);
	// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
	// intent, PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// Notification notification = new Notification();
	// notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// if (SharedPreferenceTool.getInstance().getBoolean(
	// Constants.VIBRATE_NOTIFY_KEY, true)) {
	// long[] vibrate = { 0, 100, 200, 300 };
	// ;
	// notification.vibrate = vibrate;
	// }
	//
	// if (SharedPreferenceTool.getInstance().getBoolean(
	// Constants.VOICE_NOTIFY_KEY, true)) {
	// notification.sound = Uri.parse("android.resource://"
	// + MobileConfig.getMobileConfig(context).getPackageName()
	// + "/" + R.raw.ms);
	// }
	//
	// if (DBChatLstManager.getInstance().getChatRoomAllMsgTotal() > 1) {
	// message = "您有"
	// + DBChatLstManager.getInstance().getChatRoomAllMsgTotal()
	// + "未读消息";
	// } else {
	// if (msgType == SocketPrivEntity.MSG_TYPE) {
	// message = msg;
	// } else if (msgType == SocketPrivEntity.PIC_TYPE) {
	// message = "图片";
	// } else if (msgType == SocketPrivEntity.POSTION_TYPE) {
	// message = "位置";
	// } else if (msgType == SocketPrivEntity.RECORD_TYPE) {
	// message = "语音";
	// } else if (msgType == SocketPrivEntity.VIDEO_TYPE) {
	// message = "视频";
	// } else if (msgType == SocketPrivEntity.ANIM_PIC_TYPE) {
	// message = "动画";
	// } else if (msgType == SocketPrivEntity.WEB_TYPE) {
	// message = "网页";
	// }
	//
	// }
	// if (uid == 10000) {
	// notification.icon = R.drawable.ic_launcher;
	// notification.when = System.currentTimeMillis();
	//
	// RemoteViews remoteView = new RemoteViews(context.getPackageName(),
	// R.layout.city_love_nofication);
	// remoteView.setImageViewResource(R.id.notificationIcon,
	// R.drawable.ic_launcher);
	// remoteView.setTextViewText(R.id.systemTitleTv,
	// Utils.trans(R.string.app_name));
	// remoteView.setTextViewText(R.id.systemContentNotifiTv, message);
	// notification.contentView = remoteView;
	// notification.contentIntent = pendingIntent;
	// } else {
	// notification.icon = R.drawable.ic_launcher;
	// notification.when = System.currentTimeMillis();
	//
	// notification.tickerText = message;
	// notification
	// .setLatestEventInfo(
	// context,
	// DBChatLstManager.getInstance()
	// .getChatRoomAllMsgTotal() > 1 ? Utils
	// .trans(R.string.app_name) : nickname,
	// message, pendingIntent);
	// }
	// mNotifMan.notify(Constants.NATIVE_PUSH_NOTIFICATION_ID, notification);
	// }
	//
	// // 消息通知
	// private void startNotification(DBChatLstEntity entity, Context context) {
	// Trace.d("socket isapponforeground:" + Utils.isAppOnForeground(context));
	// NotificationManager mNotifMan = (NotificationManager)
	// CityLoveApplication.mContext
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Intent intent = new Intent(context, CityLoveHomeActivity.class);
	// intent.putExtra(Constants.KEY_OPEN_MSG, true);
	// // intent.putExtra(ChatMsgActivity.INTENT_MSG_ID_KEY, entity.getMid());
	// // intent.putExtra(ChatMsgActivity.INTENT_FRIEND_NAME_KEY,
	// // entity.getNickname());
	// // intent.putExtra(ChatMsgActivity.INTENT_FRIEND_UID_KEY,
	// // entity.getUid());
	// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
	// intent, PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// Notification notification = new Notification();
	// notification.flags |= Notification.FLAG_SHOW_LIGHTS;
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// if (SharedPreferenceTool.getInstance().getBoolean(
	// Constants.VIBRATE_NOTIFY_KEY, true)) {
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// }
	//
	// if (SharedPreferenceTool.getInstance().getBoolean(
	// Constants.VOICE_NOTIFY_KEY, true)) {
	// notification.sound = Uri.parse("android.resource://"
	// + MobileConfig.getMobileConfig(context).getPackageName()
	// + "/" + R.raw.ms);
	// }
	//
	// if ("10000".equals(entity.getSendUid())) {
	// notification.icon = R.drawable.ic_launcher;
	// notification.when = System.currentTimeMillis();
	//
	// RemoteViews remoteView = new RemoteViews(context.getPackageName(),
	// R.layout.city_love_nofication);
	// remoteView.setImageViewResource(R.id.notificationIcon,
	// R.drawable.ic_launcher);
	// remoteView.setTextViewText(R.id.systemTitleTv,
	// Utils.trans(R.string.app_name));
	// remoteView.setTextViewText(R.id.systemContentNotifiTv,
	// entity.getMsg());
	// notification.contentView = remoteView;
	// notification.contentIntent = pendingIntent;
	// } else {
	// notification.icon = R.drawable.ic_launcher;
	// notification.when = System.currentTimeMillis();
	// notification.tickerText = EmoticonManager.getInstance(context)
	// .convertMsg(
	// entity.getNickname() + "发来消息：" + entity.getMsg());
	//
	// notification
	// .setLatestEventInfo(
	// context,
	// DBChatLstManager.getInstance()
	// .getChatRoomAllMsgTotal() > 1 ? Utils
	// .trans(R.string.app_name) : entity
	// .getNickname(),
	// "您有"
	// + DBChatLstManager.getInstance()
	// .getChatRoomAllMsgTotal() + "未读取消息",
	// pendingIntent);
	// }
	// mNotifMan.notify(Constants.NATIVE_PUSH_NOTIFICATION_ID, notification);
	// }

}
