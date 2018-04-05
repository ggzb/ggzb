package com.ilikezhibo.ggzb.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ilikezhibo.ggzb.avsdk.activity.ReleaseLiveActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.AppConstants;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.login.weibo.AccessTokenKeeper;
import com.ilikezhibo.ggzb.login.weibo.Constants;
import com.ilikezhibo.ggzb.wxapi.WXShareOkReceiver.ShareSuccListener;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class ShareHelper extends Dialog
    implements android.view.View.OnClickListener, ShareSuccListener, IWeiboHandler.Response {
   private static Activity mContext;

   private LinearLayout qqShareLayout;
   private LinearLayout qqzoneShareLayout;
   private LinearLayout wechatShareLayout;
   private LinearLayout wxcircleShareLayout;
   private LinearLayout sinaShareLayout;

   private ImageView wxseesionImg;
   private ImageView wxcircleImg;
   private ImageView qqShareImg;
   private ImageView qqzoneShareImg;

   private TextView wxCircleIncreaseTv;
   private TextView wxSessionIncreaseTv;
   private TextView qqIncreaseTv;
   private TextView qqzoneIncreaseTv;

   private String shareTitle;
   private String shareUrl;
   private String shareContent;
   private String imgUrl;
   private ShareEntity shareEntity;
   private ShareEntity completeEntity;

   private boolean isInvite = false;
   private String inviteCodeStr;

   private int mExtarFlag = 0x00;
   private Tencent tencent = null;

   private WXShareOkReceiver shareReceiver;

   //微博相关
   public static final int SHARE_CLIENT = 1;
   public static final int SHARE_ALL_IN_ONE = 2;
   /** 微博微博分享接口实例 */
   public IWeiboShareAPI mWeiboShareAPI = null;
   private int mShareType = SHARE_ALL_IN_ONE;
   private boolean isRegester;

   public ShareHelper(Context context) {
      super(context, R.style.MyDialogStyleBottom);
      this.mContext = (Activity) context;
      EventBus.getDefault().register(this);
      //QQ
      tencent = Tencent.createInstance(AppConstants.QQ_APP_ID, mContext.getApplicationContext());

      //微信与朋友圈
      WeixinAPI.getInstance(mContext).registerApp();
      shareReceiver = new WXShareOkReceiver();
      shareReceiver.setListener(this);
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction(WXShareOkReceiver.ACTION_SHARE_WX_KEY);

      mContext.registerReceiver(shareReceiver, intentFilter);
      isRegester = true;
      //微博相关

      // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
      mAuthInfo =
          new AuthInfo(mContext, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
      mSsoHandler = new SsoHandler(mContext, mAuthInfo);

      mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Constants.APP_KEY);
      // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
      // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
      // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
      mWeiboShareAPI.registerApp();

      //必须得在Activity才测调用,此处作废，自作警示作用
      mWeiboShareAPI.handleWeiboResponse(mContext.getIntent(), this);
   }

   @Override protected void onStop() {
      super.onStop();
   }

   @Override public void dismiss() {
      super.dismiss();
      EventBus.getDefault().unregister(this);
      mContext = null;
      //mContext.unregisterReceiver(shareReceiver);
   }

   //回调只在SinaShareReqActivity里才能收到，传到这里处理
   public void onEvent(SinaReqEvent sinaReqEvent) {
      Trace.d("ShareHelper onEvent(SinaReqEvent sinaReqEvent)");
      if (mWeiboShareAPI != null && sinaReqEvent.intent != null) {
         mWeiboShareAPI.handleWeiboResponse(sinaReqEvent.intent, (IWeiboHandler.Response) mContext);
      }
   }

   @Override protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.share_layout);

      //监听
      qqShareLayout = (LinearLayout) findViewById(R.id.shareQQLayout);
      qqzoneShareLayout = (LinearLayout) findViewById(R.id.shareQQZoneLayout);
      wechatShareLayout = (LinearLayout) findViewById(R.id.shareWeiXinLayout);
      wxcircleShareLayout = (LinearLayout) findViewById(R.id.shareWeiXinFriendLayout);
      // smsShareLayout = (LinearLayout) findViewById(R.id.shareSmsLayout);
      sinaShareLayout = (LinearLayout) findViewById(R.id.shareSinaLayout);
      qqShareLayout.setOnClickListener(this);
      qqzoneShareLayout.setOnClickListener(this);
      wechatShareLayout.setOnClickListener(this);
      wxcircleShareLayout.setOnClickListener(this);
      sinaShareLayout.setOnClickListener(this);
      // smsShareLayout.setOnClickListener(this);
      findViewById(R.id.rootLayout).setOnClickListener(this);

      //邀请，在图标与分享类型下面的提示的信息
      wxCircleIncreaseTv = (TextView) findViewById(R.id.wxcircleIncreaTv);
      wxSessionIncreaseTv = (TextView) findViewById(R.id.wxsessionIncreaTv);
      qqIncreaseTv = (TextView) findViewById(R.id.qqIncreaTv);
      qqzoneIncreaseTv = (TextView) findViewById(R.id.qqzoneIncreaTv);

      TextView shareTitlePrompt = (TextView) findViewById(R.id.sharePromptTv);
      if (shareEntity != null) {
         wxCircleIncreaseTv.setVisibility(View.VISIBLE);
         wxSessionIncreaseTv.setVisibility(View.VISIBLE);
         qqIncreaseTv.setVisibility(View.VISIBLE);
         qqzoneIncreaseTv.setVisibility(View.VISIBLE);

         qqIncreaseTv.setText(Utils.trans(R.string.share_credit_increase, shareEntity.getQq()));
         qqzoneIncreaseTv.setText(
             Utils.trans(R.string.share_credit_increase, shareEntity.getQqzone()));
         wxCircleIncreaseTv.setText(
             Utils.trans(R.string.share_credit_increase, shareEntity.getWxcircle()));
         wxSessionIncreaseTv.setText(
             Utils.trans(R.string.share_credit_increase, shareEntity.getWxsession()));

         shareTitlePrompt.setText(Utils.trans(R.string.share_title_prompt,
             shareEntity.getQq() + shareEntity.getQqzone() + shareEntity.getWxcircle() + shareEntity
                 .getWxsession()));
      } else {
         shareTitlePrompt.setVisibility(View.GONE);
      }

      qqShareImg = (ImageView) findViewById(R.id.qqShareImg);
      qqzoneShareImg = (ImageView) findViewById(R.id.qqZoneShareImg);
      wxcircleImg = (ImageView) findViewById(R.id.wxcircleShareImg);
      wxseesionImg = (ImageView) findViewById(R.id.wxsessionShareImg);

      if (completeEntity != null) {
         if (completeEntity.getQq() == 1) {
            qqShareImg.setImageResource(R.drawable.share_qq_b);
         } else {
            qqShareImg.setImageResource(R.drawable.share_qq_selector);
         }

         if (completeEntity.getQqzone() == 1) {
            qqzoneShareImg.setImageResource(R.drawable.share_qzone_b);
         } else {
            qqzoneShareImg.setImageResource(R.drawable.share_qqzone_selector);
         }

         if (completeEntity.getWxcircle() == 1) {
            wxcircleImg.setImageResource(R.drawable.share_wxcircle_b);
         } else {
            wxcircleImg.setImageResource(R.drawable.share_wxcircle_selector);
         }

         if (completeEntity.getWxsession() == 1) {
            wxseesionImg.setImageResource(R.drawable.share_wechat_b);
         } else {
            wxseesionImg.setImageResource(R.drawable.share_wechat_selector);
         }
      }
   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }

      switch (v.getId()) {
         case R.id.rootLayout:
            dismiss();
            break;
         case R.id.shareQQLayout:
            doShareToQQ();
            break;
         case R.id.shareQQZoneLayout:
            doShareToQQZone();
            break;
         case R.id.shareWeiXinLayout:
            doShareToWeiXin();
            break;
         case R.id.shareWeiXinFriendLayout:
            doShareToWeiXinFriend();
            break;
         case R.id.shareSinaLayout:// sina分享
            doShareToWeiBo();
            break;
         // case R.id.shareSmsLayout:
         //
         // break;
         default:
            break;
      }
   }

   public void doShareToWeiXin() {
      // if (isInvite) {
      Intent intent = new Intent(mContext, WXEntryActivity.class);
      Trace.d("isInvite:" + isInvite);
      if (isInvite) {
         intent.putExtra(WXEntryActivity.INTENT_INVITE_CODE_KEY, inviteCodeStr);
      } else {
         intent.putExtra(WXEntryActivity.INTENT_SHARE_URL_KEY, shareUrl);
         intent.putExtra(WXEntryActivity.INTENT_SHARE_IMG_KEY, imgUrl);
         intent.putExtra(WXEntryActivity.INTENT_SHARE_CONTENT_KEY, shareContent);
         intent.putExtra(WXEntryActivity.INTENT_SHARE_TITLE_KEY, shareTitle);

         Trace.d(
             "shareUrl:" + shareUrl + "sharecontent：" + shareContent + " shareImgUrl:" + imgUrl);
      }
      intent.putExtra(WXEntryActivity.INTENT_IS_WXCIRCLE_KEY, false);
      mContext.startActivity(intent);
      // } else {
      // if (WeixinAPI.getInstance(mContext).isWXAPPInstalled()) {
      // WeixinAPI.getInstance(mContext).sendTextAndPicOfWeixin(
      // false);
      // } else {
      // Utils.showMessage(Utils.trans(R.string.no_install_weixin));
      // }
      // }
   }

   public void doShareToWeiXinFriend() {
      // if (isInvite) {
      Intent intent = new Intent(mContext, WXEntryActivity.class);
      if (isInvite) {
         intent.putExtra(WXEntryActivity.INTENT_INVITE_CODE_KEY, inviteCodeStr);
      } else {
         intent.putExtra(WXEntryActivity.INTENT_SHARE_URL_KEY, shareUrl);
         intent.putExtra(WXEntryActivity.INTENT_SHARE_IMG_KEY, imgUrl);
         intent.putExtra(WXEntryActivity.INTENT_SHARE_CONTENT_KEY, shareContent);
         intent.putExtra(WXEntryActivity.INTENT_SHARE_TITLE_KEY, shareTitle);
      }
      intent.putExtra(WXEntryActivity.INTENT_IS_WXCIRCLE_KEY, true);
      mContext.startActivity(intent);
      // } else {
      // if (WeixinAPI.getInstance(mContext).isWXAPPInstalled()) {
      // if (WeixinAPI.getInstance(mContext)
      // .isFriendsGroupInstalled()) {
      // WeixinAPI.getInstance(mContext)
      // .sendTextAndPicOfFriendsGroup(false);
      // } else {
      // Utils.showMessage(Utils
      // .trans(R.string.share_weixin_friend_prompt));
      // }
      // } else {
      // Utils.showMessage(Utils.trans(R.string.no_install_weixin));
      // }
      // }
   }

   //必须得Activity才测调用,此处作废，自作警示作用
   @Override public void onResponse(BaseResponse baseResp) {
      Trace.d("Shareheler onResponse");
      switch (baseResp.errCode) {
         case WBConstants.ErrorCode.ERR_OK:
            Utils.showMessage(Utils.trans(R.string.weibosdk_share_success));
            break;
         case WBConstants.ErrorCode.ERR_CANCEL:
            Utils.showMessage(Utils.trans(R.string.weibosdk_share_canceled));
            break;
         case WBConstants.ErrorCode.ERR_FAIL:
            Utils.showMessage(Utils.trans(R.string.weibosdk_share_failed));
            break;
      }
   }

   //微信分享成功后更改图片状态
   @Override public void onWxShareSucc(String type) {

      Trace.d("wx share type:" + type);
      if (type.equals(AppConstants.SHARE_WXSESSION_OK)) {
         if (wxseesionImg != null) {
            wxseesionImg.setImageResource(R.drawable.share_wechat_b);
         }

         uploadShareSucc(AppConstants.SHARE_WXSESSION_OK, share_liveuid);
      } else if (type.equals(AppConstants.SHARE_WXCIRCLE_OK)) {
         if (wxcircleImg != null) {
            wxcircleImg.setImageResource(R.drawable.share_wxcircle_b);
         }
         uploadShareSucc(AppConstants.SHARE_WXCIRCLE_OK, share_liveuid);
      }
   }

   public static String share_liveuid = "";

   public void setShareLiveuid(String liveuid) {
      share_liveuid = liveuid;
   }

   // 上传分享成功的渠道
   public static void uploadShareSucc(final String type, String liveuid) {
      RequestInformation request = new RequestInformation(
          UrlHelper.SHARE_CHANNEL + "?share_from=" + type + "&liveuid=" + liveuid,
          RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback != null && callback.getStat() == 200) {
               Trace.d("upload share succ");
               if (mContext != null) {

                  if (mContext instanceof AvActivity) {
                     AvActivity avActivity = (AvActivity) mContext;
                     avActivity.sendSystemMsg(AvActivity.SHARE_ROOM, "" + callback.getMsg());
                  } else {
                     Utils.showCroutonText(mContext, callback.getMsg());
                  }
               }
            }
         }

         @Override public void onFailure(AppException e) {

         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   public void setShareTitle(String shareTitle) {
      this.shareTitle = shareTitle;
   }

   public void setShareUrl(String shareUrl) {
      this.shareUrl = shareUrl;
   }

   public void setShareContent(String shareContent, String imgUrl) {
      this.shareContent = shareContent;
      this.imgUrl = imgUrl;
   }

   public void setShareEntity(ShareEntity shareEntity) {
      this.shareEntity = shareEntity;
      if (shareEntity != null) {
         Trace.d("shareentity: qq" + shareEntity.getQq());
      }
   }

   public void setCompleteEntity(ShareEntity completeEntity) {
      this.completeEntity = completeEntity;
   }

   public void setInvite(boolean isInvite) {
      this.isInvite = isInvite;
   }

   public void setInviteCodeStr(String inviteCodeStr) {
      this.inviteCodeStr = inviteCodeStr;
   }

   public void doShareToQQ() {
      if (isInvite) {
         shareUrl = AppConstants.SHARE_INIVITE_HEAD + "?id=" + inviteCodeStr;
         shareContent = "智能人脸匹配技术，真实的个人信息，免费约会相亲交友神器，你也安装一个吧，输入邀请码"
             + inviteCodeStr
             + "就可以一起玩了。 下载地址 http://www.yuanphone.com/down/invite?id="
             + inviteCodeStr
             + " (下载安装成功后注册完成再点击即邀请成功)";
      }

      final Bundle params = new Bundle();
      params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
      params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
      // if (isInvite) {
      // shareContent = Utils.trans(R.string.invite_content,
      // KSongApplication.getUserInfo().getUid());
      // } else {
      // shareContent = Utils.trans(R.string.share_content);
      // }
      params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent);

      if (isInvite) {
         params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
             "http://img.yuanphone.com/wife/20/1813298510924564.jpg");
      } else {
         params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
      }
      // params.putString(
      // Tencent.SHARE_TO_QQ_IMAGE_URL,
      // "http://thirdapp2.qlogo.cn/qzopenapp/4296d9220a80af6828973003a8550dde9847accdf57ca14d68573c9e0d9a39d2/100");

      params.putString(QQShare.SHARE_TO_QQ_APP_NAME, Utils.getResorcString(R.string.app_name));
      params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
      params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,
          mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE));

      new Thread(new Runnable() {

         @Override public void run() {
            try {
               Looper.prepare();
               tencent.shareToQQ(mContext, params, qq_IUiListener);
            } catch (Exception e) {
            }
         }
      }).start();
   }

   public IUiListener qq_IUiListener = new IUiListener() {

      @Override public void onError(UiError arg0) {

      }

      @Override public void onComplete(Object arg0) {

         Trace.d("qq callback:" + arg0);
         uploadShareSucc(AppConstants.SHARE_QQ_OK, share_liveuid);
      }

      @Override public void onCancel() {

      }
   };

   public void doShareToQQZone() {
      Trace.d("doShareToQQZone");
      if (isInvite) {
         shareUrl = AppConstants.SHARE_INIVITE_HEAD + "?id=" + inviteCodeStr;
         shareContent = "智能人脸匹配技术，真实的个人信息，免费约会相亲交友神器，你也安装一个吧，输入邀请码"
             + inviteCodeStr
             + "就可以一起玩了。 下载地址 http://www.yuanphone.com/down/invite?id="
             + inviteCodeStr
             + " (下载安装成功后注册完成再点击即邀请成功)";
      }

      final Bundle params = new Bundle();
      params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);

      // String target_url = null;
      // try {
      // target_url = Constants.SHARE_CONTENT + "/?url="
      // + URLEncoder.encode(shareUrl, "utf-8");
      // } catch (UnsupportedEncodingException e1) {
      // e1.printStackTrace();
      // }
      // String shareContent = null;
      // if (isInvite) {
      // shareContent = Utils.trans(R.string.invite_content,
      // KSongApplication.getUserInfo().getUid());
      // } else {
      // shareContent = Utils.trans(R.string.share_content);
      // }
      // //设置分享类型：图文并茂加链接
      // int shareType = Tencent.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
      // params.putInt(Tencent.SHARE_TO_QZONE_KEY_TYPE, shareType);
      params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent);
      // params.putString(Tencent.SHARE_TO_QQ_TARGET_URL,
      // "http://www.myapp.com/downcenter/a/10308247?g_f=990935&isShowPage=true");
      params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);

      ArrayList<String> imageUrls = new ArrayList<String>();
      if (isInvite) {
         imageUrls.add("http://img.yuanphone.com/wife/20/1813298510924564.jpg");
      } else {
         imageUrls.add(imgUrl);
      }
      params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

      new Thread(new Runnable() {

         @Override public void run() {
            try {
               Looper.prepare();
               tencent.shareToQzone(mContext, params, qzone_IUiListener);
            } catch (Exception e) {
            }
         }
      }).start();
   }

   public IUiListener qzone_IUiListener = new IUiListener() {

      @Override public void onError(UiError arg0) {

      }

      @Override public void onComplete(Object arg0) {

         Trace.d("qqzone callback:" + arg0);
         Utils.showMessage("分享成功");
         uploadShareSucc(AppConstants.SHARE_QQZONE_OK, share_liveuid);
      }

      @Override public void onCancel() {

      }
   };

   private void doShareVideoToQQZone() {
      final Bundle params = new Bundle();
      params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);

      // String target_url = null;
      // try {
      // target_url = Constants.SHARE_CONTENT + "/?url="
      // + URLEncoder.encode(shareUrl, "utf-8");
      // } catch (UnsupportedEncodingException e1) {
      // e1.printStackTrace();
      // }
      // String shareContent = null;
      // if (isInvite) {
      // shareContent = Utils.trans(R.string.invite_content,
      // KSongApplication.getUserInfo().getUid());
      // } else {
      // shareContent = Utils.trans(R.string.share_content);
      // }
      // //设置分享类型：图文并茂加链接
      // int shareType = Tencent.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
      // params.putInt(Tencent.SHARE_TO_QZONE_KEY_TYPE, shareType);
      params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent);
      // params.putString(Tencent.SHARE_TO_QQ_TARGET_URL,
      // "http://www.myapp.com/downcenter/a/10308247?g_f=990935&isShowPage=true");
      params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);

      ArrayList<String> imageUrls = new ArrayList<String>();
      if (isInvite) {
         imageUrls.add("http://img.yuanphone.com/wife/20/1813298510924564.jpg");
      } else {
         imageUrls.add(imgUrl);
      }
      params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
      new Thread(new Runnable() {
         @Override public void run() {

            tencent.shareToQzone(mContext, params, new IUiListener() {

               @Override public void onError(UiError arg0) {

               }

               @Override public void onComplete(Object arg0) {

                  Trace.d("qqzone callback:" + arg0);
                  Utils.showMessage("分享成功");
                  uploadShareSucc(AppConstants.SHARE_QQZONE_OK, share_liveuid);
               }

               @Override public void onCancel() {

               }
            });
         }
      }).start();
   }

   public WXShareOkReceiver getShareReceiver() {
      return shareReceiver;
   }

   public void doShareToWeiBo() {

      Oauth2AccessToken accessToken =
          AccessTokenKeeper.readAccessToken(mContext.getApplicationContext());
      if (accessToken.isSessionValid()) {
         //如果token有效，直接分享
         doShareToWeiBoWithToken();
      } else {
         //先获取授权，在用token分享
         doWeiboLogin();
      }
   }

   /**
    * 第三方应用发送请求消息到微博，唤起微博分享界面。 注意：当
    * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
    * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
    */
   public void doShareToWeiBoWithToken() {
      new Thread(new Runnable() {
         @Override public void run() {
            // 1. 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            weiboMessage.textObject = getTextObj();
            weiboMessage.imageObject = getImageObj();

            // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
            //if (hasWebpage) {
            //weiboMessage.mediaObject = getWebpageObj();
            //}
            //if (hasMusic) {
            //weiboMessage.mediaObject = getMusicObj();
            //}
            //if (hasVideo) {
            //weiboMessage.mediaObject = getVideoObj();
            //}
            //if (hasVoice) {
            //weiboMessage.mediaObject = getVoiceObj();
            //}

            // 2. 初始化从第三方到微博的消息请求
            SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
            // 用transaction唯一标识一个请求
            request.transaction = String.valueOf(System.currentTimeMillis());
            request.multiMessage = weiboMessage;

            // 3. 发送请求消息到微博，唤起微博分享界面
            // if (mShareType == SHARE_CLIENT) {
            // mWeiboShareAPI.sendRequest(mContext, request);
            // } else if (mShareType == SHARE_ALL_IN_ONE) {
            AuthInfo authInfo =
                new AuthInfo(mContext, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
            Oauth2AccessToken accessToken =
                AccessTokenKeeper.readAccessToken(mContext.getApplicationContext());
            String token = "";
            if (accessToken != null) {
               token = accessToken.getToken();
            }
            if (mWeiboShareAPI != null) {
               mWeiboShareAPI.sendRequest(mContext, request, authInfo, token, weibo_AuthListener);
            }
         }
      }).start();
   }

   public WeiboAuthListener weibo_AuthListener = new WeiboAuthListener() {

      @Override public void onWeiboException(WeiboException arg0) {
         arg0.printStackTrace();
      }

      @Override public void onComplete(Bundle bundle) {
         Trace.d("mWeiboShareAPI.sendRequest onComplete");
         //更新token
         Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
         AccessTokenKeeper.writeAccessToken(mContext.getApplicationContext(), newToken);
         // Toast.makeText(
         // getApplicationContext(),
         // "onAuthorizeComplete token = "
         // + newToken.getToken(), 0).show();
         //上传分享成功
         uploadShareSucc("weibo", share_liveuid);
      }

      @Override public void onCancel() {
      }
   };

   public void doWeiboShareCallback() {
      Trace.d("doWeiboShareCallback()");
      uploadShareSucc("weibo", share_liveuid);
   }

   /**
    * 创建文本消息对象。
    *
    * @return 文本消息对象。
    */
   private TextObject getTextObj() {
      TextObject textObject = new TextObject();
      textObject.text = shareContent;
      return textObject;
   }

   /**
    * 创建图片消息对象。
    *
    * @return 图片消息对象。
    */
   private ImageObject getImageObj() {
      final ImageObject imageObject = new ImageObject();
      // BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView
      // .getDrawable();
      if (isInvite) {
         imageObject.setImageObject(
             BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));
      } else {
         //android.os.NetworkOnMainThreadException

         imageObject.setImageObject(ImageLoader.getInstance()
             .loadImageSync(imgUrl, AULiveApplication.getGlobalImgOptions()));
      }
      return imageObject;
   }

   // /微博第三方登录相关

   private AuthInfo mAuthInfo;

   /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
   private Oauth2AccessToken mAccessToken;

   /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
   public SsoHandler mSsoHandler;

   private void doWeiboLogin() {
      //用于过滤登录后返回的onresume,不然分享后的onresum无法调用
      ReleaseLiveActivity.is_doing_sina_login = true;

      mSsoHandler.authorize(new AuthListener());
   }

   /**
    * 微博认证授权回调类。 1. SSO 授权时，需要在 {link #onActivityResult} 中调用
    * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
    * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
    * SharedPreferences 中。
    */
   class AuthListener implements WeiboAuthListener {

      @Override public void onComplete(Bundle values) {
         // 从 Bundle 中解析 Token
         mAccessToken = Oauth2AccessToken.parseAccessToken(values);
         // 从这里获取用户输入的 电话号码信息
         String phoneNum = mAccessToken.getPhoneNum();
         if (mAccessToken.isSessionValid()) {

            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
            Utils.showMessage("授权成功");
            //授权成功继续分享
            doShareToWeiBoWithToken();
         } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = "授权失败";
            if (!TextUtils.isEmpty(code)) {
               message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
         }
      }

      @Override public void onCancel() {
         Utils.showMessage("取消授权");
      }

      @Override public void onWeiboException(WeiboException e) {
         Utils.showMessage("授权出错");
      }
   }

   public void setIsRegeister(boolean isRegeister) {
      this.isRegester = isRegeister;
   }

   public boolean getIsRegeister() {
      return isRegester;
   }
}
