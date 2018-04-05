package com.ilikezhibo.ggzb.avsdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper.FlipRoomHelper;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import de.greenrobot.event.EventBus;
import tinker.android.util.TinkerManager;

public class GameOverActivity extends BaseActivity implements IWeiboHandler.Response {
   public static String EXTRA_FLIPROOMHRLPER = "EXTRA_FLIPROOMHRLPER";
   private TextView mPraiseCountTextView;
   private TextView mViewerCountTextView;
   private TextView mPriceCountTextView;
   private String live_uid;
   private boolean hostleave = false;


   //有多少个看客
   public final static String EXTRA_VIEWS_NUM = "EXTRA_VIEWS_NUM";
   //是否是主播
   public final static String EXTRA_LEAVE_MODE = "EXTRA_LEAVE_MODE";

   //房间号
   public final static String EXTRA_ROOM_NUM = "EXTRA_ROOM_NUM";
   //点赞人数
   public final static String EXTRA_PRAISE_NUM = "EXTRA_PRAISE_NUM";
   //host_info
   public final static String EXTRA_HOST_INFO = "EXTRA_HOST_INFO";
   //本次主播收到的礼物钻石数
   public final static String EXTRA_PRICE_COUNT = "EXTRA_PRICE_COUNT";
   private TextView txt_atten;
   private MemberInfo hostMember;
   private UserInfo mSelfUserInfo;
   private TextView txt_atten_delete;
   private FlipRoomHelper flipRoomHelper;

   @Override protected void onCreate(Bundle savedInstanceState) {
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      super.onCreate(savedInstanceState);
      setContentView(R.layout.game_over_activity);

      live_uid = getIntent().getExtras().getString(EXTRA_ROOM_NUM);

      //观看人数与点赞人
      int views_count = getIntent().getExtras().getInt(EXTRA_VIEWS_NUM);
      mViewerCountTextView = (TextView) findViewById(R.id.viewercount);
      mViewerCountTextView.setText(views_count + "");

      int praise_count = getIntent().getExtras().getInt(EXTRA_PRAISE_NUM);
      mPraiseCountTextView = (TextView) findViewById(R.id.praisecount);
      mPraiseCountTextView.setText(praise_count + "");

      int pricecount = getIntent().getExtras().getInt(EXTRA_PRICE_COUNT);
      mPriceCountTextView = (TextView) findViewById(R.id.pricecount);
      mPriceCountTextView.setText(pricecount + "");

      hostleave = getIntent().getExtras().getBoolean(EXTRA_LEAVE_MODE);
      if (hostleave == true) {
         //是主播离开
         findViewById(R.id.line2).setVisibility(View.VISIBLE);
         findViewById(R.id.pricecount_ly).setVisibility(View.VISIBLE);
      } else {
         //普通看客离开
      }

      //关注处理
      txt_atten = (TextView) findViewById(R.id.txt_atten);
      txt_atten_delete = (TextView) findViewById(R.id.txt_delete_atten);
      //设置是显示添加还是删除管理
      boolean isExist = false;
      for (String uid1 : MainActivity.atten_uids) {
         if (uid1.equals(live_uid)) {
            isExist = true;
            break;
         }
      }
      if (isExist) {
         txt_atten.setVisibility(View.GONE);
         txt_atten_delete.setVisibility(View.VISIBLE);
      } else {
         txt_atten.setVisibility(View.VISIBLE);
         txt_atten_delete.setVisibility(View.GONE);
      }
      txt_atten.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            doAttend(live_uid);
         }
      });
      txt_atten_delete.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            doDelAttend(live_uid);
         }
      });

      //如果自己是主播，则关注不显示
      if (hostleave) {
         txt_atten.setVisibility(View.GONE);
         txt_atten_delete.setVisibility(View.GONE);
      }

      //返回主页，使用eventBus,关掉AVactivity
      ((Button) findViewById(R.id.return_main)).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {
            startActivity(new Intent(GameOverActivity.this, AULiveHomeActivity.class));
            finish();
         }
      });

      //分享处理
      //分享相关

      hostMember = (MemberInfo) getIntent().getExtras().getSerializable(EXTRA_HOST_INFO);
      mSelfUserInfo = AULiveApplication.getMyselfUserInfo();
      if (shareDialog == null) {
         shareDialog = new ShareHelper(GameOverActivity.this);
         shareDialog.setShareLiveuid(live_uid);
      }
      weibo_cb = (CheckBox) findViewById(R.id.weibo_cb);
      weibo_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               if (hostMember == null) {
                  return;
               }
               AULiveApplication auLiveApplication =
                   (AULiveApplication) TinkerManager.getTinkerApplicationLike();
               ReleaseLiveActivity.doSharePrepare(live_uid, auLiveApplication.getCity(), "weibo");

               String getuid = mSelfUserInfo.getUserPhone();
               String nickname = mSelfUserInfo.getUserName();
               //分享的内容
               String share_target_url = "http://web."
                   + UrlHelper.URL_domain
                   + "/play?uid="
                   + live_uid
                   + "&liveid="
                   + getuid
                   + "&share_uid="
                   + getuid
                   + "&share_from=weibo";

               shareDialog.setShareUrl(share_target_url);
               shareDialog.setShareTitle(Utils.trans(R.string.app_name));
               //内容文字，头像
               shareDialog.setShareContent(
                   "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + "！" + share_target_url,
                   hostMember.getHeadImagePath());

               shareDialog.doShareToWeiBo();
            }
         }
      });
      weixin_cb = (CheckBox) findViewById(R.id.weixin_cb);
      weixin_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               if (hostMember == null) {
                  return;
               }
               AULiveApplication auLiveApplication =
                   (AULiveApplication) TinkerManager.getTinkerApplicationLike();
               ReleaseLiveActivity.doSharePrepare(live_uid, auLiveApplication.getCity(), "weixin");

               startProgressDialog();
               String getuid = mSelfUserInfo.getUserPhone();
               String nickname = mSelfUserInfo.getUserName();
               //分享的内容
               String share_target_url = "http://web."
                   + UrlHelper.URL_domain
                   + "/play?uid="
                   + live_uid
                   + "&liveid="
                   + getuid
                   + "&share_uid="
                   + getuid
                   + "&share_from=weixin";

               shareDialog.setShareUrl(share_target_url);
               shareDialog.setShareTitle(Utils.trans(R.string.app_name));
               //内容文字，头像
               shareDialog.setShareContent(
                   "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + "！" + share_target_url,
                   hostMember.getHeadImagePath());
               shareDialog.doShareToWeiXin();
            }
         }
      });
      friend_circle_cb = (CheckBox) findViewById(R.id.friend_circle_cb);
      friend_circle_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               if (hostMember == null) {
                  return;
               }
               AULiveApplication auLiveApplication =
                   (AULiveApplication) TinkerManager.getTinkerApplicationLike();
               ReleaseLiveActivity.doSharePrepare(live_uid, auLiveApplication.getCity(),
                   "friend_circle");
               startProgressDialog();
               String getuid = mSelfUserInfo.getUserPhone();
               String nickname = mSelfUserInfo.getUserName();
               //分享的内容
               String share_target_url = "http://web."
                   + UrlHelper.URL_domain
                   + "/play?uid="
                   + live_uid
                   + "&liveid="
                   + getuid
                   + "&share_uid="
                   + getuid
                   + "&share_from=friend_circle";

               shareDialog.setShareUrl(share_target_url);
               shareDialog.setShareTitle(Utils.trans(R.string.app_name));
               //内容文字，头像
               shareDialog.setShareContent(
                   "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + "！" + share_target_url,
                   hostMember.getHeadImagePath());
               shareDialog.doShareToWeiXinFriend();
            }
         }
      });
      qq_cb = (CheckBox) findViewById(R.id.qq_cb);
      qq_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               if (hostMember == null) {
                  return;
               }
               AULiveApplication auLiveApplication =
                   (AULiveApplication) TinkerManager.getTinkerApplicationLike();
               ReleaseLiveActivity.doSharePrepare(live_uid, auLiveApplication.getCity(), "qq");
               String getuid = mSelfUserInfo.getUserPhone();
               String nickname = mSelfUserInfo.getUserName();
               //分享的内容
               String share_target_url = "http://web."
                   + UrlHelper.URL_domain
                   + "/play?uid="
                   + live_uid
                   + "&liveid="
                   + getuid
                   + "&share_uid="
                   + getuid
                   + "&share_from=qq";

               shareDialog.setShareUrl(share_target_url);
               shareDialog.setShareTitle(Utils.trans(R.string.app_name));
               //内容文字，头像
               shareDialog.setShareContent(
                   "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + "！" + share_target_url,
                   hostMember.getHeadImagePath());
               shareDialog.doShareToQQ();
            }
         }
      });
      qzone_cb = (CheckBox) findViewById(R.id.qzone_cb);
      qzone_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               if (hostMember == null) {
                  return;
               }
               AULiveApplication auLiveApplication =
                   (AULiveApplication) TinkerManager.getTinkerApplicationLike();
               ReleaseLiveActivity.doSharePrepare(live_uid, auLiveApplication.getCity(), "qzone");
               String getuid = mSelfUserInfo.getUserPhone();
               //分享的内容
               String share_target_url = "http://web."
                   + UrlHelper.URL_domain
                   + "/play?uid="
                   + live_uid
                   + "&liveid="
                   + getuid
                   + "&share_uid="
                   + getuid
                   + "&share_from=qzone";

               shareDialog.setShareUrl(share_target_url);
               shareDialog.setShareTitle(Utils.trans(R.string.app_name));
               //内容文字，头像
               shareDialog.setShareContent(
                   "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + "！" + share_target_url,
                   hostMember.getHeadImagePath());

               shareDialog.doShareToQQZone();
            }
         }
      });



      Bundle bundle = getIntent().getParcelableExtra(GameOverActivity.EXTRA_FLIPROOMHRLPER);
      if(bundle != null) {
         flipRoomHelper = (FlipRoomHelper) bundle.getParcelable(GameOverActivity.EXTRA_FLIPROOMHRLPER);
         Trace.d("**>>>接收到fliproomhelper" + flipRoomHelper.toString());

      }
   }



   @Override protected void onResume() {
      super.onResume();
      stopProgressDialog();
      //清除所有选项
      weibo_cb.setChecked(false);
      weixin_cb.setChecked(false);
      friend_circle_cb.setChecked(false);
      qq_cb.setChecked(false);
      qzone_cb.setChecked(false);
   }

   private ShareHelper shareDialog = null;
   private CheckBox weibo_cb;
   private CheckBox weixin_cb;
   private CheckBox friend_circle_cb;
   private CheckBox qq_cb;
   private CheckBox qzone_cb;

   private void doAttend(final String uid) {

      RequestInformation request = new RequestInformation(UrlHelper.ROOM_ADD_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               if (MainActivity.atten_uids.contains(uid)) {
               } else {
                  MainActivity.atten_uids.add(uid);
               }
               txt_atten.setVisibility(View.GONE);
               txt_atten_delete.setVisibility(View.VISIBLE);
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void doDelAttend(final String uid) {

      RequestInformation request = new RequestInformation(UrlHelper.ROOM_DEL_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               if (MainActivity.atten_uids.contains(uid)) {
                  MainActivity.atten_uids.remove(uid);
               } else {

               }
               txt_atten.setVisibility(View.VISIBLE);
               txt_atten_delete.setVisibility(View.GONE);
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private CustomProgressDialog progressDialog = null;

   private void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(this);
         progressDialog.setMessage("加载中");
      }

      progressDialog.show();
   }

   private void stopProgressDialog() {
      if (progressDialog != null) {
         progressDialog.dismiss();
         progressDialog = null;
      }
   }

   @Override public void finish() {
      super.finish();
      EventBus.getDefault().post(new CloseAvActivityEvent());
   }

   @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      try {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareDialog.qq_IUiListener);
         }
         if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                shareDialog.qzone_IUiListener);
         }
         // SSO 授权回调
         // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
         if (shareDialog != null && shareDialog.mSsoHandler != null) {
            shareDialog.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
         }
      } catch (Exception e) {
      }
   }

   @Override protected void onDestroy() {
      super.onDestroy();
      if (shareDialog != null) {
         shareDialog.dismiss();
         GameOverActivity.this.unregisterReceiver(shareDialog.getShareReceiver());
      }
   }

   @Override public void onResponse(BaseResponse baseResp) {

      Trace.d("GameOverActivity onResponse");
      switch (baseResp.errCode) {
         case WBConstants.ErrorCode.ERR_OK:
            Utils.showCroutonText(GameOverActivity.this,
                Utils.trans(R.string.weibosdk_share_success));
            shareDialog.doWeiboShareCallback();
            break;
         case WBConstants.ErrorCode.ERR_CANCEL:
            Utils.showCroutonText(GameOverActivity.this,
                Utils.trans(R.string.weibosdk_share_canceled));
            break;
         case WBConstants.ErrorCode.ERR_FAIL:
            Utils.showCroutonText(GameOverActivity.this,
                Utils.trans(R.string.weibosdk_share_failed));
            break;
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
      this.finish();
      Trace.d("**>>>传出了event");
      MotionEvent eventClone = MotionEvent.obtain(event);
      eventClone.setLocation(event.getRawX(), event.getRawY());
     if(flipRoomHelper != null && flipRoomHelper.mWatchSwitchViewPager != null) {

        flipRoomHelper.mWatchSwitchViewPager.dispatchTouchEvent(eventClone);
     }
      return super.onTouchEvent(event);
   }
}
