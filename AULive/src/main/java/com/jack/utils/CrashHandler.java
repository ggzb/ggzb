package com.jack.utils;

/**
 * @ClassName: CrashHandler
 * @Description: TODO
 * @author jack.long
 * @date 2013-12-28 下午9:41:57
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.StringCallback;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.home.MainActivity;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理�?当程序发生Uncaught异常的时�?有该类来接管程序,并记录发送错误报�?
 *
 * @author jack.long
 */
public class CrashHandler implements UncaughtExceptionHandler {

   // 系统默认的UncaughtException处理�?
   private Thread.UncaughtExceptionHandler mDefaultHandler;
   // CrashHandler实例
   private static CrashHandler INSTANCE = new CrashHandler();
   // 程序的Context对象
   private Context mContext;
   // 用来存储设备信息和异常信�?
   private Map<String, String> infos = new HashMap<String, String>();

   /** 保证只有�?��CrashHandler实例 */
   private CrashHandler() {
   }

   /** 获取CrashHandler实例 ,单例模式 */
   public static CrashHandler getInstance() {
      return INSTANCE;
   }

   /**
    * 初始�?
    */
   public void init(Context context) {
      mContext = context;
      // 获取系统默认的UncaughtException处理�?
      mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
      // 设置该CrashHandler为程序的默认处理�?
      Thread.setDefaultUncaughtExceptionHandler(this);
   }

   /**
    * 当UncaughtException发生时会转入该函数来处理
    */
   @Override public void uncaughtException(Thread thread, Throwable ex) {
      if (!handleException(ex) && mDefaultHandler != null) {
         // 如果用户没有处理则让系统默认的异常处理器来处�?
         mDefaultHandler.uncaughtException(thread, ex);
      } else {
         Intent intent = new Intent(AULiveApplication.mContext, MainActivity.class);
         PendingIntent restartIntent =
             PendingIntent.getActivity(AULiveApplication.mContext, 0, intent,
                 Intent.FLAG_ACTIVITY_NEW_TASK);
         AlarmManager mgr =
             (AlarmManager) AULiveApplication.mContext.getSystemService(Context.ALARM_SERVICE);
         mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 1秒钟后重启应�?
         ActivityStackManager.getInstance().exitActivity();
      }
   }

   /**
    * 自定义错误处�?收集错误信息 发�?错误报告等操作均在此完成.
    *
    * @return true:如果处理了该异常信息;否则返回false.
    */
   private boolean handleException(Throwable ex) {
      if (ex == null) {
         return false;
      }
      // 使用Toast来显示异常信�?
      new Thread() {
         @Override public void run() {

            Looper.prepare();
            // Toast.makeText(mContext, "很抱�?程序出现异常,即将重启.",
            // Toast.LENGTH_LONG)
            // .show();
            Looper.loop();
         }
      }.start();

      // 收集设备参数信息
      collectDeviceInfo(mContext);
      // 保存日志文件
      saveCrashInfo2Server(ex);
      return true;
   }

   /**
    * 收集设备参数信息
    */
   private void collectDeviceInfo(Context ctx) {
      try {
         PackageManager pm = ctx.getPackageManager();
         PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
         if (pi != null) {
            String versionName = pi.versionName == null ? "null" : pi.versionName;
            String versionCode = pi.versionCode + "";
            infos.put("versionName", versionName);
            infos.put("versionCode", versionCode);
         }
      } catch (NameNotFoundException e) {
         Trace.d("an error occured when collect package info" + e);
      }
      Field[] fields = Build.class.getDeclaredFields();
      for (Field field : fields) {
         try {
            field.setAccessible(true);
            infos.put(field.getName(), field.get(null).toString());
            Trace.d(field.getName() + " : " + field.get(null));
         } catch (Exception e) {
            Trace.d("an error occured when collect crash info" + e);
         }
      }
   }

   /**
    * 保存错误信息到文件中
    *
    * @return 返回文件名称, 便于将文件传送到服务�?
    */
   private void saveCrashInfo2Server(Throwable ex) {

      StringBuffer sb = new StringBuffer();
      for (Map.Entry<String, String> entry : infos.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();
         sb.append(key + "=" + value + "\n");
      }

      Writer writer = new StringWriter();
      PrintWriter printWriter = new PrintWriter(writer);
      ex.printStackTrace(printWriter);
      Throwable cause = ex.getCause();
      while (cause != null) {
         cause.printStackTrace(printWriter);
         cause = cause.getCause();
      }
      printWriter.close();
      String result = writer.toString();
      sb.append(result);
      Trace.e("error Info:" + sb.toString());
      uploadErrorInfo(sb.toString());
   }

   //上传错误信息到服务器
   private void uploadErrorInfo(String content) {
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/other/errlog",
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("cont", content);

      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            Trace.d("upload error fail");
         }

         @Override public void onCallback(String callback) {
            Trace.d("upload error info:" + callback);
         }
      });
      request.execute();
   }
}
