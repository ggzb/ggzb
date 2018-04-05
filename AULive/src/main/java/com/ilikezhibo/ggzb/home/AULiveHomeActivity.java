package com.ilikezhibo.ggzb.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.ilikezhibo.ggzb.avsdk.activity.ReleaseLiveActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.paytop.TotalRankFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.paytop.TotalTopRankFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankFragment;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondFragment;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import com.ilikezhibo.ggzb.avsdk.home.ProgramListFragment;
import com.ilikezhibo.ggzb.entity.UpdateApkEntity;
import com.ilikezhibo.ggzb.find.FindFragment;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;
import com.ilikezhibo.ggzb.news.NewsFragment;
import com.ilikezhibo.ggzb.userinfo.MyUserInfoFragment;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import update.Constants;
import update.DownloadService;

/**
 * @author big
 * @ClassName: AULiveHomeActivity
 * @Description: ILVB
 * @date 2014-3-18 下午11:29:52
 */
public class AULiveHomeActivity extends BaseFragmentActivity implements NavigationListener {
   public static final String INTENT_MSG_ID_KEY = "INTENT_MSG_ID_KEY";
   public static final String INTENT_FRIEND_USERINFO_KEY = "INTENT_FRIEND_USERINFO_KEY";

   public static int oldType = NavigationManager.TYPE_BANGMAMATAO_ZHOUFANGZI;

   private int mCurrentFrameId;
   // SquareFragment
   private ProgramListFragment squareFragment;// 广场
   private FindFragment msgFragment;// 消息
   private NewsFragment findFragment;// 新闻
   private MyUserInfoFragment myInfoFragment;// 我的资料
   private TotalTopRankFragment mRankFragment;
   private BuyDiamondFragment mDiamondFragment;


   private HashMap<String, Fragment> mCachedFragments = new HashMap<String, Fragment>();
   private NavigationManager navigationManager;
   private View bottomView;

   public static AULiveHomeActivity auLiveHomeActivity;

   @Override protected void setContentView() {
      auLiveHomeActivity = this;
      Trace.d("AULiveHomeActivity setContentView");
      setContentView(R.layout.jiesihuo_home);
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

      //监听home键
      IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
      registerReceiver(homeReceiver, homeFilter);

      // >如果1v1版本第一次登录,弹出开通1v1对话框 从网络上获取是否是新用户或者检查是否开通
//      if (getIsSoloFirstLogin()) {
//         final CustomDialog customDialog = new CustomDialog(this, new CustomDialogListener() {
//
//            @Override public void onDialogClosed(int closeType) {
//               switch (closeType) {
//                  case CustomDialogListener.BUTTON_POSITIVE:
//                     Trace.d("**>确定开通");
//                     // >开通1v1的功能;
//                     SoloMgmtUtils.openOnetOne(new SoloRequestListener() {
//                        @Override public void onSuccess() {
//
//                        }
//
//                        @Override public void onFailure() {
//
//                        }
//                     });
//                     Intent intent_videochat =
//                         new Intent(AULiveHomeActivity.this, VideoChatCondActivity.class);
//                     startActivity(intent_videochat);
//                     break;
//
//                  case CustomDialogListener.BUTTON_NEUTRAL:
//                     Trace.d("**>>取消开通");
//                     // >显示提示到设置里开通
//                     ViewGroup toastRoot =
//                         (ViewGroup) getLayoutInflater().inflate(R.layout.my_toast, null);
//                     Utils.setCustomViewToast(toastRoot, true, "主人稍后可以前往设置里开启哦");
//                     break;
//               }
//            }
//         });
//         customDialog.setCustomMessage("开通此功能可以和他人 1 对 1 视频聊天，" + "接受他人的视频聊天邀请，您每分钟都会获得" + Utils.trans(R.string.app_money) + "收益。");
//         customDialog.setCheckboxContent(true, "接受在线视频聊天");
//         customDialog.setCheckboxChecked(true);
//         // >设置点击别处不让对话框消失
//         customDialog.setCanceledOutside(false);
//         customDialog.setCancelable(true);
//         customDialog.setAndFormatTitle("限时免费开通", true, Color.WHITE);
//         customDialog.setType(CustomDialog.DOUBLE_BTN);
//         customDialog.show();
//         setIsSoloFirstLogin();
//      }
   }

   /**
    * 设置
    */
   private void setIsSoloFirstLogin() {
      SharedPreferenceTool.getInstance().saveBoolean(SharedPreferenceTool.IS_FIRST_ONETONE +
              AULiveApplication.getUserInfo().getUid(), false);
   }

   /**
    * 访问网络,确定用户是否是1v1的新用户
    */
   private boolean getIsSoloFirstLogin() {
      Trace.d("**>>"
              + AULiveApplication.getUserInfo().getSex()
              + ">>>"
              + AULiveApplication.getUserInfo().getOnetone());
      return SharedPreferenceTool.getInstance().getBoolean(
              SharedPreferenceTool.IS_FIRST_ONETONE + AULiveApplication.getUserInfo().getUid(), true)
              && AULiveApplication.getUserInfo().getSex() == 2
              && AULiveApplication.getUserInfo().getOnetone() == 0;
   }

   @Override protected void initializeViews() {
      squareFragment = new ProgramListFragment();

      // 导航栏相关
      bottomView = findViewById(R.id.bottomLayout);

      tab_unread_bg_qiuzhu = bottomView.findViewById(R.id.tab_unread_bg_find);

      navigationManager =
          new NavigationManager(bottomView, NavigationManager.TYPE_BANGMAMATAO_ZHOUFANGZI, this);

      Button tab_add_bt = (Button) findViewById(R.id.tab_add_bt);
      tab_add_bt.setOnClickListener(new View.OnClickListener() {

         @Override public void onClick(View arg0) {
            if (Utils.isLogin(AULiveHomeActivity.this)) {
               //Intent faxingmu_intent =
               //    new Intent(AULiveHomeActivity.this, FaXiangMuStep1Activity.class);
               //faxingmu_intent.putExtra(FaXiangMuStep1Activity.IS_IGNORD, true);
               //startActivity(faxingmu_intent);
               startActivity(new Intent(AULiveHomeActivity.this, ReleaseLiveActivity.class));
            }
         }
      });
   }

   @Override protected void initializeData() {
      mCurrentFrameId = R.id.mActivitySquareHallFrame;
      mCachedFragments.put(R.id.mActivitySquareHallFrame + "", squareFragment);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.mActivitySquareHallFrame, squareFragment)
          .commitAllowingStateLoss();
      getSupportLoaderManager();

      //Trace.d("home baidu push api_key:" + Utils.getMetaValue(AULiveHomeActivity.this, "api_key"));
      //PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
      //    Utils.getMetaValue(AULiveHomeActivity.this, "api_key"));
///////////////////////////////////////////////////////////////////////////////
      try {
         //信鸽相关
         // 开启logcat输出，方便debug，发布时请关闭
         XGPushConfig.enableDebug(this, false);
// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
// 具体可参考详细的开发指南
// 传递的参数为ApplicationContext
         Context context = getApplicationContext();
         String uid = AULiveApplication.getUserInfo().getUid();
         XGPushManager.registerPush(context, uid, new XGIOperateCallback() {
            @Override public void onSuccess(Object o, int i) {
               Trace.d("XGPushManager onSuccess token:" + o);
               uploadUserId(o + "");
            }

            @Override public void onFail(Object o, int i, String s) {
               Trace.d("XGPushManager onFail" + s);
            }
         });

      } catch (Exception e) {
      }
// 2.36（不包括）之前的版本需要调用以下2行代码
//      Intent service = new Intent(context, XGPushService.class);
//      context.startService(service);

// 其它常用的API：
// 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
// 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
// 反注册（不再接收消息）：unregisterPush(context)
// 设置标签：setTag(context, tagName)
// 删除标签：deleteTag(context, tagName)

////////////////////////////////////////////////////////////////////////////////////
      // 友盟自动更新
      //Utils.updateApp(this, true);

      UpdateApkEntity updateApkEntity =
          (UpdateApkEntity) getIntent().getSerializableExtra(MainActivity.UPDATE_APK_KEY);

      try {
         if (updateApkEntity != null) {
            int versionCode =
                this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
            if (updateApkEntity.versonCode > versionCode) {
               //UpdateChecker.checkForDialog(AULiveHomeActivity.this, updateApkEntity);
               //UpdateChecker.checkForNotification(AULiveHomeActivity.this,APP_UPDATE_SERVER_URL);
               showUpDateDialog(updateApkEntity.updateUrl, updateApkEntity.upType,
                   updateApkEntity.updateMessage);
            } else {
               //Toast.makeText(mContext, mContext.getString(R.string.app_no_new_update), Toast.LENGTH_SHORT).show();
            }
         }
      } catch (PackageManager.NameNotFoundException e) {
         e.printStackTrace();
      }

      ////////////////////////////////////////////////////////////////////////////////////
      //跟新patch补丁

      ActivityStackManager.getInstance().pushActivity(this);

      RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
   }

   @Override protected void onResume() {
      super.onResume();
      Trace.d("AULiveHomeActivity onresume");

      AULiveApplication.is_on_background= false;

      //当按home键返回后,重连容云
      if (has_home_key_press) {
         has_home_key_press = false;
         //用户按home键返回后回来，重连融云
         try {
            if (AULiveApplication.getUserInfo() != null) {
               String im_token = AULiveApplication.getUserInfo().getIm_token();
               if (im_token != null) {
                  connect(im_token, AULiveHomeActivity.this);
               }
            }
         } catch (Exception e) {

         }
      }
      //rongcloud 不是在连接以及连接中,做重连处理
      RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus =
          RongIM.getInstance().getCurrentConnectionStatus();
      if (connectionStatus != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED
          && connectionStatus
          != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
         String im_token = AULiveApplication.getUserInfo().getIm_token();
         if (im_token != null) {
            Trace.d("不是在连接以及连接中,做重连处理");
            AULiveHomeActivity.connect(im_token, AULiveHomeActivity.this);
         }
      }

      home_activity_has_pause = false;
      //如果没登录，先登录
      if (!Utils.isLogin(AULiveHomeActivity.this)) {
         Utils.showCroutonText(this, "你还没登录");
         this.finish();
         return;
      }
      // 推送跳转
      // String index = getIntent().getStringExtra(
      // PushMessageReceiver.back_home_index_key);
      // if (index != null && !index.equals("")) {
      // doPushJump(index);
      // }
      // // 检查是否要更新红点
      // hasNewProduct();
      AULiveApplication.is_on_home_context = true;
      AULiveApplication.mAuLiveHomeActivity = this;
      AULiveApplication.mAvActivity = null;
      //EventBus.getDefault().post(new UpDateUnReadEvent());

   }

   public static boolean home_activity_has_pause = false;

   @Override protected void onPause() {
      super.onPause();
      Trace.d("AULiveHomeActivity onPause");
      home_activity_has_pause = true;
   }

   private static long firstTime;

   // 连续按两次返回键就退出
   @Override public void onBackPressed() {

      if (firstTime + 2000 > System.currentTimeMillis()) {
         super.onBackPressed();
         //AULiveHomeActivity.this.finish();
      } else {
         //Utils.showMessage("再按一次退出程序");
         Utils.showCroutonText(AULiveHomeActivity.this, "再按一次退出程序");
      }
      firstTime = System.currentTimeMillis();
   }

   public void doPushJump(String jump_index) {
      if (jump_index != null && !jump_index.equals("")) {
         View view = new View(this);
         if (jump_index.equals("0")) {
            view.setId(R.id.maintab_layout_1);
         } else if (jump_index.equals("1")) {
            view.setId(R.id.maintab_layout_2);
         } else if (jump_index.equals("2")) {
            view.setId(R.id.maintab_layout_3);
         } else if (jump_index.equals("3")) {
            view.setId(R.id.maintab_layout_4);
         } else if (jump_index.equals("4")) {
            view.setId(R.id.maintab_layout_5);
         }
         navigationManager.onClick(view);
      }
   }

   @Override public void onClickCurrBang(int currType) {
      switch (currType) {
         case NavigationManager.TYPE_BANGMAMATAO_ZHOUFANGZI:
            oldType = NavigationManager.TYPE_BANGMAMATAO_ZHOUFANGZI;
            if (!mCachedFragments.containsKey(R.id.mActivitySquareHallFrame + "")) {
               squareFragment = new ProgramListFragment();
               mCachedFragments.put(R.id.mActivitySquareHallFrame + "", squareFragment);
               replaceCenterFragment(squareFragment, R.id.mActivitySquareHallFrame);
            } else {
               replaceCenterFragment(null, R.id.mActivitySquareHallFrame);
            }

            break;
         case NavigationManager.TYPE_JIESIHUO_MSG:
            oldType = NavigationManager.TYPE_JIESIHUO_MSG;
            if (!mCachedFragments.containsKey(R.id.mActivityMsgFrame + "")) {
               msgFragment = new FindFragment();
               mCachedFragments.put(R.id.mActivityMsgFrame + "", msgFragment);
               replaceCenterFragment(msgFragment, R.id.mActivityMsgFrame);
            } else {
               replaceCenterFragment(null, R.id.mActivityMsgFrame);
            }

            if (AULiveApplication.getUserInfo() == null
                || AULiveApplication.getUserInfo().getUid() == null
                || AULiveApplication.getUserInfo().getNickname() == null) {
               navigationManager.showNewMsgTotal(0);
            }

            // 去红点
            if (has_new_time > 0) {
               SharedPreferenceTool.getInstance().saveString(qiuzhu_key, "" + has_new_time);
               tab_unread_bg_qiuzhu.setVisibility(View.GONE);
            }

            break;
         case NavigationManager.TYPE_BANGMAMATAO_LIANXIRAN:
            oldType = NavigationManager.TYPE_BANGMAMATAO_LIANXIRAN;
            if (!mCachedFragments.containsKey(R.id.mActivityFindFrame + "")) {
               findFragment = new NewsFragment();
               mCachedFragments.put(R.id.mActivityFindFrame + "", findFragment);
               replaceCenterFragment(findFragment, R.id.mActivityFindFrame);
            } else {
               replaceCenterFragment(null, R.id.mActivityFindFrame);
            }
            break;
         case NavigationManager.TYPE_BANGMAMATAO_MYINFO:
            oldType = NavigationManager.TYPE_BANGMAMATAO_MYINFO;
            if (!mCachedFragments.containsKey(R.id.mActivityMyInfoFrame + "")) {
               myInfoFragment = new MyUserInfoFragment();
               mCachedFragments.put(R.id.mActivityMyInfoFrame + "", myInfoFragment);
               replaceCenterFragment(myInfoFragment, R.id.mActivityMyInfoFrame);
            } else {
               replaceCenterFragment(null, R.id.mActivityMyInfoFrame);
            }
            break;
         case NavigationManager.TYPE_RANK:
            oldType = NavigationManager.TYPE_RANK;
            if (!mCachedFragments.containsKey(R.id.mActivityRankFrame + "")) {
                mRankFragment= new TotalTopRankFragment();
               mCachedFragments.put(R.id.mActivityRankFrame + "", mRankFragment);
               replaceCenterFragment(mRankFragment, R.id.mActivityRankFrame);
            } else {
               replaceCenterFragment(null, R.id.mActivityRankFrame);
            }
            break;
         case NavigationManager.TYPE_PAY:
            oldType = NavigationManager.TYPE_PAY;
            if (!mCachedFragments.containsKey(R.id.mActivityPayFrame + "")) {
                mDiamondFragment= new BuyDiamondFragment();
               mCachedFragments.put(R.id.mActivityPayFrame + "", mDiamondFragment);
               replaceCenterFragment(mDiamondFragment, R.id.mActivityPayFrame);
            } else {
               replaceCenterFragment(null, R.id.mActivityPayFrame);
            }
            break;
      }
   }

   private void replaceCenterFragment(Fragment fragment, int frameId) {
      findViewById(mCurrentFrameId).setVisibility(View.GONE);
      findViewById(frameId).setVisibility(View.VISIBLE);
      mCurrentFrameId = frameId;
      if (fragment != null) {
         getSupportFragmentManager().beginTransaction()
             .replace(frameId, fragment)
             .commitAllowingStateLoss();
      }
   }

   @Override protected void onStop() {
      super.onStop();
      Trace.d("AULiveHomeActivity onStop");
   }

   public static String qiuzhu_key = "qiuzhu_key";

   private View tab_unread_bg_qiuzhu;
   private long has_new_time = 0;

   @Override protected void onDestroy() {
      super.onDestroy();
      ActivityStackManager.getInstance().exitActivity();
      Trace.d("AULiveHomeActivity onDestroy");
      //剪切版广告计数
      AULiveApplication.adv_count = 0;

      //防内存泄漏
      AULiveApplication.mAvActivity = null;
      AULiveApplication.mPrivateChatActivity = null;
      AULiveApplication.mAuLiveHomeActivity = null;
      AULiveApplication.is_on_home_context = false;
      AULiveApplication.is_on_background= true;
      //取消监听HOME键
      if (homeReceiver != null) {
         try {
            unregisterReceiver(homeReceiver);
         } catch (Exception e) {
            Trace.d("unregisterReceiver homeReceiver failure :" + e.getCause());
         }
      }
   }

   private boolean isUpload = false;

   //上传信鸽token
   private void uploadUserId(String userId) {
      if (isUpload) {
         return;
      }
      isUpload = true;
      RequestInformation request = new RequestInformation(UrlHelper.getUploadBaiduUser_Id(userId),
          RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            isUpload = false;
         }

         @Override public void onCallback(String callback) {
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

   public class UIDAndTime {
      public UIDAndTime(String uid1, long time1) {
         uid = uid1;
         time = time1;
      }

      public String uid;
      public long time;
   }

   private ArrayList<UIDAndTime> adv_filter_arraylist = new ArrayList<UIDAndTime>();
   private ArrayList<String> adv_filter_uidlist = new ArrayList<String>();

   //去广告
   private void doWeixinGG(String memo) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.SERVER_URL + "other/adfilter?memo=" + URLEncoder.encode(memo, "utf-8"));
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<EnterRoomEntity>() {

         @Override public void onCallback(EnterRoomEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showCroutonText(AULiveHomeActivity.this, callback.getMsg());
            } else {
               Utils.showCroutonText(AULiveHomeActivity.this, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }

   //自己的发送监听器,AVActivity 创建后会被取代
   private class MySendMessageListener implements RongIM.OnSendMessageListener {

      /**
       * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
       *
       * @param message 发送的消息实例。
       * @return 处理后的消息实例。
       */
      @Override public Message onSend(final Message message) {

         if (message == null || message.getContent() == null) {
            return message;
         }

         ////////////////////////////////////////////////////////
         //   广告处理
         if (message.getContent() instanceof TextMessage) {//文本消息
            if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
               TextMessage textMessage = (TextMessage) message.getContent();
               try {
                  //判断广告
                  int grade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
                  String msg = textMessage.getContent();
                  if (msg == null) {
                     return null;
                  }
                  //////////////////
                  if (grade < 5) {
                     //去微信
                     adv_filter_arraylist.add(
                         new UIDAndTime(message.getTargetId(), System.currentTimeMillis()));
                     //不存在才加入，加入时判断，算法最简单,方法1
                     if (!adv_filter_uidlist.contains(message.getTargetId())) {
                        adv_filter_uidlist.add(message.getTargetId());
                     }

                     //1分钟发6次不同用户，包含微信，等级小于5
                     //Trace.d("adv_filter_uidlist:" + adv_filter_uidlist.size());
                     if (adv_filter_arraylist.size() > 5) {
                        long last_time =
                            adv_filter_arraylist.get(adv_filter_arraylist.size() - 1).time;
                        long first_time =
                            adv_filter_arraylist.get(adv_filter_arraylist.size() - 6).time;
                        //1分钟之内
                        Trace.d("last_time - first_time:" + (last_time - first_time));
                        if (last_time - first_time < 60 * 1000) {

                           //包含6次不同用户
                           //排序遍历方法2
                           //int same_times_count = adv_filter_uidlist.size();
                           //Collections.sort(adv_filter_uidlist);
                           //while (adv_filter_uidlist.size() > 0) {
                           //   //从后面开始算
                           //   int times = Collections.frequency(adv_filter_uidlist,
                           //       adv_filter_uidlist.get(adv_filter_uidlist.size() - 1));
                           //   //同样的算一个
                           //   same_times_count = same_times_count - times + 1;
                           //   while (times > 0) {
                           //      adv_filter_uidlist.remove(adv_filter_uidlist.size() - 1);
                           //      times--;
                           //   }
                           //}
                           if (adv_filter_uidlist.size() > 5) {
                              Trace.d("有广告嫌疑");
                              //有广告嫌疑
                              doWeixinGG(msg);
                           }
                        }
                     }
                     //清空
                     if (adv_filter_arraylist.size() > 8) {
                        adv_filter_arraylist.clear();
                        adv_filter_uidlist.clear();
                     }
                  }

                  String clipBoardText = getClipboardText();
                  if (clipBoardText != null && !clipBoardText.equals("")) {
                     if (clipBoardText.equals(textMessage.getContent())) {
                        AULiveApplication.adv_count++;
                        //3次以上是广告及5级以下
                        if (AULiveApplication.adv_count > 3 && grade < 6) {
                           return null;
                        }
                     }
                  }
               } catch (Exception e) {
               }
            }
         }

         return message;
      }

      /**
       * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
       *
       * @param message 消息实例。
       * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
       * @return true 表示走自已的处理方式，false 走融云默认处理方式。
       */
      @Override public boolean onSent(Message message,
          RongIM.SentMessageErrorCode sentMessageErrorCode) {

         if (message.getSentStatus() == Message.SentStatus.FAILED) {
            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
               //不在聊天室
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
               //不在讨论组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
               //不在群组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
               //你在他的黑名单中
            }
         }

         MessageContent messageContent = message.getContent();

         if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Trace.d("onSent-TextMessage:" + textMessage.getContent());
         } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Trace.d("onSent-ImageMessage:" + imageMessage.getRemoteUri());
         } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Trace.d("onSent-voiceMessage:" + voiceMessage.getUri().toString());
         } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Trace.d("onSent-RichContentMessage:" + richContentMessage.getContent());
         } else {
            Trace.d("onSent-其他消息，自己来判断处理");
         }

         return false;
      }
   }

   private String getClipboardText() {
      try {
         ClipboardManager clipboardManager =
             (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
         if (clipboardManager.hasPrimaryClip()) {
            return clipboardManager.getPrimaryClip().getItemAt(0).getText() + "";
         }
      } catch (Exception e) {
      }
      return null;
   }

   /////////////////////////////////////////////////////
   //有按home键吗
   private boolean has_home_key_press = false;
   private final BroadcastReceiver homeReceiver = new BroadcastReceiver() {
      final String SYS_KEY = "reason"; //标注下这里必须是这么一个字符串值

      final String SYS_HOME_KEY = "homekey";//标注下这里必须是这么一个字符串值

      @Override public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();
         if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYS_KEY);
            if (reason != null && reason.equals(SYS_HOME_KEY)) {
               Trace.d("HomeKey Press");
               String brand = android.os.Build.BRAND.toLowerCase();
               String manufacturer = android.os.Build.MANUFACTURER.toLowerCase();
               //只在home界面处理,只处理oppo的机型

               has_home_key_press = true;

               //if (home_activity_has_pause == false && (brand.equals("oppo") || manufacturer.equals(
               //    "oppo"))) {
               //   AULiveHomeActivity.this.finish();
               //}
            }
         }
      }
   };

   ///////////////////////////////////////////////////////////
   //更新相关
   //update_type= 1强制更新 0,默认提示跟新
   private void showUpDateDialog(final String apk_url, final int update_type,
       final String update_content) {
      final CustomDialog customDialog =
          new CustomDialog(AULiveHomeActivity.this, new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      showNotification(Utils.trans(R.string.app_name) + "更新", apk_url);
                      //Utils.showMessage("请稍后");
                      break;
                   case CustomDialogListener.BUTTON_NEUTRAL:
                      if (update_type == 1) {
                         AULiveHomeActivity.this.finish();
                      }
                      break;
                }
             }
          });

      customDialog.setCustomMessage("" + update_content);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
      //一定要在show之后
      customDialog.setCustomTitle("应用更新");
      customDialog.setContentTextSize(14);
      customDialog.setCancelable(false);
      customDialog.setCanceledOnTouchOutside(false);
   }

   public void showNotification(String content, String apkUrl) {
      //Notification noti;
      //Intent myIntent = new Intent(this, DownloadService.class);
      //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      //myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
      //PendingIntent pendingIntent =
      //    PendingIntent.getService(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      //
      //int smallIcon = this.getApplicationInfo().icon;
      //noti = new NotificationCompat.Builder(this).setTicker(getString(R.string.newUpdateAvailable))
      //    .setContentTitle(getString(R.string.newUpdateAvailable))
      //    .setContentText(content)
      //    .setSmallIcon(smallIcon)
      //    .setContentIntent(pendingIntent)
      //    .build();
      //
      //noti.flags = Notification.FLAG_AUTO_CANCEL;
      //NotificationManager notificationManager =
      //    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
      //notificationManager.notify(0, noti);

      Intent intent = new Intent(this.getApplicationContext(), DownloadService.class);
      intent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
      this.startService(intent);
   }

   @Override public void onTrimMemory(int level) {
      super.onTrimMemory(level);
      //level可以选择60或者80 . 监测到就自己干掉自己
      Trace.d("AULiveHomeActivity onTrimMemory(int level):level=" + level);
   }

   @Override public void onLowMemory() {
      super.onLowMemory();
      Trace.d("AULiveHomeActivity onLowMemory");
   }

   @Override
   public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
      super.onSaveInstanceState(outState, outPersistentState);
      Trace.d("AULiveHomeActivity onSaveInstanceState");
   }

   @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      Trace.d("AULiveHomeActivity onRestoreInstanceState");
   }

   @Override public void finish() {
      super.finish();
   }

   /**
    * 建立与融云服务器的连接
    */
   public static void connect(String token, Activity activity) {

      if (activity.getApplicationInfo().packageName.equals(
          AULiveApplication.getCurProcessName(activity.getApplicationContext()))) {

          /**
          * IMKit SDK调用第二步,建立与服务器的连接
          */
         RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override public void onTokenIncorrect() {

               Trace.d("MainActivity--onTokenIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override public void onSuccess(String userid) {

               Trace.d("MainActivity --onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override public void onError(RongIMClient.ErrorCode errorCode) {

               Trace.d("MainActivity--onError" + errorCode);
            }
         });
      }
   }

   /**
    * 主界面不设置退出动画
    */
   @Override protected void exitAnim() {

   }

}
