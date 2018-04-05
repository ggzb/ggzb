package com.ilikezhibo.ggzb.home;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.MobileConfig;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.entity.UidListEntity;
import com.ilikezhibo.ggzb.avsdk.badwordfilter.BadWordDownloadHelper;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UpdateApkEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.welcome.GuideActivity;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author big
 * @ClassName: MainActivity
 * @Description: 入口
 * @date 2014-3-18 下午11:12:44
 */
public class
MainActivity extends BaseActivity {
   private MobileConfig config;
   public static final String INTENT_INVITE_CODE_KEY = "INTENT_INVITE_CODE_KEY";
   private boolean alreadyRequest;
   private int mPhoneState;
   private int mWrite_storage;
   private int mRead_storage;
   private int mCoarse_location;
   private int mFine_location;

   @Override protected void onCreate(Bundle savedInstanceState) {
      mPhoneState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
      mWrite_storage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
      mRead_storage = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
      mCoarse_location = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
      mFine_location = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

      if (mPhoneState != PackageManager.PERMISSION_GRANTED || mWrite_storage != PackageManager.PERMISSION_GRANTED || mRead_storage != PackageManager.PERMISSION_GRANTED || mCoarse_location != PackageManager.PERMISSION_GRANTED || mFine_location != PackageManager.PERMISSION_GRANTED) {
         String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
         ActivityCompat.requestPermissions(this, permissions, 2);
//         init();
      } else {
//         init();
         init();
      }
      super.onCreate(savedInstanceState);
      Trace.d(
          "MainActivity lanch_time:" + (System.currentTimeMillis() - AULiveApplication.lanch_time));
      // 去掉系统状态栏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_main);

// 判断是否从推送通知栏打开的

      XGPushClickedResult click = XGPushManager.onActivityStarted(this);
      if (click != null) {
         //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
         //查看是不是全新打开的面板
         if (isTaskRoot()) {
            return;
         }
         //如果有面板存在则关闭当前的面板
         finish();
      }

   }

   @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
         Utils.showCroutonText(MainActivity.this, "没有电话权限,请打开再尝试");
         return;
      } else {
         init();
      }

   }

   @Override protected void onResume() {
      super.onResume();
      //printDeviceInf();
//      init();
      //startActivity(new Intent(MainActivity.this, LoginActivity.class));
   }

   @Override protected void onPause() {
      super.onPause();
   }

   private static final int GO_HOME = 100;
   private static final int GO_GUIDE = 200;
   boolean isFirst = false;
   private Handler mHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         switch (msg.what) {
            case GO_HOME:
               goHome();
               break;
            case GO_GUIDE:
               goGuide();
               break;
         }
      }
   };

   private void init() {
      if (mPhoneState != PackageManager.PERMISSION_GRANTED) {
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            Trace.d("No CAMERA or AudioRecord permission, please check");
//            Utils.showCroutonText(MainActivity.this, "没有电话权限,请打开再尝试");
//            return;
//         }
      }
      mHandler.sendEmptyMessageDelayed(GO_HOME, 150);
//      SharedPreferences preferences = getSharedPreferences("first_pref", MODE_PRIVATE);
//      isFirst = preferences.getBoolean("isFirst", true);
//      if (!isFirst) {
//         mHandler.sendEmptyMessageDelayed(GO_HOME, 150);
//      } else {
//         mHandler.sendEmptyMessageDelayed(GO_GUIDE, 150);
//      }
   }

   private void goHome() {
//      requestStep();
      requestNewDomain();
   }

   private void goGuide() {
      Intent intent = new Intent(MainActivity.this, GuideActivity.class);
      startActivity(intent);
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      this.finish();
   }

   public static String URL_DOMAIN_key = "URL_DOMAIN_key";
   public static String default_URL_DOMAIN_url = "";

   // 请求的步骤
   private void requestStep() {
      Trace.d("MainActivity lanch_time2:" + (System.currentTimeMillis()
          - AULiveApplication.lanch_time));

      RequestInformation request = null;
      // 测试是否为注册，以及作相应跳转
      config = MobileConfig.getMobileConfig(MainActivity.this);
      try {
         String domain =
             SharedPreferenceTool.getInstance().getString(URL_DOMAIN_key);
         Trace.d("****" + domain);
         StringBuilder sb = new StringBuilder("http://mqphone."
             + domain
             + "/index/mqsync?system_name="
             + config.getOS()
             + "&system_version="
             + config.getMobileOsVersion()
             + "&platform="
             + URLEncoder.encode(config.getMobileModel(), "utf-8")
             + "&carrier="
             + URLEncoder.encode(config.getSimOperatorName(), "utf-8")
             + "&udid="
             + config.getIemi()
             + "&app_version="
             + config.getPkgVerCode()
             + "&app_channel="
             + config.getAppMetaData(MainActivity.this, "UMENG_CHANNEL")
             + "&app=AULive");
         // Trace.d("app_channel:" + config.getCurrMarketName());
         //
         // Trace.d("url:" + sb.toString());
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      // callback:{"stat":200,"msg":"","userinfo":{"uid":"19456778","account":"qwerty","sex":1,"nickname":"","face":"http:\/\/img.yuanphone.com\/face\/10\/19456778.jpg","sip":"42.121.85.232","sport":"8008","device_token":""},"step":5,"is_push":1,"alipay":0,"is_report":1}

      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               Trace.d("****网络请求" + UrlHelper.SERVER_URL);
               Trace.d("MainActivity lanch_time3:" + (System.currentTimeMillis()
                   - AULiveApplication.lanch_time));

//               AULiveApplication.step = callback.getStep();

               if (callback.getUserinfo() != null) {
                  LoginUserEntity loginUserEntity = callback.getUserinfo();
                  // 把token保存到userinfo里，方便做缓存
                  loginUserEntity.setDevice_token(callback.getToken());

                  if (callback.getUplive_url() != null) {
                     loginUserEntity.setUpLiveUrl(callback.getUplive_url());
                  }

                  if (loginUserEntity != null
                      && loginUserEntity.getUid() != null
                      && !loginUserEntity.getUid().equals("")) {
                     AULiveApplication.setUserInfo(loginUserEntity);
                     Trace.d("MainActivity准备连接融信connect");
                     //连接融云
                     connect(callback.getUserinfo().getIm_token());
                  } else {
                     //信息为空
                     AULiveApplication.setUserInfo(loginUserEntity);

                     //直播前必须登录
                     if (!Utils.isLogin(MainActivity.this)) {
                        MainActivity.this.finish();
                        return;
                     }
                  }
               } else {
                  //信息为空
                  LoginUserEntity loginUserEntity = new LoginUserEntity();
                  loginUserEntity.setDevice_token("");
                  AULiveApplication.setUserInfo(loginUserEntity);

                  //直播前必须登录
                  if (!Utils.isLogin(MainActivity.this)) {
                     MainActivity.this.finish();
                     return;
                  }
               }
               //存userSig
               AULiveApplication.setMyselfUserInfo(callback.getUserinfo().getUid(),
                   callback.getUserinfo().getNickname(), callback.getUserinfo().getFace(),
                   callback.getUserSig(),callback.getUserinfo().getMsg_tip(),callback.getUserinfo().getPrivate_chat_status());
               try {
                  if (RongIM.getInstance() != null) {
                     //RongClod图片
                     RongIM.getInstance()
                         .setCurrentUserInfo(
                             new io.rong.imlib.model.UserInfo(callback.getUserinfo().getUid(),
                                 callback.getUserinfo().getNickname(),
                                 Uri.parse(callback.getUserinfo().getFace())));
                     RongIM.getInstance().setMessageAttachedUserInfo(true);
                  }
               } catch (Exception e) {
               }
               //缓存更新APK信息
               UpdateApkEntity updateApkEntity = callback.upnew;
               ///////////////////////////////////////////////////////////
               //缓存更新补丁
               UpdateApkEntity updatePatchEntity = callback.fixpatch;
               //updatePatchEntity.updateUrl="http://www.hrbhzkj.com/i/down/au_test_p2.patch";
               //跟新patch补丁
               //UpdateApkEntity updatePatchEntity =
               //    (UpdateApkEntity) getIntent().getSerializableExtra(MainActivity.UPDATE_PATCH_KEY);

               try {
                  if (updatePatchEntity != null) {
                     int versionCode = MainActivity.this.getPackageManager()
                         .getPackageInfo(MainActivity.this.getPackageName(), 0).versionCode;
                     if (updatePatchEntity.versonCode == versionCode) {
                        PatchDownloadHelper.downloadPatchFile(updatePatchEntity.updateUrl,
                            MainActivity.this);
                     } else {
                        //Toast.makeText(mContext, mContext.getString(R.string.app_no_new_update), Toast.LENGTH_SHORT).show();
                     }
                  }
               } catch (PackageManager.NameNotFoundException e) {
                  e.printStackTrace();
               }

               Trace.d("****" + "从服务器下载敏感词库");
               doFilterWordsDownload(callback.filterurl);
               ///////////////////////////////////////////////////////////
               System.out.println("url" + callback.filterurl);
               jumpHome(AULiveApplication.step + "", updateApkEntity, updatePatchEntity);

               // // 测试
               // Intent intent1 = new Intent(Car1Animation.this,
               // OldLoginActivity.class);
               // intent1.putExtra("first_time", true);
               // startActivity(intent1);
               // Car1Animation.this.finish();

               // uploadNetModel();
            } else {
               // 当返回不是200时，清空cookie
               AULiveApplication.removeAllCookie();
               SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.COOKIE_KEY, "");

               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            // 请求一次新域名
            Trace.d("****" + "访问失败");
            if (!alreadyRequest) {
               requestNewDomain();
            } else {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               //第二次失败返回默认的域名
               SharedPreferenceTool.getInstance()
                   .saveString(URL_DOMAIN_key, default_URL_DOMAIN_url);
            }
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }

   /**
    * 下载敏感词库
    */
   public void doFilterWordsDownload(String url) {
      //BadWordDownloadHelper badWordDownloadHelper=new BadWordDownloadHelper();
      BadWordDownloadHelper.downloadTxtFile(url, MainActivity.this);
   }

   /**
    * 获取新域名http://120.55.120.128/?pf=au
    */
//           118.89.19.234
   private void requestNewDomain() {
      //测试服务器 http://43.225.157.206/?pf=meimei
      // 已经请求过一次
      alreadyRequest = true;
      RequestInformation request = new RequestInformation("http://43.225.157.206/?pf=meimei",
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new StringCallback() {
         @Override public void onCallback(String callback) {
            String trim = callback.trim();
            SharedPreferenceTool.getInstance().saveString(URL_DOMAIN_key, trim);
            Trace.d("****请求到的新域名" + UrlHelper.SERVER_URL);
            // 再执行一次请求
            requestStep();
         }

         @Override public void onFailure(AppException e) {

         }
      });
      request.execute();
   }

   // 按已完成的步骤跳转
   private void jumpHome(final String step1, final UpdateApkEntity updateApkEntity1,
       final UpdateApkEntity updatePatchEntity1) {
      Trace.d("MainActivity lanch_time4:" + (System.currentTimeMillis()
          - AULiveApplication.lanch_time));

      getAttenList(AULiveApplication.getUserInfo().getUid());
      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
         public void run() {
            Looper.prepare();

            // // 百度推送
            // PushManager.startWork(getApplicationContext(),
            // PushConstants.LOGIN_TYPE_API_KEY,
            // Utils.getMetaValue(Car1Animation.this, "api_key"));
            // Trace.d("baidu api_key:"
            // + Utils.getMetaValue(Car1Animation.this, "api_key"));

            // 定位
            // LoadAddress.getInstance().getAddress();

            //startActivity(new Intent(Car1Animation.this,
            //		AULiveHomeActivity.class));

            Intent intent = new Intent(MainActivity.this, AULiveHomeActivity.class);
            intent.putExtra(UPDATE_APK_KEY, updateApkEntity1);
            intent.putExtra(UPDATE_PATCH_KEY, updatePatchEntity1);
            startActivity(intent);

            // overridePendingTransition(R.anim.abc_fade_in,
            // R.anim.abc_fade_out);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            MainActivity.this.finish();
            Trace.d("MainActivity lanch_time5:" + (System.currentTimeMillis()
                - AULiveApplication.lanch_time));
         }
      };
      timer.schedule(task, 300);
   }

   @Override public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_main, menu);
      return true;
   }

   public static String UPDATE_APK_KEY = "UPDATE_APK_KEY";
   public static String UPDATE_PATCH_KEY = "UPDATE_PATCH_KEY";
   //关注列表
   public static ArrayList<String> atten_uids = new ArrayList<String>();

   //获取自己的关注列表
   public static void getAttenList(String uid) {
      if (uid == null || uid.equals("")) {
         return;
      }

      RequestInformation request =
          new RequestInformation(UrlHelper.ROOM_ATTEN_UID_LIST + "?liveuid=" + uid,
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UidListEntity>() {

         @Override public void onCallback(UidListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               atten_uids.clear();
               String[] uidss = callback.getUids().split(",");
               for (String uid : uidss) {
                  atten_uids.add(uid);
               }
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(UidListEntity.class));
      request.execute();
   }

   /**
    * 建立与融云服务器的连接
    */
   private void connect(String token) {

      if (getApplicationInfo().packageName.equals(
          AULiveApplication.getCurProcessName(getApplicationContext()))) {

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

   public static void printDeviceInf() {
      StringBuilder sb = new StringBuilder();
      sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
      sb.append("BOARD ").append(android.os.Build.BOARD).append("\n");
      sb.append("BOOTLOADER ").append(android.os.Build.BOOTLOADER).append("\n");
      sb.append("BRAND ").append(android.os.Build.BRAND).append("\n");
      sb.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append("\n");
      sb.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append("\n");
      sb.append("DEVICE ").append(android.os.Build.DEVICE).append("\n");
      sb.append("DISPLAY ").append(android.os.Build.DISPLAY).append("\n");
      sb.append("FINGERPRINT ").append(android.os.Build.FINGERPRINT).append("\n");
      sb.append("HARDWARE ").append(android.os.Build.HARDWARE).append("\n");
      sb.append("HOST ").append(android.os.Build.HOST).append("\n");
      sb.append("ID ").append(android.os.Build.ID).append("\n");
      sb.append("MANUFACTURER ").append(android.os.Build.MANUFACTURER).append("\n");
      sb.append("MODEL ").append(android.os.Build.MODEL).append("\n");
      sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
      sb.append("RADIO ").append(android.os.Build.RADIO).append("\n");
      sb.append("SERIAL ").append(android.os.Build.SERIAL).append("\n");
      sb.append("TAGS ").append(android.os.Build.TAGS).append("\n");
      sb.append("TIME ").append(android.os.Build.TIME).append("\n");
      sb.append("TYPE ").append(android.os.Build.TYPE).append("\n");
      sb.append("USER ").append(android.os.Build.USER).append("\n");
      Trace.d(sb.toString());
   }
}
