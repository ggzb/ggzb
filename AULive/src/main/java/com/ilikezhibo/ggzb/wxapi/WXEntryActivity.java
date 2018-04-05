package com.ilikezhibo.ggzb.wxapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.AppConstants;
import com.jack.utils.FileUtil;
import com.jack.utils.MobileConfig;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.login.WeiXinRegEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import de.greenrobot.event.EventBus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
   public static final String INTENT_INVITE_CODE_KEY = "INTENT_INVITE_CODE_KEY";
   public static final String INTENT_IS_WXCIRCLE_KEY = "INTENT_IS_WXCIRCLE_KEY";
   public static final String INTENT_SHARE_TITLE_KEY = "INTENT_SHARE_TITLE_KEY";
   public static final String INTENT_SHARE_CONTENT_KEY = "INTENT_SHARE_CONTENT_KEY";
   public static final String INTENT_SHARE_URL_KEY = "INTENT_SHARE_URL_KEY";
   public static final String INTENT_SHARE_IMG_KEY = "INTENT_SHARE_IMG_KEY";
   public static final String INTENT_SHARE_VIDEO_URL_KEY = "INTENT_SHARE_VIDEO_URL_KEY";
   public static final String INTENT_SHARE_FRAME_POS_KEY = "INTENT_SHARE_FRAME_POS_KEY";

   // IWXAPI 是第三方app和微信通信的openapi接口
   private IWXAPI api;

   private int framePos;

   private String inviteCodeStr;// 邀请码
   private static boolean iswxCircle;// 分享的渠道
   private String shareContent;
   private String shareTitle;
   private String imgUrl;
   private String shareUrl;
   private String shareVideoUrl;

   @Override public void onCreate(Bundle savedInstanceState) {
      // 通过WXAPIFactory工厂，获取IWXAPI的实例
      api = WXAPIFactory.createWXAPI(this, AppConstants.WEI_XIN_ID, false);
      api.handleIntent(getIntent(), this);
      api.registerApp(AppConstants.WEI_XIN_ID);

      super.onCreate(savedInstanceState);

      inviteCodeStr = getIntent().getStringExtra(INTENT_INVITE_CODE_KEY);
      iswxCircle = getIntent().getBooleanExtra(INTENT_IS_WXCIRCLE_KEY, false);
      shareContent = getIntent().getStringExtra(INTENT_SHARE_CONTENT_KEY);
      shareTitle = getIntent().getStringExtra(INTENT_SHARE_TITLE_KEY);
      imgUrl = getIntent().getStringExtra(INTENT_SHARE_IMG_KEY);
      shareUrl = getIntent().getStringExtra(INTENT_SHARE_URL_KEY);
      shareVideoUrl = getIntent().getStringExtra(INTENT_SHARE_VIDEO_URL_KEY);
      framePos = getIntent().getIntExtra(INTENT_SHARE_FRAME_POS_KEY, 1);
      if (inviteCodeStr != null && inviteCodeStr.trim().length() > 0) {
         if (iswxCircle && !WeixinAPI.getInstance(this).isFriendsGroupInstalled()) {
            Utils.showMessage(Utils.trans(R.string.share_weixin_friend_prompt));
            finish();
            return;
         }
         doinvite();
      } else if (shareContent != null) {
         if (iswxCircle && !WeixinAPI.getInstance(this).isFriendsGroupInstalled()) {
            Utils.showMessage(Utils.trans(R.string.share_weixin_friend_prompt));
            finish();
            return;
         }
         // loadImage(iswxCircle ? SendMessageToWX.Req.WXSceneTimeline
         // : SendMessageToWX.Req.WXSceneSession);
         share();
      }
   }

   private void share() {
      Trace.d("wx share videourl:" + shareVideoUrl);
      new Thread() {
         public void run() {
            if (shareVideoUrl != null) {
               sendVideoReq(iswxCircle ? SendMessageToWX.Req.WXSceneTimeline
                   : SendMessageToWX.Req.WXSceneSession);
            } else {
               sendWxReq(iswxCircle ? SendMessageToWX.Req.WXSceneTimeline
                   : SendMessageToWX.Req.WXSceneSession);
            }
         }
      }.start();
   }

   @Override protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);

      setIntent(intent);
      WeixinAPI.getInstance(this).getWxApi().handleIntent(intent, this);
   }

   private void doinvite() {
      if (!api.isWXAppInstalled()) {
         Utils.showMessage(Utils.trans(R.string.no_install_weixin));
         finish();
         return;
      }

      // send appdata with no attachment
      final WXAppExtendObject appdata = new WXAppExtendObject();
      // 传出来与读回来的数据
      appdata.extInfo = inviteCodeStr;
      final WXMediaMessage msg = new WXMediaMessage();
      msg.title = "智能人脸匹配技术，真实的个人信息，免费约会相亲交友神器，你也安装一个吧";
      msg.description = "邀请码:" + inviteCodeStr + "(下载安装成功后注册完成再点击即邀请成功)";
      msg.mediaObject = appdata;
      try {
         msg.setThumbImage(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon));
      } catch (OutOfMemoryError e) {
         e.printStackTrace();
      }
      SendMessageToWX.Req req = new SendMessageToWX.Req();
      req.transaction = buildTransaction("appdata");
      req.message = msg;
      // 是否是分享到朋友圈
      req.scene =
          iswxCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
      api.sendReq(req);
      WXEntryActivity.this.finish();
   }

   private void sendWxReq(final int scene) {
      WXWebpageObject localWXWebpageObject = new WXWebpageObject();
      localWXWebpageObject.webpageUrl = shareUrl;
      WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
      if (imgUrl == null) {
         try {
            localWXMediaMessage.setThumbImage(
                BitmapFactory.decodeResource(getResources(), R.drawable.app_icon));
         } catch (OutOfMemoryError e) {
            e.printStackTrace();
         }
      } else {
         Bitmap bmp = null;
         try {
            bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
         } catch (Exception e) {
            e.printStackTrace();
         }

         if (bmp != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);
            bmp.recycle();
            localWXMediaMessage.thumbData = bmpToByteArray(thumbBmp, true);
         }
      }
      localWXMediaMessage.title =
          iswxCircle ? shareContent : shareTitle;// 不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。
      localWXMediaMessage.description = iswxCircle ? shareTitle : shareContent;

      // localWXMediaMessage.thumbData =
      // BitmapFactory.decodeResource(getResources(),
      // R.drawable.ic_launcher);
      SendMessageToWX.Req localReq = new SendMessageToWX.Req();
      localReq.transaction = buildTransaction("img");
      localReq.message = localWXMediaMessage;
      localReq.scene = scene;
      api.sendReq(localReq);
      finish();
   }

   private void sendVideoReq(int scene) {
      WXWebpageObject localWXWebpageObject = new WXWebpageObject();
      localWXWebpageObject.webpageUrl = shareUrl;
      WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
      localWXMediaMessage.title = shareTitle;// 不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。

      Trace.d("share video title:"
          + shareTitle
          + " webPageurl:"
          + shareUrl
          + " sharecontent："
          + shareContent
          + " shareVideoUrl:"
          + shareVideoUrl);
      localWXMediaMessage.description = shareContent;

      Bitmap bmp = BitmapFactory.decodeFile(
          FileUtil.VIDEO_THUMB_CACHE_PATH + File.separator + framePos + ".png");
      if (bmp != null) {
         Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 80 * 2, 80 * 2, true);
         bmp.recycle();
         localWXMediaMessage.thumbData = bmpToByteArray(thumbBmp, true);
      }

      SendMessageToWX.Req localReq = new SendMessageToWX.Req();
      localReq.transaction = System.currentTimeMillis() + "";
      localReq.message = localWXMediaMessage;
      localReq.scene = scene;
      api.sendReq(localReq);
      finish();
   }

   private String buildTransaction(final String type) {
      return (type == null) ? String.valueOf(System.currentTimeMillis())
          : type + System.currentTimeMillis();
   }

   // 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
   private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
      if (bitmap == null) {
         return null;
      }
      Bitmap localBitmap = Bitmap.createBitmap(80 * 3, 80 * 3, Bitmap.Config.RGB_565);
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
         localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, 80, 80), null);
         if (paramBoolean) {
            bitmap.recycle();
         }
         ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
         localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
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

   public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

      int i;
      int j;
      if (bmp.getHeight() > bmp.getWidth()) {
         i = bmp.getWidth();
         j = bmp.getWidth();
      } else {
         i = bmp.getHeight();
         j = bmp.getHeight();
      }

      Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);

      while (true) {
         localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
         if (needRecycle) {
            bmp.recycle();
         }
         ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
         localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
         localBitmap.recycle();
         byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
         try {
            localByteArrayOutputStream.close();
            return arrayOfByte;
         } catch (Exception e) {
            // F.out(e);
         }
         i = bmp.getHeight();
         j = bmp.getHeight();
      }
   }

   // 微信发送请求到第三方应用时，会回调到该方法
   @Override public void onReq(BaseReq req) {
      switch (req.getType()) {
         case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
            // goToGetMsg();
            Trace.d("ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX");
            break;
         case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
            // goToShowMsg((ShowMessageFromWX.Req) req);
            ShowMessageFromWX.Req req1 = (ShowMessageFromWX.Req) req;
            // 获取从微信取得的邀请码
            String extinfo = ((WXAppExtendObject) req1.message.mediaObject).extInfo;
            Trace.d("extinfo:" + extinfo);
            if (Utils.isAppInstalled(this,
                MobileConfig.getMobileConfig(this).getPackageName())) {// 已经安装
               Intent intent = new Intent(this, MainActivity.class);
               intent.putExtra(MainActivity.INTENT_INVITE_CODE_KEY, extinfo);
               startActivity(intent);
            } else {// 未安装
               Uri uri = Uri.parse("http://www.yuanphone.com/down/invite");
               Intent it = new Intent(Intent.ACTION_VIEW, uri);
               startActivity(it);
            }
            this.finish();
            break;
         default:
            break;
      }
   }

   // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
   @Override public void onResp(BaseResp resp) {

      String result = "";
      Trace.d("WXonResp result:" + result + " wxcircle:" + iswxCircle);

      switch (resp.errCode) {
         case BaseResp.ErrCode.ERR_OK:

            // 如果是受权登陆
            if (resp instanceof SendAuth.Resp) {
               String token = ((SendAuth.Resp) resp).code;
               WeiXinRegEvent weiXinRegEvent = new WeiXinRegEvent();
               weiXinRegEvent.token = token;
               EventBus.getDefault().post(weiXinRegEvent);
               WXEntryActivity.this.finish();
               return;
            }
            result = "分享成功";
            // 分享成功后做金币任务
            if (iswxCircle) {
               uploadShareSucc(AppConstants.SHARE_WXCIRCLE_OK, ShareHelper.share_liveuid);
            } else {
               uploadShareSucc(AppConstants.SHARE_WXSESSION_OK, ShareHelper.share_liveuid);
            }

            if (shareVideoUrl != null) {
               FileUtil.deleteFileOfDir(FileUtil.VIDEO_THUMB_CACHE_PATH, true);
            }

            break;
         case BaseResp.ErrCode.ERR_USER_CANCEL:
            result = "取消分享";
            if (shareVideoUrl != null) {
               FileUtil.deleteFileOfDir(FileUtil.VIDEO_THUMB_CACHE_PATH, true);
            }
            break;
         case BaseResp.ErrCode.ERR_AUTH_DENIED:
            Trace.d("ERR_AUTH_DENIED");
            result = "分享失败";
            break;
         default:
            result = "分享失败";
            break;
      }

      Utils.showMessage(result);
      this.finish();
   }

   private void uploadShareSucc(final String type, String liveuid) {
      RequestInformation request = new RequestInformation(
          UrlHelper.SHARE_CHANNEL + "?share_from=" + type + "&liveuid=" + liveuid,
          RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            if (callback != null && callback.getStat() == 200) {
               Trace.d("upload share succ" + type);
               //用于改变
               Intent intent = new Intent(WXShareOkReceiver.ACTION_SHARE_WX_KEY);
               intent.putExtra(WXShareOkReceiver.INTENT_SHARE_TYPE_KEY, type);
               WXEntryActivity.this.sendBroadcast(intent);
            }
         }

         @Override public void onFailure(AppException e) {

         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }
}
