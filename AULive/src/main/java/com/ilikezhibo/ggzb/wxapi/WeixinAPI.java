package com.ilikezhibo.ggzb.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.jack.utils.AppConstants;
import com.ilikezhibo.ggzb.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.io.ByteArrayOutputStream;

/**
 * @filename SendToWxManager.java
 * @author
 * @time 2012-12-26 下午1:54:47
 * @desp 微信API
 */
public class WeixinAPI {
	public final static String APPID = AppConstants.WEI_XIN_ID;
	private static WeixinAPI instance = null;
	private IWXAPI wxApi = null;
	private Context context;

	private String shareContent;
	private String shareTitle;

	private WeixinAPI(Context context) {
		this.context = context;
		wxApi = WXAPIFactory.createWXAPI(context, APPID, true);
	}

	public static WeixinAPI getInstance(Context context) {
		if (instance == null) {
			instance = new WeixinAPI(context);
		}

		return instance;
	}

	public IWXAPI getWxApi() {
		return wxApi;
	}

	/**
	 * 设备是否有微信客户端
	 * 
	 * @return
	 */
	public boolean isWXAPPInstalled() {
		return wxApi.isWXAppInstalled();
	}

	/**
	 * 设备是否有朋友圈
	 * 
	 * @return
	 */
	public boolean isFriendsGroupInstalled() {
		// 微信4.2以上支持，如果需要检查微信版本支持API的情况，
		// 可调用IWXAPI的getWXAppSupportAPI方法,0x21020001及以上支持发送朋友圈
		return wxApi.getWXAppSupportAPI() >= 0x21020001;
	}

	/**
	 * 注册
	 * 
	 * @return
	 */
	public boolean registerApp() {
		return wxApi.registerApp(APPID);
	}

	// /**
	// * 授权
	// */
	// public boolean sendAuth() {
	// SendAuth.Req rep = new Req();
	// return wxApi.sendReq(rep);
	// }

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	/**
	 * 分享文字到微信
	 * 
	 * @param text
	 * @return
	 */
	public boolean sendTextAndPicOfWeixin(boolean isInvite) {
		if (!registerApp()) {
			return false;
		}

		return sendReq(SendMessageToWX.Req.WXSceneSession, isInvite);
	}

	/**
	 * 分享文字到朋友圈
	 * 
	 * @param text
	 * @return
	 */
	public boolean sendTextAndPicOfFriendsGroup(boolean isInvite) {
		if (!registerApp()) {
			return false;
		}

		return sendReq(SendMessageToWX.Req.WXSceneTimeline, isInvite);
	}

	public boolean sendVideo(String videoUrl) {
		if (!registerApp()) {
			return false;
		}
		return sendVideoReq(SendMessageToWX.Req.WXSceneTimeline, videoUrl);
	}

	private boolean sendVideoReq(int scene, String videoUrl) {
		WXWebpageObject localWXWebpageObject = new WXWebpageObject();
		localWXWebpageObject.webpageUrl = shareContent;
		WXMediaMessage localWXMediaMessage = new WXMediaMessage(
				localWXWebpageObject);
		StringBuffer sb = new StringBuffer();
		if (scene == SendMessageToWX.Req.WXSceneTimeline) {
			// if (isInvite) {
			// sb.append(Utils.trans(R.string.invite_content, KSongApplication
			// .getUserInfo().getUid()));
			// } else {
			sb.append(shareContent);
			// }
		} else {
			sb.append(shareTitle);
		}
		localWXMediaMessage.title = sb.toString();// 不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。

		// String shareContent = null;
		// if (isInvite) {
		// shareContent = Utils.trans(R.string.invite_content,
		// KSongApplication.getUserInfo().getUid());
		// } else {
		// shareContent = Utils.trans(R.string.share_content);
		// }
		localWXMediaMessage.description = shareContent;

		localWXMediaMessage.thumbData = getBitmapBytes(
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.app_icon), false);
		WXVideoObject videoObj = new WXVideoObject();
		videoObj.videoUrl = videoUrl;
		localWXMediaMessage.mediaObject = videoObj;

		SendMessageToWX.Req localReq = new SendMessageToWX.Req();
		localReq.transaction = System.currentTimeMillis() + "";
		localReq.message = localWXMediaMessage;
		localReq.scene = scene;
		return wxApi.sendReq(localReq);
	}

	private boolean sendReq(int scene, boolean isInvite) {
		WXWebpageObject localWXWebpageObject = new WXWebpageObject();
		localWXWebpageObject.webpageUrl = shareContent;
		WXMediaMessage localWXMediaMessage = new WXMediaMessage(
				localWXWebpageObject);
		StringBuffer sb = new StringBuffer();
		if (scene == SendMessageToWX.Req.WXSceneTimeline) {
			// if (isInvite) {
			// sb.append(Utils.trans(R.string.invite_content, KSongApplication
			// .getUserInfo().getUid()));
			// } else {
			sb.append(shareContent);
			// }
		} else {
			sb.append(shareTitle);
		}
		localWXMediaMessage.title = sb.toString();// 不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。

		// String shareContent = null;
		// if (isInvite) {
		// shareContent = Utils.trans(R.string.invite_content,
		// KSongApplication.getUserInfo().getUid());
		// } else {
		// shareContent = Utils.trans(R.string.share_content);
		// }
		localWXMediaMessage.description = shareContent;

		localWXMediaMessage.thumbData = getBitmapBytes(
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.app_icon), false);
		SendMessageToWX.Req localReq = new SendMessageToWX.Req();
		localReq.transaction = System.currentTimeMillis() + "";
		localReq.message = localWXMediaMessage;
		localReq.scene = scene;
		return wxApi.sendReq(localReq);
	}

	// 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
	private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
		if (bitmap == null) {
			return null;
		}
		Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);
		int i;
		int j;
		if (bitmap.getHeight() > bitmap.getWidth()) {
			i = bitmap.getWidth();
			j = bitmap.getWidth();
		} else {
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
		while (true) {
			localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
					80, 80), null);
			if (paramBoolean)
				bitmap.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
			}
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
	}

	// /**
	// * 分享图片到微信
	// *
	// * @param path
	// * @return
	// */
	// public boolean sendImgOfWeixin(String path) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendImg(path, SendMessageToWX.Req.WXSceneSession);
	// }
	//
	// /**
	// * 分享图片到朋友圈
	// *
	// * @param path
	// * @return
	// */
	// public boolean sendImgOfFriendsGroup(String path) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendImg(path, SendMessageToWX.Req.WXSceneTimeline);
	// }
	//
	// /**
	// * 分享音乐到微信
	// *
	// * @param musicUrl
	// * @param headUrl
	// * @param songName
	// * @param singer
	// * @return
	// */
	// public boolean sendMusicOfWeixin(String musicUrl, String playUrl,
	// String headUrl, String songName, String singer) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendMusic(musicUrl, playUrl, headUrl, songName, singer,
	// SendMessageToWX.Req.WXSceneSession);
	// }
	//
	// /**
	// * 分享音乐到朋友圈
	// *
	// * @param musicUrl
	// * @param headUrl
	// * @param songName
	// * @param singer
	// * @return
	// */
	// public boolean sendMusicOfFriendsGroup(String musicUrl, String playUrl,
	// String headUrl, String songName, String singer) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendMusic(musicUrl, playUrl, headUrl, songName, singer,
	// SendMessageToWX.Req.WXSceneTimeline);
	// }
	//
	// /**
	// * 分享音乐到微信
	// *
	// * @param musicUrl
	// * @param headBmp
	// * @param songName
	// * @param singer
	// * @return
	// */
	// public boolean sendMusicOfWeixin(String musicUrl, String playUrl,
	// Bitmap headBmp, String songName, String singer) {
	// Trace.d("****************url:" + musicUrl);
	//
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendMusic(musicUrl, playUrl, headBmp, songName, singer,
	// SendMessageToWX.Req.WXSceneSession);
	// }
	//
	// /**
	// * 分享音乐到朋友圈
	// *
	// * @param musicUrl
	// * @param headBmp
	// * @param songName
	// * @param singer
	// * @return
	// */
	// public boolean sendMusicOfFriendsGroup(String musicUrl, String playUrl,
	// Bitmap headBmp, String songName, String singer) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendMusic(musicUrl, playUrl, headBmp, songName, singer,
	// SendMessageToWX.Req.WXSceneTimeline);
	// }
	//
	// /**
	// * 分享网页到微信
	// *
	// * @param url
	// * @param webTitle
	// * @param webDesc
	// */
	// public boolean sendURLOfWeixin(String url, String webTitle, String
	// webDesc) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendURL(url, webTitle, webDesc,
	// SendMessageToWX.Req.WXSceneSession);
	// }
	//
	// /**
	// * 分享网页到朋友圈
	// *
	// * @param url
	// * @param webTitle
	// * @param webDesc
	// */
	// public boolean sendURLOfGroup(String url, String webTitle, String
	// webDesc) {
	// if (!registerApp()) {
	// return false;
	// }
	//
	// return sendURL(url, webTitle, webDesc,
	// SendMessageToWX.Req.WXSceneTimeline);
	// }

	// /**
	// * 分享文本
	// *
	// * @param text
	// */
	// private boolean sendText(String text, int scene) {
	// if (text == null || text.length() == 0) {
	// return false;
	// }
	//
	// WXTextObject textObj = new WXTextObject();
	// textObj.text = text;
	//
	// WXMediaMessage msg = new WXMediaMessage();
	// msg.mediaObject = textObj;
	// msg.description = text;
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("text");
	// req.message = msg;
	// req.scene = scene;
	//
	// return wxApi.sendReq(req);
	// }
	//
	// /**
	// * 唯一标记本次请求
	// *
	// * @param type
	// * @return
	// */
	// private String buildTransaction(final String type) {
	// return (type == null) ? System.currentTimeMillis() + "" : type
	// + System.currentTimeMillis();
	//
	// }
	// /**
	// * 分享图片
	// *
	// * @param path
	// */
	// private boolean sendImg(String path, int scene) {
	// if (path == null || path.trim().length() == 0) {
	// return false;
	// }
	//
	// WXImageObject imgObj = new WXImageObject();
	// imgObj.imagePath = path;
	//
	// WXMediaMessage msg = new WXMediaMessage();
	// msg.mediaObject = imgObj;
	// msg.thumbData = scaleBitmapToByteArray(BitmapUtils.createBitmap(path));
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("img");
	// req.message = msg;
	// req.scene = scene;
	//
	// return wxApi.sendReq(req);
	// }
	//
	// /**
	// * 分享音乐
	// *
	// * @param musicUrl
	// * @param headUrl
	// * @param songName
	// * @param singer
	// */
	// private boolean sendMusic(String musicUrl, String playUrl, String
	// headUrl,
	// String songName, String singer, int scene) {
	// return sendMusic(musicUrl, playUrl, BitmapUtils.createBitmap(headUrl),
	// songName, singer, scene);
	// }
	//
	// /**
	// * 分享音乐
	// *
	// * @param musicUrl
	// * @param headBmp
	// * @param songName
	// * @param singer
	// */
	// private boolean sendMusic(String musicUrl, String playUrl, Bitmap
	// headBmp,
	// String songName, String singer, int scene) {
	// if (musicUrl == null || musicUrl.trim().length() <= 0) {
	// return false;
	// }
	//
	// if (songName == null || songName.trim().length() <= 0) {
	// songName = "无歌曲名";
	// }
	//
	// if (singer == null || singer.trim().length() <= 0) {
	// singer = "无歌手名";
	// }
	//
	// WXMusicObject music = new WXMusicObject();
	// music.musicUrl = getMusicUrl(musicUrl, playUrl);
	// music.musicLowBandUrl = music.musicUrl;
	//
	// WXMediaMessage msg = new WXMediaMessage();
	// msg.mediaObject = music;
	// msg.title = songName;
	// msg.description = singer;
	// // msg.setThumbImage(headBmp);
	// msg.thumbData = scaleBitmapToByteArray(headBmp);
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("music");
	// req.message = msg;
	// req.scene = scene;
	//
	// return wxApi.sendReq(req);
	// }
	//
	// // 获取音乐URL
	// private String getMusicUrl(String mp3Url, String playUrl) {
	// String musicUrl = null;
	// try {
	// JSONObject json = new JSONObject();
	// json.put("song_WapLiveURL", mp3Url);
	// json.put("song_WifiURL", mp3Url);
	//
	// musicUrl = playUrl + "#wechat_music_url="
	// + BCDCoding.encode(json.toString());
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return musicUrl;
	// }
	//
	// /**
	// * 分享网页
	// *
	// * @param url
	// * @param webTitle
	// * @param webDesc
	// */
	// private boolean sendURL(String url, String webTitle, String webDesc,
	// int scene) {
	// if (url == null || url.trim().length() == 0) {
	// return false;
	// }
	//
	// WXWebpageObject webpage = new WXWebpageObject();
	// webpage.webpageUrl = url;
	//
	// WXMediaMessage msg = new WXMediaMessage(webpage);
	// msg.title = webTitle;
	// msg.description = webDesc;
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("webpage");
	// req.message = msg;
	// req.scene = scene;
	//
	// return wxApi.sendReq(req);
	// }
	//
	//
	// /**
	// * 将bitmap转换成100*100
	// *
	// * @param bmp
	// * @return
	// */
	// private byte[] scaleBitmapToByteArray(Bitmap bmp) {
	// if (bmp == null) {
	// return null;
	// }
	//
	// Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
	// bmp.recycle();
	//
	// return BitmapUtils.bitmapToByteArrayOfPNG(thumbBmp);
	// }

	// /**
	// * 创建Bitmap
	// *
	// * @param path
	// * @return
	// */
	// public Bitmap createBmpFromPath(String path) {
	// Drawable drawable = Drawable.createFromPath(path);
	// if (drawable == null) {
	// return null;
	// }
	//
	// BitmapDrawable bmpDrawable = (BitmapDrawable) drawable;
	// return bmpDrawable.getBitmap();
	// }

	// /**
	// * bitmap转换byteArray
	// *
	// * @param bmp
	// * @return
	// */
	// private byte[] bmpToByteArray(final Bitmap bmp) {
	// ByteArrayOutputStream output = new ByteArrayOutputStream();
	// bmp.compress(CompressFormat.PNG, 100, output);
	// bmp.recycle();
	//
	// byte[] result = output.toByteArray();
	// try {
	// output.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
}
