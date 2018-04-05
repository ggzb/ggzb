package com.ilikezhibo.ggzb.wxapi;
//package com.qixi.citylove.wxapi;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
//import android.app.Activity;
//import android.widget.Toast;
//
//import com.jack.lib.AppException;
//import com.jack.lib.net.RequestInformation;
//import com.jack.lib.net.callback.StringCallback;
//import com.jack.utils.Constants;
//import com.jack.utils.Trace;
//import com.jack.utils.UrlHelper;
//import com.jack.utils.Utils;
//import com.qixi.citylove.R;
//import com.umeng.analytics.social.UMSocialService;
//
///**
// * @ClassName: UMengShareHelper
// * @Description: 友盟分享
// * @author big
// * @date 2014-6-19 上午11:27:15
// * 
// */
//public class UMengShareHelper {
//	private static UMengShareHelper instance;
//	private UMSocialService mController;
//	private SnsPostListener snsPostListener;
//
//	private UMengShareHelper() {
//		init();
//	}
//
//	public static UMengShareHelper getInstance() {
//		if (instance == null) {
//			instance = new UMengShareHelper();
//		}
//		return instance;
//	}
//
//	private void init() {
//		mController = UMServiceFactory.getUMSocialService("com.umeng.share",
//				RequestType.SOCIAL);
//		createShareListener();
//		registerListener();
//	}
//
//	public void doUmeng(Activity activity, String share_url, String shareTitle) {
//		// 分享点击跳转的url
//		String target_url = null;
//		try {
//			target_url = Constants.SHARE_CONTENT + "/?url="
//					+ URLEncoder.encode(share_url, "utf-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//		SocializeConstants.APPKEY = Constants.UMENG_ID;
//		// 首先在您的Activity中添加如下成员变量
//		// final UMSocialService mController = UMServiceFactory
//		// .getUMSocialService("com.umeng.share", RequestType.SOCIAL);
//
//		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		String appID = Constants.WEI_XIN_ID;
//		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
//		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(
//				activity, appID, target_url);
//		// 设置分享标题
//		wxHandler.setWXTitle(shareTitle);
//
//		// 支持微信朋友圈
//		UMWXHandler circleHandler = mController.getConfig()
//				.supportWXCirclePlatform(activity, appID, target_url);
//		circleHandler.setCircleTitle(shareTitle);
//
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent(target_url);
//		qqShareContent.setTitle(shareTitle);
//		qqShareContent.setShareImage(new UMImage(activity, share_url));
//		qqShareContent.setTargetUrl(target_url);
//		mController.setShareMedia(qqShareContent);
//
//		QZoneShareContent qZoneShareContent = new QZoneShareContent();
//		qZoneShareContent.setShareContent(target_url);
//		qZoneShareContent.setTitle(shareTitle);
//		qZoneShareContent.setShareImage(new UMImage(activity, share_url));
//		qZoneShareContent.setTargetUrl(target_url);
//		mController.setShareMedia(qZoneShareContent);
//
//		// 参数1为当前Activity， 参数2为用户点击分享内容时跳转到的目标地址
//		mController.getConfig().supportQQPlatform(activity, target_url);
//		mController.getConfig().setSsoHandler(new QZoneSsoHandler(activity));
//		// 设置腾讯微博SSO handler
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN);
//
//		// // 设置分享内容
//		mController.setShareContent(target_url);
//		// 设置分享图片, 参数2为图片的url地址
//		mController.setShareMedia(new UMImage(activity, share_url));
//		mController.openShare(activity, false);
//	}
//
//	public void shareToSms(final Activity activity, String inviteCodeStr) {
//		SmsShareContent smsShareContent = new SmsShareContent();
//		smsShareContent
//				.setShareContent("智能人脸匹配技术，真实的个人信息，免费约会相亲交友神器，你也安装一个吧，输入邀请码"
//						+ inviteCodeStr
//						+ "就可以一起玩了。 下载地址 http://www.yuanphone.com/down/invite?id="
//						+ inviteCodeStr + " (下载安装成功后注册完成再点击即邀请成功)");
//		mController.setShareMedia(smsShareContent);
//
//		mController.postShare(activity, SHARE_MEDIA.SMS, new SnsPostListener() {
//			@Override
//			public void onStart() {
//				Toast.makeText(activity, "开始分享.", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				if (eCode == 200) {
//					Toast.makeText(activity, "分享成功.", Toast.LENGTH_SHORT)
//							.show();
//				} else {
//					String eMsg = "";
//					if (eCode == -101) {
//						eMsg = "没有授权";
//					}
//					Toast.makeText(activity, "分享失败[" + eCode + "] " + eMsg,
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//	}
//
//	public void shareToQQ(final Activity mContext) {
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent("智能人脸匹配技术，真实的个人信息，免费约会相亲交友神器.");
//		qqShareContent.setTitle(Utils.trans(R.string.app_name));
//		qqShareContent.setTargetUrl("http://www.yuanphone.com/down/invite");
//		qqShareContent.setShareMedia(new UMImage(mContext,
//				R.drawable.ic_launcher));
//		mController.setShareMedia(qqShareContent);
//		// mController
//		// .getConfig()
//		// .supportQQPlatform(
//		// mContext,
//		// "智能人脸匹配技术，真实的个人信息，免费约会相亲交友神器，你也安装一个吧，输入邀请码1212121就可以一起玩了。 下载地址 http://www.yuanphone.com/down/invite");
//
//		mController.postShare(mContext, SHARE_MEDIA.QQ, new SnsPostListener() {
//			@Override
//			public void onStart() {
//				Toast.makeText(mContext, "开始分享.", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				if (eCode == 200) {
//					Toast.makeText(mContext, "分享成功.", Toast.LENGTH_SHORT)
//							.show();
//				} else {
//					String eMsg = "";
//					if (eCode == -101) {
//						eMsg = "没有授权";
//					}
//					Toast.makeText(mContext, "分享失败[" + eCode + "] " + eMsg,
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//	}
//
//	public UMSocialService getmController() {
//		return mController;
//	}
//
//	private void registerListener() {
//		this.mController.registerListener(snsPostListener);
//	}
//
//	public void unregisterListener() {
//		this.mController.unregisterListener(snsPostListener);
//	}
//
//	private void createShareListener() {
//		snsPostListener = new SnsPostListener() {
//
//			@Override
//			public void onStart() {
//				Trace.d("开始分享");
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				// Utils.showMessage("eCode:" + eCode + "platform:" + platform
//				// + " SHARE_MEDIA:" + SHARE_MEDIA.EMAIL.toString());
//				uploadShareChannel(platform.toString());
//				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//					Utils.showMessage("分享成功！");
//					Trace.d("分享成功 platform:" + platform + " platform:");
//					// shareReturn(1);
//				} else {
//					Trace.d("分享失败 platform:" + platform + " platform:");
//					// shareReturn(0);
//				}
//			}
//		};
//	}
//
//	private void uploadShareChannel(final String channel) {
//		RequestInformation request = new RequestInformation(
//				UrlHelper.SHARE_CHANNEL + channel,
//				RequestInformation.REQUEST_METHOD_GET);
//		request.setCallback(new StringCallback() {
//
//			@Override
//			public void onFailure(AppException e) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onCallback(String callback) {
//				// TODO Auto-generated method stub
//				Trace.d("channel:" + channel);
//			}
//		});
//		request.execute();
//	}
//
// }
