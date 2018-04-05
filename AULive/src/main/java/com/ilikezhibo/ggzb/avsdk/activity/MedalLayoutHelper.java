package com.ilikezhibo.ggzb.avsdk.activity;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.PathCallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import java.io.File;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import update.StorageUtils;

/**
 * Created by big on 8/25/16.
 */
public class MedalLayoutHelper {

   private AvActivity mActivity;

   public MedalLayoutHelper(AvActivity mActivity1) {
      mActivity = mActivity1;
      init();
   }

   private LinearLayout medal_ly;

   private void init() {
      medal_ly = (LinearLayout) mActivity.findViewById(R.id.medal_ly);

      ArrayList<String> tem_urls = new ArrayList<String>();
      //先添加主播勋章
      if (mActivity.medalListEvent != null && mActivity.medalListEvent.anchor_medal != null) {
         tem_urls.addAll(mActivity.medalListEvent.anchor_medal);
      }
      ////再添加玩家勋章
      //if (mActivity.medalListEvent != null && mActivity.medalListEvent.wanjia_medal != null) {
      //   tem_urls.addAll(mActivity.medalListEvent.wanjia_medal);
      //}
      //把所有的子view隐藏
      for (int i = 0; i < medal_ly.getChildCount(); i++) {
         medal_ly.getChildAt(i).setVisibility(View.GONE);
      }

      for (int i = 0; i < medal_ly.getChildCount() && i < tem_urls.size(); i++) {
         String url = tem_urls.get(i);
         try {
            showGifImage(url, mActivity, medal_ly.getChildAt(i));
         } catch (Exception e) {

         }
      }
   }

   // 下载与打开附件
   public static void showGifImage(String voiceUrl, Activity context, View medal_ly) {
      if (voiceUrl == null || voiceUrl.equals("")) {
         Trace.d("voiceUrl==null");
         return;
      }
      File dir = StorageUtils.getCacheDirectory(context);
      String apkName = getFileTag(voiceUrl);
      File apkFile = new File(dir, apkName);

      if (apkFile.exists()) {

         //String localVoicePath = apkFile.getPath();
         //Trace.d("本地附件存在 localVoicePath:" + localVoicePath);
         try {
            medal_ly.setVisibility(View.VISIBLE);
            GifDrawable gifFromPath = new GifDrawable(apkFile);
            //加载gif图片
            GifImageView gif_view = (GifImageView) medal_ly.findViewById(R.id.gif_iv);
            gif_view.setImageDrawable(gifFromPath);
         } catch (Exception e) {
            e.printStackTrace();
         }
      } else {
         downFile2(voiceUrl, context, medal_ly);
      }
   }

   // 下载附件
   public static void downFile2(final String voiceUrl, final Activity context,
       final View medal_ly) {
      File dir = StorageUtils.getCacheDirectory(context);
      final String apkName = getFileTag(voiceUrl);
      final File apkFile = new File(dir, apkName);

      RequestInformation request =
          new RequestInformation(voiceUrl, RequestInformation.REQUEST_METHOD_GET);
      request.setProgressChangeListener(new IProgressListener() {

         @Override public void progressChanged(int status, int progress, String operationName) {
            if (progress == 100) {

               //String localVoicePath = apkFile.getPath();
               //Trace.d("localVoicePath:" + localVoicePath);
               //加载gif图片
               try {
                  medal_ly.setVisibility(View.VISIBLE);
                  GifDrawable gifFromPath = new GifDrawable(apkFile);
                  //加载gif图片
                  GifImageView gif_view = (GifImageView) medal_ly.findViewById(R.id.gif_iv);
                  gif_view.setImageDrawable(gifFromPath);
               } catch (Exception e) {
               }
            }
         }
      });
      request.setCallback(new PathCallback() {

         @Override public void onFailure(AppException e) {

         }

         @Override public void onCallback(String callback) {
         }
      }.setFilePath(apkFile.getPath()));
      request.execute();
   }

   // 获取附件tag
   public static String getFileTag(String fileUrl) {
      if (fileUrl == null) {
         return "";
      }

      return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
   }
}
