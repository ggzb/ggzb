package com.ilikezhibo.ggzb.avsdk.badwordfilter;

import android.app.Activity;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.PathCallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.JsonParser;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import update.StorageUtils;

/**
 * Created by big on 8/25/16.
 */
public class BadWordDownloadHelper {

   public static String BADWORDDOWNLOAD_KEY = "BADWORDDOWNLOAD_KEY";

   // 下载与打开附件
   public static void downloadTxtFile(String voiceUrl, Activity context) {
      if (voiceUrl == null || voiceUrl.equals("")) {
         Trace.d("voiceUrl==null");
         return;
      }
      File dir = StorageUtils.getCacheDirectory(context);
      String apkName = getFileTag(voiceUrl);
      File apkFile = new File(dir, apkName);

      if (apkFile.exists()) {
      //已经下载过
      //   String info =
      //       SharedPreferenceTool.getInstance().getString(BADWORDDOWNLOAD_KEY, "");
      //   if (TextUtil.isValidate(info)) {
      //      //ArrayList<String> b_words = JsonParser.deserializeByJson(info, ArrayList.class);
      //      return;
      //   }
         //没保存过是
         String content = null;
         try {
            InputStream instream = new FileInputStream(apkFile);
            if (instream != null) {
               InputStreamReader inputreader = new InputStreamReader(instream);
               BufferedReader buffreader = new BufferedReader(inputreader);
               String line;
               //分行读取
               while ((line = buffreader.readLine()) != null) {
                  content += line + "\n";
               }
               instream.close();
            }


            SharedPreferenceTool.getInstance()
                .saveString(BADWORDDOWNLOAD_KEY, JsonParser.serializeToJson(content));
         } catch (Exception e) {

         }
      } else {
         downFile2(voiceUrl, context);
      }
   }

   // 下载附件
   public static void downFile2(final String voiceUrl, final Activity context) {
      File dir = StorageUtils.getCacheDirectory(context);
      final String apkName = getFileTag(voiceUrl);
      final File apkFile = new File(dir, apkName);

      RequestInformation request =
          new RequestInformation(voiceUrl, RequestInformation.REQUEST_METHOD_GET);
      request.setProgressChangeListener(new IProgressListener() {

         @Override public void progressChanged(int status, int progress, String operationName) {
            if (progress == 100) {
               //加载txt
//没保存过是
               String content = null;
               try {
                  InputStream instream = new FileInputStream(apkFile);
                  if (instream != null) {
                     InputStreamReader inputreader = new InputStreamReader(instream);
                     BufferedReader buffreader = new BufferedReader(inputreader);
                     String line;
                     //分行读取
                     while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                     }
                     instream.close();
                  }
                  //String[] words = content.split(",");
                  //ArrayList<String> al_words = new ArrayList<String>();
                  //al_words.addAll(Arrays.asList(words));

                  SharedPreferenceTool.getInstance()
                      .saveString(BADWORDDOWNLOAD_KEY, JsonParser.serializeToJson(content));
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
