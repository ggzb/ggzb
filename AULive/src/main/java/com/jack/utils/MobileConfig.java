package com.jack.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import java.util.UUID;

public class MobileConfig {
   private static final String MARKET_360 = "360";// 360
   private static final String MARKET_WANDOUJIA = "wandoujia";// 豌豆�?
   private static final String MARKET_ANDROID = "android";// 安卓市场
   private static final String MARKET_BAIDU = "baidu";// 百度市场
   private static final String MARKET_QQ = "myapp";// 腾讯应用�?
   private static final String MARKET_91 = "91";// 91助手
   private static final String MARKET_ANZHI = "anzhi";// 安智市场
   private static final String MARKET_YINGYONGHUI = "yingyonghui";// 应用�?
   private static final String MARKET_OTHER = "other";// 其他市场

   private TelephonyManager tm;
   private Context ctx;
   private ConnectivityManager cm;

   private static MobileConfig mMobileConfig;
   private PackageInfo mPkgInfo;

   private static final float HDPI = 1.5f;

   public static MobileConfig getMobileConfig(Context mCon) {
      if (mMobileConfig == null) {
         mMobileConfig = new MobileConfig(mCon);
      }
      return mMobileConfig;
   }

   public MobileConfig(Context ctx) {
      this.ctx = ctx;
      init();
      PackageManager manager = ctx.getPackageManager();
      try {
         mPkgInfo = manager.getPackageInfo(ctx.getPackageName(), 0);
      } catch (NameNotFoundException e) {
         e.printStackTrace();
      }
   }

   void init() {
      tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
      cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
   }

   public float getDensity() {
      if (ctx != null) {
         return ctx.getResources().getDisplayMetrics().density;
      } else {
         return HDPI;
      }
   }

   /** 获取当前市场名称 */
   public String getCurrMarketName() {
      return MARKET_QQ;
   }

   /** 获取包版本Code�? 在AndroidManifest.xml中的versionCode中进行配�? */
   public int getPkgVerCode() {
      return mPkgInfo.versionCode;
   }

   /** 返回应用包名. */
   public String getPackageName() {
      return mPkgInfo.packageName;
   }

   /** 返回设备id. */
   public String getDeviceId() {
      String tmDevice, tmSerial, androidId;
      tmDevice = "" + tm.getDeviceId();
      // tmSerial = "" + tm.getSimSerialNumber();
      androidId = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(),
          android.provider.Settings.Secure.ANDROID_ID);
      // UUID deviceUuid = new UUID(androidId.hashCode(), ((long)
      // tmDevice.hashCode() << 32) | tmSerial.hashCode());
      UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32));
      // Trace.d("uuid:"+deviceUuid.toString());
      // Trace.d("deviceId:"+tmDevice+",androidId:"+androidId);

      return deviceUuid.toString();
   }

   /** 返回设备iemi. */
   public String getIemi() {
      if (tm.getDeviceId() == null || tm.getDeviceId().trim().length() <= 0) {
         return getOnlyDevice();
      }

      return tm.getDeviceId();
   }

   public String getLine1Number() {
      return tm.getLine1Number();
   }

   public String getSimSerialNumber() {
      return tm.getSimSerialNumber();
   }

   public String getSubscriberId() {
      return tm.getSubscriberId();
   }

   public String getNetworkOperator() {
      return tm.getNetworkOperator();
   }

   public String getNetworkOperatorName() {
      return tm.getNetworkOperatorName();
   }

   public int getNetworkType() {
      return tm.getNetworkType();
   }

   /** 返回 运营商名�? */
   public String getSimOperatorName() {
      String operator = tm.getSimOperator();
      String name = "";

      if (operator != null) {
         if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
            // 中国移动
            name = "中国移动";
         } else if (operator.equals("46001")) {
            // 中国联通
            name = "中国联通";
         } else if (operator.equals("46003")) {
            // 中国电信
            name = "中国电信";
         }
      }

      return name;
   }

   public int getPhoneType() {
      return tm.getPhoneType();
   }

   public String getModel() {
      return Build.MODEL;
   }

   public String getBrand() {
      return Build.BRAND;
   }

   public String getDevice() {
      return Build.DEVICE;
   }

   public String getOnlyDevice() {
      String m_szDevIDShort = "45"
          +
          // we make this look like reset valid IMEI
          Build.BOARD.length() % 10
          + Build.BRAND.length() % 10
          + Build.CPU_ABI.length() % 10
          + Build.DEVICE.length() % 10
          + Build.DISPLAY.length() % 10
          + Build.HOST.length() % 10
          + Build.ID.length() % 10
          + Build.MANUFACTURER.length() % 10
          + Build.MODEL.length() % 10
          + Build.PRODUCT.length() % 10
          + Build.TAGS.length() % 10
          + Build.TYPE.length() % 10
          + Build.USER.length() % 10;
      return m_szDevIDShort;
   }

   public String getProduct() {
      return Build.PRODUCT;
   }

   public String getMANUFACTURER() {
      return Build.MANUFACTURER;
   }

   public String getOS() {
      return "android";
   }

   public int getWidth() {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      return dm.widthPixels;
   }

   public int getHeight() {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      return dm.heightPixels;
   }

   public int getXByScale(int pX) {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      float scale = (dm.widthPixels / 320f) < (dm.heightPixels / 480f) ? dm.widthPixels / 320f
          : dm.heightPixels / 480f;
      return (int) (pX * scale);
   }

   public int getYByScale(int pY) {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      float scale = (dm.widthPixels / 320f) < (dm.heightPixels / 480f) ? dm.widthPixels / 320f
          : dm.heightPixels / 480f;
      return (int) (pY * scale);
   }

   public float getScale() {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      float scale = (dm.widthPixels / 320f) < (dm.heightPixels / 480f) ? dm.widthPixels / 320f
          : dm.heightPixels / 480f;
      return scale;
   }

   public int getYByScaleHeight(int pY) {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      float scale = (dm.heightPixels / 480f);
      return (int) (pY * scale);
   }

   public int getXByScaleWidth(int pX) {
      DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
      float scale = (dm.widthPixels / 320f);
      return (int) (pX * scale);
   }

   public String getUuid() {
      return Settings.Secure.getString(this.ctx.getContentResolver(),
          android.provider.Settings.Secure.ANDROID_ID);
   }

   /** 返回网络类型. */
   public String getNetworkTypeName() {
      String typeName = "others";
      if (cm == null) {
         typeName = "others";
      } else {
         NetworkInfo info = cm.getActiveNetworkInfo();
         if (info == null) {
            typeName = "others";
         } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

            switch (this.getNetworkType()) {
               case TelephonyManager.NETWORK_TYPE_EDGE:
                  typeName = "EDGE";
                  break;
               case TelephonyManager.NETWORK_TYPE_GPRS:
                  typeName = "GPRS";
                  break;

               case TelephonyManager.NETWORK_TYPE_UMTS:
                  typeName = "UMTS";
                  break;

               case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                  typeName = "others";
                  break;
            }
         } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            typeName = "WIFI";
         }
      }
      return typeName;
   }

   /**
    * Get the model of phone.For example:C8600,W711 and so on.
    */
   public String getMobileModel() {
      String mPhoneType = Build.MODEL;
      return mPhoneType;
   }

   /**
    * Get the operation system of phone.
    */
   public String getMobileOsVersion() {
      String mSdkVersion = Build.VERSION.RELEASE;
      return mSdkVersion;
   }


   public String getPkgVerName() {
      return mPkgInfo.versionName;
   }

   /**
    * 获取application中指定的meta-data
    * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
    */
   public static String getAppMetaData(Context ctx, String key) {
      if (ctx == null || android.text.TextUtils.isEmpty(key)) {
         return null;
      }
      String resultData = null;
      try {
         PackageManager packageManager = ctx.getPackageManager();
         if (packageManager != null) {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
               if (applicationInfo.metaData != null) {
                  resultData = applicationInfo.metaData.getString(key);
               }
            }

         }
      } catch (PackageManager.NameNotFoundException e) {
         e.printStackTrace();
      }

      if(resultData==null||resultData.equals("")){
         resultData="myapp";
      }
      //Trace.d("getAppMetaData:"+resultData);
      return resultData;
   }
}
