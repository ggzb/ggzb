package com.jack.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
   public static final String AppID = "100646015";
   private static final String HEAD_FACE_URL = "http://app100646015.qzoneapp.com/";
   public static boolean isBackVideoHall = false;// 是否返回主播大厅
   public static boolean isUpdateUserInfo = false;// 是否更新用户信息
   public static boolean isInitVideoEnvorinment = false;// 是否已经初始化环境

   public static boolean isOnFindFragmentUI = false;// 是否在fragmentUI

   // push utils
   public static final String TAG = "PushDemoActivity";
   public static final String RESPONSE_METHOD = "method";
   public static final String RESPONSE_CONTENT = "content";
   public static final String RESPONSE_ERRCODE = "errcode";
   protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
   public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
   public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
   public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
   protected static final String EXTRA_ACCESS_TOKEN = "access_token";
   public static final String EXTRA_MESSAGE = "message";

   private static Toast toast;
   private static Toast customToast;
   private static Configuration croutonConfiguration;
   private static Style style;

   // 检查网络是否可用
   public static boolean checkNetworkIsAvailable() {
      ConnectivityManager manager =
          (ConnectivityManager) AULiveApplication.mContext.getSystemService(
              Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkinfo = manager.getActiveNetworkInfo();
      if (networkinfo == null || !networkinfo.isAvailable()) {// 当前网络不可用
         return false;
      } else {
         return true;
      }
   }

   // 获取AppKey
   public static String getMetaValue(Context context, String metaKey) {
      Bundle metaData = null;
      String apiKey = null;
      if (context == null || metaKey == null) {
         return null;
      }
      try {
         ApplicationInfo ai = context.getPackageManager()
             .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
         if (null != ai) {
            metaData = ai.metaData;
         }
         if (null != metaData) {
            apiKey = metaData.getString(metaKey);
         }
      } catch (NameNotFoundException e) {

      }
      return apiKey;
   }

   public static String trans(int id) {
      if (id == 0) {
         return null;
      }
      return AULiveApplication.mContext.getString(id);
   }

   public static String trans(int id, Object... args) {
      if (id == 0) {
         return null;
      }
      return AULiveApplication.mContext.getString(id, args);
   }

   public static String getImgUrl(String imgUrl) {
      if (imgUrl != null && imgUrl.contains("http")) {
         return imgUrl;
      } else {
         if (imgUrl.contains("face")) {
            return UrlHelper.IMAGE_ROOT_URL2 + imgUrl;
         } else {
            return UrlHelper.IMAGE_ROOT_URL + imgUrl;
         }
      }
   }

   public static int getStringResId(String name) {
      return AULiveApplication.mContext.getResources()
          .getIdentifier(name, "string", AULiveApplication.mContext.getPackageName());
   }

   public static String translateCustomerInformation(String description) {
      String pluralName = "SyncObjectKPI_" + description;
      Resources resources = AULiveApplication.mContext.getResources();
      int id = resources.getIdentifier(pluralName, "string", "com.coresuite.android");
      if (id != 0) {
         return trans(id);
      }
      return pluralName;
   }

   /** make sure all the given object params is null */
   public static boolean assertAllNULL(Object... objs) {
      for (Object obj : objs) {
         if (obj != null) {
            return false;
         }
      }
      return true;
   }

   public static void showMessage(String msg) {
      // Trace.d("msg:" + msg);

      //if (AULiveApplication.mAuLiveHomeActivity != null) {
      //   Crouton.showText(AULiveApplication.mAuLiveHomeActivity, msg, Style.INFO);
      //} else {
      if (toast == null) {
         toast = Toast.makeText(AULiveApplication.mContext, msg, Toast.LENGTH_SHORT);
      } else {
         toast.setText(msg);
      }
      toast.show();
      //}
   }

   public static void showCroutonText(Activity activity, String msg) {
      // Define configuration options
      Configuration croutonConfiguration = new Configuration.Builder().setDuration(2000).build();
      // Define custom styles for crouton
      Style style = new Style.Builder().setBackgroundColorValue(Color.parseColor("#54acea"))
              .setGravity(Gravity.CENTER_HORIZONTAL)
              .setConfiguration(croutonConfiguration)
              .setHeight(PixelDpHelper.dip2px(activity, 44))
              .setTextColorValue(Color.parseColor("#F5F5F5"))
              .build();
      Crouton.showText(activity, msg, style);
   }

   public static void showCenterMessage(String msg) {
      try {
         if (toast == null) {
            toast = Toast.makeText(AULiveApplication.mContext, msg, Toast.LENGTH_SHORT);
         } else {
            toast.setText(msg);
         }
         toast.setGravity(Gravity.CENTER, 0, 0);
         toast.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * 显示自定义背景的toast
    * @param toastRoot toast布局
    * @param isCenter 是否屏幕居中显示
    * @param msg toast内容
     */
   public static void setCustomViewToast(ViewGroup toastRoot, boolean isCenter, String msg) {
      try {
         if (!(toastRoot.getChildAt(0) instanceof TextView )) {
            throw new RuntimeException("第一个view不是Textview");
         }
         TextView text = (TextView) toastRoot.getChildAt(0);
            customToast = Toast.makeText(AULiveApplication.mContext, msg, Toast.LENGTH_SHORT);
            customToast.setView(toastRoot);
            text.setText(msg);
         if (isCenter) {
            customToast.setGravity(Gravity.CENTER, 0, 0);
         }
         customToast.show();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static int dip2px(Context paramContext, float paramFloat) {
      return (int) (paramFloat * paramContext.getResources().getDisplayMetrics().density);
   }

   public static String getResorcString(int resId) {
      return AULiveApplication.mContext.getResources().getString(resId);
   }

   /** 推出动画 */
   public static void exitAnimation(Activity activity) {
       activity.overridePendingTransition(R.anim.activity_close_enter,
       R.anim.activity_close_exit);
   }

   /** 清空缓存图片 */
   public static void clearCacheImg() {
      new Thread() {
         public void run() {
            FileUtil.deleteFileDir(FileUtil.ROOTPATH, true);
         }
      }.start();
   }

   /**
    * @param plainText 明文
    * @return 32位密文
    */
   public static String encryption(String plainText) {
      String re_md5 = new String();
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(plainText.getBytes());
         byte b[] = md.digest();

         int i;

         StringBuffer buf = new StringBuffer("");
         for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
               i += 256;
            }
            if (i < 16) {
               buf.append("0");
            }
            buf.append(Integer.toHexString(i));
         }

         re_md5 = buf.toString();
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      }
      return re_md5;
   }

   public static void updateApp(final Activity activity, final boolean home) {
      // 所有网络情况都可以更新
      UmengUpdateAgent.setUpdateOnlyWifi(false);
      // 检测版本更新
      UmengUpdateAgent.update(activity);

      UmengUpdateAgent.setUpdateAutoPopup(false);
      UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

         @Override public void onUpdateReturned(int arg0, UpdateResponse arg1) {
            switch (arg0) {
               case 0: // has update
                  UmengUpdateAgent.showUpdateDialog(activity, arg1);
                  break;
               case 1: // has no update
                  // 当不是主页面时提示
                  if (!home) {
                     Utils.showMessage("已经是最新版本了");
                  }
                  break;
               case 2: // none wifi
                  Utils.showMessage("没有wifi连接， 只在wifi下更新");
                  break;
               case 3: // time out
                  Utils.showMessage("网络超时");
                  break;
            }
         }
      });
   }

   /**
    * 回收LAYOUT
    */
   public static void unbindLayout(View view) {
      unbindDrawables(view);
      System.gc();
      // Runtime.getRuntime().gc();
   }

   private static void unbindDrawables(View view) {
      if (view == null) {
         return;
      }

      try {
         if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
         }

         if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
               unbindDrawables(((ViewGroup) view).getChildAt(i));
            }

            if (view != null) {
               try {
                  ((ViewGroup) view).removeAllViews();
               } catch (Exception e) {
                  // TODO: handle exception
                  e.printStackTrace();
               }
            }
         }
      } catch (NullPointerException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /**
    * getMapUrl:
    *
    * @Description: 获取静态图片url
    * @author jack.long
    */
   public static String getMapUrl(String lat, String lon, int width, int height, String address) {
      Trace.d("");
      // http://api.map.baidu.com/staticimage?center=116.403874,39.914889&width=400&height=300&zoom=11&markers=116.288891,40.004261
      String path = "http://api.map.baidu.com/staticimage?center="
          + lon
          + ","
          + lat
          + "&zoom=11&width="
          + width * MobileConfig.getMobileConfig(AULiveApplication.mContext).getDensity()
          + "&height="
          + height * MobileConfig.getMobileConfig(AULiveApplication.mContext).getDensity()
          + "&markers="
          + lon
          + ","
          + lat
          + "&labels="
          + lon
          + ","
          + lat
          + "&labelStyles="
          + address
          + ",1,12,0xff000000,0xff00,1";
      Trace.d("map path:" + path);
      return path;
   }

   /**
    * check the app is installed
    */
   public static boolean isAppInstalled(Context context, String packagename) {
      PackageInfo packageInfo;
      try {
         packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
      } catch (NameNotFoundException e) {
         packageInfo = null;
         e.printStackTrace();
      }
      if (packageInfo == null) {
         // System.out.println("没有安装");
         return false;
      } else {
         // System.out.println("已经安装");
         return true;
      }
   }

   public static boolean isLogin(Activity activity) {
      String uid = AULiveApplication.getUserInfo().getUid();
      String nickname = AULiveApplication.getUserInfo().getNickname();
      if (AULiveApplication.getMyselfUserInfo() == null
          || AULiveApplication.getUserInfo() == null
          || uid == null
          || uid.equals("")
          || uid.equals("0")) {
         Intent login_intent = new Intent(activity, LoginActivity.class);
         login_intent.putExtra(LoginActivity.back_home_key, false);
         activity.startActivity(login_intent);
//         activity.startActivity(new Intent(activity, TestActivity.class));
         return false;
      }
      return true;
   }

   /**
    * 从字符串中截取连续6位数字组合 ([0-9]{" + 4 + "})截取四位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
    *
    * @param str 短信内容
    * @return 截取得到的4位动态密码
    */
   public static String getDynamicPassword(String str) {
      // 4是验证码的位数
      Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])");
      Matcher m = continuousNumberPattern.matcher(str);
      String dynamicPassword = "";
      while (m.find()) {
         dynamicPassword = m.group();
      }

      return dynamicPassword;
   }

   public static List<String> splitURL(String url) {
      List<String> list = new ArrayList<String>();
      String[] splitOne = url.split("sign=");
      String[] splitTwo = splitOne[1].split("&t");
      list.add(splitOne[0]);
      list.add(splitTwo[0]);
      list.add(splitTwo[1]);
      return list;
   }

   public static List<String> splitSign(String sign) {
      List<String> list_sign = new ArrayList<String>();
      String s1 = sign.substring(0, 8);
      String s2 = sign.substring(8, 16);
      String s3 = sign.substring(16, 24);
      String s4 = sign.substring(24, 32);
      list_sign.add(s1);
      list_sign.add(s2);
      list_sign.add(s3);
      list_sign.add(s4);
      return list_sign;
   }
}
