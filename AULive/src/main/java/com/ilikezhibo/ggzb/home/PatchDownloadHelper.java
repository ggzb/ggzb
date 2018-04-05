package com.ilikezhibo.ggzb.home;

import android.app.Activity;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.PathCallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

import update.StorageUtils;

/**
 * Created by big on 8/25/16.
 */
public class PatchDownloadHelper {

   public static String PATCHDDOWNLOAD_URLS_KEY = "PATCHDDOWNLOAD_URLS_KEY";

   // 下载与打开附件
   public static void downloadPatchFile(String patchUrl, Activity context) {

      String urls =
          SharedPreferenceTool.getInstance().getString(PATCHDDOWNLOAD_URLS_KEY, "");

      if(urls.contains(patchUrl)){
         //所有补丁只打一次
         return;
      }

      if (patchUrl == null || patchUrl.equals("")) {
         Trace.d("voiceUrl==null");
         return;
      }
      File dir = StorageUtils.getCacheDirectory(context);
      String apkName = getFileTag(patchUrl);
      File apkFile = new File(dir, apkName);

      if (apkFile.exists()) {
         //apkFile.delete();
         //SharePatchFileUtil.safeDeleteFile(apkFile);

         SharedPreferenceTool.getInstance()
             .saveString(PATCHDDOWNLOAD_URLS_KEY, urls+","+patchUrl);

         TinkerInstaller.onReceiveUpgradePatch(context, apkFile.getPath());

      } else {
         downFile2(patchUrl, context);
      }
   }

   // 下载附件
   public static void downFile2(final String patchUrl, final Activity context) {
      File dir = StorageUtils.getCacheDirectory(context);
      final String apkName = getFileTag(patchUrl);
      final File apkFile = new File(dir, apkName);

      RequestInformation request =
          new RequestInformation(patchUrl, RequestInformation.REQUEST_METHOD_GET);
      request.setProgressChangeListener(new IProgressListener() {

         @Override public void progressChanged(int status, int progress, String operationName) {
            if (progress == 100) {

               String urls =
                   SharedPreferenceTool.getInstance().getString(PATCHDDOWNLOAD_URLS_KEY, "");

               SharedPreferenceTool.getInstance()
                   .saveString(PATCHDDOWNLOAD_URLS_KEY, urls+","+patchUrl);

               //没保存过是
               Trace.d("开始打tinker补丁");
               TinkerInstaller.onReceiveUpgradePatch(context, apkFile.getPath());
               //TinkerInstaller.onReceiveUpgradePatch(context,
               //    Environment.getExternalStorageDirectory().getAbsolutePath()
               //        + "/patch_signed_7zip.apk");
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
