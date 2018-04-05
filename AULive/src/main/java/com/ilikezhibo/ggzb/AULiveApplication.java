/*
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ilikezhibo.ggzb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.GlobalRequestFilter;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.Constants;
import com.jack.utils.CrashHandler;
import com.jack.utils.FileUtil;
import com.jack.utils.JsonParser;
import com.jack.utils.NetWorkUtils;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.TextUtil;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.RongReceiveEvent;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeChatRoomMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeMsgQueueMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeRCTMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.GiftChatRoomMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.SystemChatRoomMessage;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.ChatMsgEntity;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatActivity;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatListActivity;
import com.ilikezhibo.ggzb.avsdk.chat.event.UpDateUnReadEvent;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.GiftProvider;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.SoloProvider;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.photos.photobrowser.PicBrowseActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.apache.http.HttpResponse;
import tinker.android.Log.MyLogImp;
import tinker.android.util.SampleApplicationContext;
import tinker.android.util.TinkerManager;


@SuppressWarnings("unused") @DefaultLifeCycle(application = "com.ilikezhibo.ggzb.DelegateApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)

public class AULiveApplication extends DefaultApplicationLike {
   private static final String TAG = "Tinker.SampleApplicationLike";

   public AULiveApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                            long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
                            Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
      super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
              applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
   }

   /**
    * install multiDex before install tinker
    * so we don't need to put the tinker lib classes in the main dex
    */
   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @Override public void onBaseContextAttached(
           Context base) {
      super.onBaseContextAttached(base);
      //you must install multiDex whatever tinker is installed!
      MultiDex.install(base);

      SampleApplicationContext.application = getApplication();
      SampleApplicationContext.context = getApplication().getApplicationContext();
      TinkerManager.setTinkerApplicationLike(this);
      TinkerManager.initFastCrashProtect();
      //should set before tinker is installed
      TinkerManager.setUpgradeRetryEnable(true);

      //optional set logIml, or you can use default debug log
      TinkerInstaller.setLogIml(new MyLogImp());

      //installTinker after load multiDex
      //or you can put com.tencent.tinker.** to main dex
      TinkerManager.installTinker(this);
      TinkerManager.getTinkerApplicationLike();
   }

   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
   public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
      getApplication().registerActivityLifecycleCallbacks(callback);
   }

   ////////////////////////////////////
   public static Context mContext;
   private static DisplayImageOptions options;

   public static int step = -1;
   private static LoginUserEntity mCurrentUserInfo;
   public static String currLiveUid;// 当前观看直播id
   public static String oneToOneUid;// 当前观看直播id

   private String citycode;

   /** msg state start */
   public static boolean isCurrConversaUI = true;// 是否当前会话
   public static boolean isRequestConverationNetData = false;// 是否请求会话的网络数据
   public static String currChatMid;// 当前聊天的mid
   public static String friendUid;// 对方id
   /** msg state end */

   public static RongIM.UserInfoProvider userInfoProvider;
   public static AULiveHomeActivity mAuLiveHomeActivity;

   public static boolean is_on_home_context = true;
   public static AvActivity mAvActivity = null;


   //私聊的上下文
   public static PrivateChatActivity mPrivateChatActivity = null;

   //剪切版广告计数
   public static int adv_count = 0;

   public static long lanch_time = 0;

   @Override public void onCreate() {
      Trace.d("AULiveApplication onCreate");
      lanch_time = System.currentTimeMillis();
      super.onCreate();
      mContext = getApplication().getApplicationContext();
      // 推送初始化
      // Android V4.5.1不用初始化
      // FrontiaApplication.initFrontiaApplication(mContext);

      initImageLoader(getApplication().getApplicationContext());
      initGlobalConfig();

      CrashHandler crashHandler = CrashHandler.getInstance();
      crashHandler.init(getApplication());

      //直播相关
      mSelfUserInfo = null;

      //融云聊天相关
      /**
       * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
       * io.rong.push 为融云 push 进程名称，不可修改。
       */
      try {
         if (getApplication().getApplicationInfo().packageName.equals(
                 getCurProcessName(getApplication().getApplicationContext())) || "io.rong.push".equals(
                 getCurProcessName(getApplication().getApplicationContext()))) {

            Trace.d("RongIM.init(this);");
            if (mAuLiveHomeActivity != null) {
               mAuLiveHomeActivity.finish();
               mAuLiveHomeActivity = null;
            }
            if (mAvActivity != null) {
               mAvActivity.finish();
               mAvActivity = null;
            }
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(getApplication());
            userInfoProvider = new RongIM.UserInfoProvider() {
               @Override public io.rong.imlib.model.UserInfo getUserInfo(String userId) {
                  return getHomeInfo(userId);
               }
            };
            RongIM.setUserInfoProvider(userInfoProvider, true);

            //扩展功能自定义
            InputProvider.ExtendProvider[] provider = {
                    new ImageInputProvider(RongContext.getInstance()),//图片
                    //new CameraInputProvider(RongContext.getInstance()),//相机
                    new LocationInputProvider(RongContext.getInstance()),//地理位置
                    new GiftProvider(RongContext.getInstance()),//自定义礼物
                    new SoloProvider(RongContext.getInstance())//1v1视频
            };

            RongIM.getInstance()
                    .resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);

            RongIM.setLocationProvider(new RongIM.LocationProvider() {

               @Override
               public void onStartLocation(Context context, LocationCallback locationCallback) {
                  //在这里打开你的地图页面,保存 locationCallback 对象。
                  Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?center="
                          + lon
                          + ","
                          + lat
                          + "&width=400&height=300&zoom=11&markers="
                          + lon
                          + ","
                          + lat
                          + "&markerStyles=Point,A&copyright=1");
                  Trace.d("onStartLocation uri:" + uri.toString());
                  LocationMessage locationMessage = LocationMessage.obtain(lat, lon, address, uri);
                  //如果地图地位成功，那么调用
                  locationCallback.onSuccess(locationMessage);
                  //如果地图地位失败，那么调用
                  locationCallback.onFailure("定位失败!");
               }
            });
            /**
             * 设置会话界面操作的监听器。
             */
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
            RongIM.getInstance()
                    .setOnReceiveUnreadCountChangedListener(new MyReceiveUnreadCountChangedListener(),
                            Conversation.ConversationType.PRIVATE);
            //自定义消息注册
            RongIM.registerMessageType(SystemChatRoomMessage.class);
            RongIM.registerMessageType(GiftChatRoomMessage.class);
            RongIM.registerMessageType(CustomizeChatRoomMessage.class);
            RongIM.registerMessageType(CustomizeMsgQueueMessage.class);
            RongIM.registerMessageType(CustomizeRCTMessage.class);
            RongIM.getInstance()
                    .getRongIMClient()
                    .setConnectionStatusListener(new MYConnectionStatusListener());

            RongIM.getInstance().setOnReceiveMessageListener(new MyReceiveMessageListener());
         }
      } catch (Exception e) {
      }
      //只有当前线程
      if (getApplication().getApplicationInfo().packageName.equals(
              getCurProcessName(getApplication().getApplicationContext()))) {
         try {
            // // 百度地图初始化
            // SDKInitializer.initialize(this);
            // 百度定位
            init();
         } catch (Exception e) {
            // 什么不做，防挂
            e.printStackTrace();
         }

         //获取本地的ip及调用接口获取最近的服务器ip
         //getKSYHostIP();
      }
      //场景类型设置
      MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
      //bugly
      CrashReport.initCrashReport(getApplication(), "8bcb2fbb4e", false);
   }

   /**
    * 获得当前进程的名字
    *
    * @return 进程号
    */
   public static String getCurProcessName(Context context) {

      int pid = android.os.Process.myPid();

      ActivityManager activityManager =
              (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

      for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {

         if (appProcess.pid == pid) {
            return appProcess.processName;
         }
      }
      return null;
   }

   public static DisplayImageOptions getGlobalImgOptions() {
      return options;
   }

   public static DisplayImageOptions getNoDefaultImgOptions() {
      return new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
   }

   /** 清空Cookie. */
   public static void removeAllCookie() {
      CookieSyncManager.createInstance(mContext);
      CookieManager cookieManager = CookieManager.getInstance();
      cookieManager.removeAllCookie();
      CookieSyncManager.getInstance().sync();
   }

   // 初始化cookie
   private void initGlobalConfig() {
      RequestInformation.mGlobalRequestFilter = new GlobalRequestFilter() {

         @Override public HashMap<String, String> filterHeader() {
            String mCookie =
                    SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.COOKIE_KEY, "");
            if (mCookie != null) {
               HashMap<String, String> header = new HashMap<String, String>();
               header.put(Constants.KEY_COOKIE_INFO, mCookie);
               return header;
            }
            return null;
         }
      };
   }

   private static void initImageLoader(Context context) {
      File cacheDir = new File(FileUtil.getImageFolder());
      ImageLoaderConfiguration config =
              new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                      .denyCacheImageMultipleSizesInMemory()
                      .memoryCache(new WeakMemoryCache())
                      // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
                      // 1024))
                      .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                      .tasksProcessingOrder(QueueProcessingType.LIFO)
                      .build();

      ImageLoader.getInstance().init(config);
      options = new DisplayImageOptions.Builder().showStubImage(R.drawable.loading)
              .showImageForEmptyUri(R.drawable.loading)
              .showImageOnFail(R.drawable.loading)
              .cacheOnDisk(true)
              .cacheInMemory(true)
              //    // 设置图片以如何的编码方式显示
              //.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
              //    // 设置图片的解码类型
              .bitmapConfig(Bitmap.Config.RGB_565)
              .build();
   }

   /**
    * 登录成功后 设置的用户信息
    *
    * @param userinfo 用户信息
    */
   public static void setUserInfo(LoginUserEntity userinfo) {
      mCurrentUserInfo = userinfo;
      if (userinfo != null) {
         SharedPreferenceTool.getInstance()
                 .saveString(Constants.KEY_CURRENT_USERINFO, JsonParser.serializeToJson(userinfo));
      } else {
         SharedPreferenceTool.getInstance().saveString(Constants.KEY_CURRENT_USERINFO, "");
      }
   }

   public static LoginUserEntity getUserInfo() {
      if (mCurrentUserInfo == null) {
         String userinfo =
                 SharedPreferenceTool.getInstance().getString(Constants.KEY_CURRENT_USERINFO, "");
         if (TextUtil.isValidate(userinfo)) {
            mCurrentUserInfo = JsonParser.deserializeByJson(userinfo, LoginUserEntity.class);
         }
      }
      return mCurrentUserInfo;
   }

   // 判断是否已经登陆
   public static boolean hasLogin() {
      if (getUserInfo() == null) {
         return false;
      } else {
         return true;
      }
   }

   /////////////////////////////////////////////
   // 定位相关
   private LocationClient mLocationClient;
   private MyLocationListener mMyLocationListener;

   private double lat;
   private double lon;
   private String address;

   private String province;
   private String city;
   private String district;
   private float radius;

   private void init() {
      // 定位相关
      mLocationClient = new LocationClient(getApplication());
      String baidu_accessKey = mLocationClient.getAccessKey();
      // Trace.d("baidu_accessKey:" + baidu_accessKey);

      mMyLocationListener = new MyLocationListener();
      mLocationClient.registerLocationListener(mMyLocationListener);
      // mLocationClient.requestLocation();
      LocationClientOption option = new LocationClientOption();
      option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
      option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
      option.setOpenGps(true);
      option.setProdName("接私活");
      // 返回的定位结果包含地址信息
      option.setAddrType("all");
      // 原为：bd09ll
      option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
      option.setScanSpan(1000 * 60 * 60);
      // option.disableCache(true);//禁止启用缓存定位
      mLocationClient.setLocOption(option);
      loadPosition();
      //
      // mLocationClient = new LocationClient(CityLoveApplication.mContext);
      // mMyLocationListener = new MyLocationListener();
      // mLocationClient.registerLocationListener(mMyLocationListener);
      //
      // mGeofenceClient = new GeofenceClient(CityLoveApplication.mContext);
      // mNotifyLister = new NotifyLister();
      // mVibrator
      // =(Vibrator)CityLoveApplication.mContext.getSystemService(Service.VIBRATOR_SERVICE);
   }

   /** 实现实位回调监听 */
   private class MyLocationListener implements BDLocationListener {

      @Override public void onReceiveLocation(BDLocation location) {

         Trace.d("onReceiveLocation");
         // Receive Location
         StringBuffer sb = new StringBuffer(256);
         sb.append("time : ");
         sb.append(location.getTime());
         sb.append("\nerror code : ");
         sb.append(location.getLocType());
         sb.append("\nlatitude : ");
         sb.append(location.getLatitude());
         sb.append("\nlontitude : ");
         sb.append(location.getLongitude());
         sb.append("\nradius : ");
         sb.append(location.getRadius());
         String strLog =
                 String.format("您当前的位置:\r\n" + "纬度:%f\r\n" + "经度:%f", location.getLongitude(),
                         location.getLatitude());
         strLog =
                 strLog + " 省：" + location.getProvince() + " 市：" + location.getCity() + " 区：" + location
                         .getDistrict() + " 街道:" + location.getAddrStr();
         address = location.getAddrStr();
         province = location.getProvince();
         city = location.getCity();
         district = location.getDistrict();
         setCitycode(location.getCityCode());

         setRadius(location.getRadius());

         if (address != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();

            // //测试
            // lat = 31.236136;
            // lon = 121.46678;
         }

         if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\ndirection : ");
            sb.append(location.getDirection());
         } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            Trace.d("address:" + location.getAddrStr());
            // 运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
         }
         Trace.d("地址信息：" + sb.toString());
         Trace.d("strLog:" + strLog);
      }
   }

   public void loadPosition() {
      Trace.d("mLocationClient.start()");

      mLocationClient.start();
      mLocationClient.requestLocation();
   }

   public void refleshPosition() {
      mLocationClient.requestLocation();
   }

   /** 停止获取位置 */
   public void stopLoadPosition() {
      mLocationClient.stop();
   }

   /** 经度 */
   public double getLongitude() {
      return lon;
   }

   /** 纬度 */
   public double getLatitude() {
      return lat;
   }

   /** 地址 */
   public String getAddress() {
      return address;
   }

   public String getProvince() {
      return province;
   }

   public void setProvince(String province) {
      this.province = province;
   }

   public String getDistrict() {
      return district;
   }

   public void setDistrict(String district) {
      this.district = district;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public float getRadius() {
      return radius;
   }

   public void setRadius(float radius) {
      this.radius = radius;
   }

   public String getCitycode() {
      return citycode;
   }

   public void setCitycode(String citycode) {
      this.citycode = citycode;
   }

   public static String getEncodeUrl(String url) {
      try {
         url = URLEncoder.encode(url, "utf-8");
      } catch (UnsupportedEncodingException e1) {
         e1.printStackTrace();
      }
      return url;
   }

   ////////////////////////////////////////////////////
   //直播相关
   private static UserInfo mSelfUserInfo;

   //登录后缓存的user_sig等数据
   public static void setMyselfUserInfo(String uid, String user_name, String face,
                                        String user_sig ,String msg_tip,String private_chat_status) {
      //做直播userInfo的数据转换
      mSelfUserInfo = new UserInfo(uid, user_name, face, msg_tip,private_chat_status);
      mSelfUserInfo.setUsersig(user_sig);

      if (mSelfUserInfo != null) {
         SharedPreferenceTool.getInstance()
                 .saveString(Constants.KEY_AV_USERINFO, JsonParser.serializeToJson(mSelfUserInfo));
      } else {
         SharedPreferenceTool.getInstance().saveString(Constants.KEY_AV_USERINFO, "");
      }
   }

   public static UserInfo getMyselfUserInfo() {
      if (mSelfUserInfo == null) {
         String userinfo =
                 SharedPreferenceTool.getInstance().getString(Constants.KEY_AV_USERINFO, "");
         if (TextUtil.isValidate(userinfo)) {
            mSelfUserInfo = JsonParser.deserializeByJson(userinfo, UserInfo.class);
         }
      }
      return mSelfUserInfo;
   }

   //同步调用,no callback
   private io.rong.imlib.model.UserInfo getHomeInfo(String userId) {
      String result = null;
      BufferedReader reader = null;
      Trace.d("AULiveApplication getHomeInfo userId:" + userId);
      String uid = userId;
      if (uid == null || uid.equals("")) {
         Utils.showMessage("uid为空");
         return null;
      }
      try {
         StringBuilder sb = new StringBuilder(
                 UrlHelper.user_home_Url + "/userinfo" + "?uid=" + uid + "&pf=android");
         RequestInformation request =
                 new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

         HttpResponse response = com.jack.lib.net.HttpUtil.getWithOutCallBack(request);

         reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
         StringBuffer strBuffer = new StringBuffer("");
         String line = null;
         while ((line = reader.readLine()) != null) {
            strBuffer.append(line);
         }

         result = strBuffer.toString();

         Trace.d("AULiveApplication getHomeInfo result:" + result);
         com.ilikezhibo.ggzb.entity.UserInfoList callback =
                 JsonParser.deserializeByJson(result, com.ilikezhibo.ggzb.entity.UserInfoList.class);
         ArrayList<LoginUserEntity> userinfo = callback.getUserinfo();

         io.rong.imlib.model.UserInfo info =
                 new io.rong.imlib.model.UserInfo(userinfo.get(0).getUid(),
                         userinfo.get(0).getNickname(), Uri.parse(userinfo.get(0).getFace()));
         return info;
      } catch (Exception e) {
         Trace.e(e.toString());
      } finally {
         if (reader != null) {
            try {
               reader.close();
               reader = null;
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return null;
   }

   private class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

      /**
       * 当点击用户头像后执行。
       *
       * @param context 上下文。
       * @param conversationType 会话类型。
       * @param userInfo 被点击的用户的信息。
       * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
       */
      @Override public boolean onUserPortraitClick(Context context,
                                                   Conversation.ConversationType conversationType, io.rong.imlib.model.UserInfo userInfo) {
         if (mAvActivity != null
                 && mAuLiveHomeActivity == null
                 && getMyselfUserInfo().isCreater()) {
            Utils.showCroutonText(mAvActivity, "主播不能离开直播界面");
            return false;
         }
         if (userInfo != null && userInfo.getUserId() != null) {
            Intent homepage_Intent = new Intent(context, HomePageActivity.class);
            homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, userInfo.getUserId());
            context.startActivity(homepage_Intent);
         }
         return true;
      }

      /**
       * 当长按用户头像后执行。
       *
       * @param context 上下文。
       * @param conversationType 会话类型。
       * @param userInfo 被点击的用户的信息。
       * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
       */
      @Override public boolean onUserPortraitLongClick(Context context,
                                                       Conversation.ConversationType conversationType, io.rong.imlib.model.UserInfo userInfo) {
         return false;
      }

      /**
       * 当点击消息时执行。
       *
       * @param context 上下文。
       * @param view 触发点击的 View。
       * @param message 被点击的消息的实体信息。
       * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
       */
      @Override public boolean onMessageClick(Context context, View view, Message message) {
         MessageContent messageContent = message.getContent();

         //
         if (mAvActivity != null
                 && mAuLiveHomeActivity == null
                 && getMyselfUserInfo().isCreater()
                 && !(messageContent instanceof VoiceMessage)) {
            Utils.showCroutonText(mAvActivity, "主播在直播,离开不了");
            return true;
         }
         if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
         } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            if (imageMessage == null || imageMessage.getRemoteUri() == null) {
               return false;
            }
            String url = imageMessage.getRemoteUri().toString();
            if (url == null || url.equals("")) {
               return false;
            }
            String[] urls = { url };
            Intent intent = new Intent(context, PicBrowseActivity.class);
            intent.putExtra(PicBrowseActivity.INTENT_BROWSE_POS_KEY, 0);
            intent.putExtra(PicBrowseActivity.INTENT_BROWSE_LST_KEY, urls);
            context.startActivity(intent);
            return true;
         } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            return false;
         } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            //返回true,即不处理
            return true;
         }
         if (messageContent instanceof LocationMessage) {//位置消息
            LocationMessage locationMessage = (LocationMessage) messageContent;

            //String url = locationMessage.getImgUri().toString();
            //if (url == null || url.equals("")) {
            //   return false;
            //}
            //String[] urls = { url };
            //Intent intent = new Intent(context, PicBrowseActivity.class);
            //intent.putExtra(PicBrowseActivity.INTENT_BROWSE_POS_KEY, 0);
            //intent.putExtra(PicBrowseActivity.INTENT_BROWSE_LST_KEY, urls);
            //context.startActivity(intent);

            String url_s = "http://api.map.baidu.com/marker?location="
                    + locationMessage.getLat()
                    + ","
                    + locationMessage.getLng()
                    + "&title="
                    + locationMessage.getPoi()
                    + "&content="
                    + locationMessage.getPoi()
                    + "&output=html";
            Intent intent = new Intent(context, WebViewActivity.class);

            intent.putExtra(WebViewActivity.input_url, url_s);
            intent.putExtra(WebViewActivity.back_home_key, false);
            intent.putExtra(WebViewActivity.actity_name, "" + locationMessage.getPoi());
            context.startActivity(intent);
            return false;
         } else {
            Trace.d("onSent-其他消息，自己来判断处理");
         }

         return false;
      }

      /**
       * 当长按消息时执行。
       *
       * @param context 上下文。
       * @param view 触发点击的 View。
       * @param message 被长按的消息的实体信息。
       * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
       */
      @Override public boolean onMessageLongClick(Context context, View view, Message message) {
         return false;
      }

      /**
       * 当点击链接消息时执行。
       *
       * @param context 上下文。
       * @param link 被点击的链接。
       * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
       */
      @Override public boolean onMessageLinkClick(Context context, String link) {
         return true;
      }
   }

   /**
    * 接收未读消息的监听器。
    */
   private class MyReceiveUnreadCountChangedListener
           implements RongIM.OnReceiveUnreadCountChangedListener {

      /**
       * @param count 未读消息数。
       */
      @Override public void onMessageIncreased(int count) {
         EventBus.getDefault().post(new UpDateUnReadEvent());
         if (PrivateChatListActivity.is_on_chat_context == true) {
            return;
         }
         if (count > 0) {
            if (is_on_home_context && mAuLiveHomeActivity != null && mAvActivity == null) {
               Utils.showCroutonText(mAuLiveHomeActivity, "您有" + count + "条信息未阅读");
            } else if (!is_on_home_context && mAvActivity != null && mAuLiveHomeActivity == null) {
               Utils.showCroutonText(mAvActivity, "您有" + count + "条信息未阅读");
            } else {
               //Utils.showMessage("您有" + count + "条信息未阅读");
            }
         }
      }
   }

   private class MYConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
      @Override public void onChanged(ConnectionStatus connectionStatus) {
         Trace.d("MYConnectionStatusListener onChanged:" + connectionStatus.getMessage());
         if (connectionStatus == ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
            if (mAuLiveHomeActivity != null) {
               showSuccPrompt(mAuLiveHomeActivity, "你的账号在别处登录");
            } else if (mAvActivity != null) {
               showSuccPrompt(mAvActivity, "你的账号在别处登录");
            } else {
               com.ilikezhibo.ggzb.tool.Utils.showMessageLong("你的账号在别处登录");
               doQuit();
            }
         }
      }
   }

   private CustomDialog promptDialog;

   private void showSuccPrompt(final Context context, final String msg) {
      Activity mActivity = (Activity) context;
      mActivity.runOnUiThread(new Runnable() {
         @Override public void run() {

            if (promptDialog == null) {
               promptDialog = new CustomDialog(context, new CustomDialogListener() {

                  @Override public void onDialogClosed(int closeType) {
                     switch (closeType) {
                        case CustomDialogListener.BUTTON_POSITIVE:
                           doQuit();
                           break;
                     }
                  }
               });
            }
            promptDialog.setCustomMessage(msg);
            promptDialog.setCancelable(false);
            promptDialog.setType(CustomDialog.SINGLE_BTN);

            if (null != promptDialog) {
               promptDialog.show();
            }
         }
      });
   }

   private void doQuit() {
      RequestInformation request =
              new RequestInformation(UrlHelper.EXIT_APP_URL, RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {

               //断开rongcloud连接
               RongIM.getInstance().getRongIMClient().logout();

               AULiveApplication.removeAllCookie();
               SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.COOKIE_KEY, "");

               AULiveApplication.setUserInfo(new LoginUserEntity());

               Intent intent = new Intent(AULiveApplication.mContext, MainActivity.class);
               PendingIntent restartIntent =
                       PendingIntent.getActivity(AULiveApplication.mContext, 0, intent,
                               PendingIntent.FLAG_UPDATE_CURRENT);

               Intent login_intent = new Intent(AULiveApplication.mContext, LoginActivity.class);
               login_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               AULiveApplication.mContext.startActivity(login_intent);
            } else {
               com.ilikezhibo.ggzb.tool.Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            com.ilikezhibo.ggzb.tool.Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private NotificationManager mNotifyManager;
   private NotificationCompat.Builder mBuilder;
   public static boolean is_on_background = false;

   private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

      /**
       * 收到消息的处理。
       *
       * @param message 收到的消息实体。
       * @param left 剩余未拉取消息数目。
       * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
       */
      @Override public boolean onReceived(final Message message, int left) {
         Trace.d("onReceived Application Notification Tips:" + message.getConversationType());

         //一对一处理
         if (message.getContent() instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message.getContent();
            String extra = textMessage.getExtra();
            if (extra != null) {
               long sent_time = message.getSentTime();
               int second = (int) (System.currentTimeMillis() - sent_time) / 1000;
               Trace.d("Application OneToOneHelper.onetoone_invite sent_time:"
                       + sent_time
                       + "second:"
                       + second);
               //在房间处理
               //else if (extra.equals("1v1:reject")) {
               //
               //}
               //else if (extra.equals("1v1:hangup")) {
               //
               //} else if (extra.equals("1v1:award")) {
               //
               //} else if (extra.equals("1v1:noresponse")) {
               //
               //}

            }
         }

         //发送到需要的接收的地方
         EventBus.getDefault().post(new RongReceiveEvent(message, left));

         //处理谁上线的推送消息
         if (message.getConversationType() == Conversation.ConversationType.SYSTEM
                 && message.getSenderUserId().equals("10000")) {
            if (message.getContent() instanceof CustomizeChatRoomMessage) {
               CustomizeChatRoomMessage push_ccrm = (CustomizeChatRoomMessage) message.getContent();
               String customText = push_ccrm.data;
               //与ChatMsgEntity 字段相同
               ChatMsgEntity chatMsgEntity =
                       JsonParser.deserializeByJson(customText, ChatMsgEntity.class);
               if (chatMsgEntity.type.equals("tips")) {
                  //PushNotificationMessage pushMsg =
                  //    PushNotificationMessage.obtain(chatMsgEntity.content,
                  //        Conversation.ConversationType.PRIVATE, chatMsgEntity.uid,
                  //        chatMsgEntity.nickname);
                  //PushNotificationManager.getInstance().onReceiveMessage(pushMsg, false);

                  mNotifyManager = (NotificationManager) getApplication().getSystemService(
                          Context.NOTIFICATION_SERVICE);
                  mBuilder = new NotificationCompat.Builder(AULiveApplication.mContext);

                  String appName =
                          getApplication().getString(getApplication().getApplicationInfo().labelRes);
                  int icon = getApplication().getApplicationInfo().icon;
                  mBuilder.setContentTitle(appName).setSmallIcon(icon);
                  mBuilder.setTicker(chatMsgEntity.content);
                  mBuilder.setContentText(chatMsgEntity.content);
                  //| Notification.DEFAULT_VIBRATE
                  mBuilder.setDefaults(Notification.DEFAULT_SOUND);

                  Intent homePageIntent =
                          new Intent(AULiveApplication.mContext, HomePageActivity.class);
                  homePageIntent.putExtra(HomePageActivity.HOMEPAGE_UID, chatMsgEntity.uid);
                  homePageIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                  homePageIntent.setFlags(
                          Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                  PendingIntent pendingIntent =
                          PendingIntent.getActivity(AULiveApplication.mContext, 0, homePageIntent,
                                  PendingIntent.FLAG_UPDATE_CURRENT);
                  mBuilder.setContentIntent(pendingIntent);
                  Notification noti = mBuilder.build();
                  noti.flags = Notification.FLAG_AUTO_CANCEL;
                  int notify_id = 0;
                  try {
                     notify_id = Integer.parseInt(chatMsgEntity.uid);
                  } catch (Exception e) {

                  }
                  mNotifyManager.notify((int) notify_id, noti);

                  return true;
               }
            }
            return false;
         }
         return false;
      }
   }

   ///////////////////////////////////////////
   //加载加速相关
   public static String ksy_ip;

   private void getKSYHostIP() {
      ksy_ip = null;
      String IPAddr = null;
      if (NetWorkUtils.isNetworkAvailable(AULiveApplication.mContext)) {
         IPAddr = NetWorkUtils.getIpAddress(AULiveApplication.mContext);
      }
      Trace.d("getKSYHostIP():" + IPAddr);
      if (IPAddr == null || IPAddr.equals("")) {
         ksy_ip = null;
      } else {
         String url = "http://120.92.234.96/d?dn=play2." + UrlHelper.URL_domain + "&ttl=1&ip=" + IPAddr;
         String cache_ksy_ip = SharedPreferenceTool.getInstance().getString(url, "");
         if (cache_ksy_ip == null || cache_ksy_ip.equals("")) {
            doGetKSYIP(url);
         } else {
            ksy_ip = cache_ksy_ip;
         }
      }
   }

   public static void doGetKSYIP(final String url) {
      //预拉流，加速
      RequestInformation request =
              new RequestInformation(url, RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new StringCallback() {
         @Override public void onFailure(AppException e) {
         }

         @Override public void onCallback(String callback) {
            if (callback == null) {
               return;
            } else {
               String[] ips = callback.split(";");
               if (ips.length > 0) {
                  Random random = new Random();

                  ksy_ip = ips[random.nextInt(ips.length)];
                  //ksy_ip = ips[0];
                  Trace.d("ips[0]:" + ksy_ip);
                  SharedPreferenceTool.getInstance().saveString(url, ksy_ip);
               }
            }
         }
      });
      request.execute();
   }

   @Override public void onTerminate() {
      // 程序终止的时候执行
      Trace.d("AULiveApplication onTerminate");
      super.onTerminate();
   }

   @Override public void onLowMemory() {
      // 低内存的时候执行
      Trace.d("AULiveApplication onLowMemory");
      super.onLowMemory();
   }

   @Override public void onTrimMemory(int level) {
      // 程序在内存清理的时候执行
      Trace.d("AULiveApplication onTrimMemory(int level):level=" + level);
      super.onTrimMemory(level);
      if (getApplication().getApplicationInfo().packageName.equals(
              getCurProcessName(getApplication().getApplicationContext()))) {
         //TRIM_MEMORY_COMPLETE这个监听的时候有时候监听不到，建议监听TRIM_MEMORY_MODERATE，在这个里面处理退出程序操作。
         Trace.d("AULiveApplication onTrimMemory packageName.equals(getCurProcessName ");
         if (level == 60) {
            Trace.d("AULiveApplication 处理退出程序操作");
            if (mAuLiveHomeActivity != null) {
               mAuLiveHomeActivity.finish();
               mAuLiveHomeActivity = null;
            }
            if (mAvActivity != null) {
               mAvActivity.finish();
               mAvActivity = null;
            }
         }
      }
   }
}

