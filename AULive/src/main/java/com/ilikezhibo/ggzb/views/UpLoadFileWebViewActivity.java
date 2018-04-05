package com.ilikezhibo.ggzb.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;
import java.io.File;

public class UpLoadFileWebViewActivity extends BaseActivity implements OnClickListener {

   private WebView introduce_webview;
   public final static String input_url = "input_url";
   public final static String actity_name = "actity_name";
   public final static String back_home_key = "back_home";
   public final static String is_from_list_key = "is_from_list";
   private boolean back_home;
   private String root_url;
   // 如果是从单个跳过来，则返回到1，如果是从列表跳过来则必须回到0
   private boolean is_from_list;

   @Override protected void onResume() {
      super.onResume();
      MobclickAgent.onResume(this);
   }

   @Override protected void onPause() {
      super.onPause();
      MobclickAgent.onPause(this);
   }

   @Override protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.authorize_webview);

      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      back_home = getIntent().getBooleanExtra(back_home_key, true);
      root_url = getIntent().getStringExtra(input_url);
      is_from_list = getIntent().getBooleanExtra(is_from_list_key, false);

      String name = getIntent().getStringExtra(actity_name);

      TextView tv_title = (TextView) this.findViewById(R.id.title);
      if (name != null && !name.equals("")) {
         tv_title.setText(name);
      }

      //设置webview 共享httpClient的cookie
      String mCookie =
              SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.COOKIE_KEY, "");
      Trace.d("webview cookie:"+mCookie);
      //CookieSyncManager.createInstance(this);
      //CookieManager cookieManager = CookieManager.getInstance();
      //cookieManager.setCookie(root_url, mCookie);
      //CookieSyncManager.getInstance().sync();

      WebViewFileUtils.syncCookie(root_url,mCookie,UpLoadFileWebViewActivity.this);

      introduce_webview = (WebView) findViewById(R.id.introduce_webview);
      //introduce_webview.setWebChromeClient(new WebChromeClient());
      introduce_webview.setDownloadListener(new MyWebViewDownLoadListener());
      introduce_webview.setWebChromeClient(new MyChromeViewClient());

      introduce_webview.setWebViewClient(new WebViewClient() {

         public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 当开启新的页面的时候用webview来进行处理而不是用系统自带的浏览器处理
            // view.loadUrl(url);
            Trace.d("url:" + url);
            //自定义处理
            if (url.startsWith("http://protocal.")) {
               //Utils.showMessage("兑换成功");
               String type = Uri.parse(url).getQueryParameter("type");
               if (type == null) {
                  type = "1";
               }
               if (type.equals("1")) {
                  //UpLoadFileWebViewActivity.this.finish();
               } else if (type.equals("2")) {
                  String uid = Uri.parse(url).getQueryParameter("uid");
                  String nickname = Uri.parse(url).getQueryParameter("nickname");
                  String face = Uri.parse(url).getQueryParameter("face");
                  Intent homepage_Intent =
                          new Intent(UpLoadFileWebViewActivity.this, HomePageActivity.class);
                  homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, uid);
                  UpLoadFileWebViewActivity.this.startActivity(homepage_Intent);
                  //UpLoadFileWebViewActivity.this.finish();
               } else if (type.equals("3")) {
                  String uid = Uri.parse(url).getQueryParameter("uid");
                  String nickname = Uri.parse(url).getQueryParameter("nickname");
                  String face = Uri.parse(url).getQueryParameter("face");
                  String grade = Uri.parse(url).getQueryParameter("grade");
                  String title = Uri.parse(url).getQueryParameter("title");
                  String time = Uri.parse(url).getQueryParameter("time");
                  String total = Uri.parse(url).getQueryParameter("total");
                  enterRoom(uid, AULiveApplication.getMyselfUserInfo().getUserPhone(), uid, face,
                          nickname);
               } else {

               }
               return true;
            }
            // return true;
            return false;
         }

         @Override
         public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // super.onReceivedSslError(view, handler, error);
            // handler.cancel(); // 默认的处理方式，WebView变成空白页
            handler.proceed();// 接受证书
            // handleMessage(Message msg); 其他处理
         }

         @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
         }

         @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
         }
      });
      WebSettings webSettings = introduce_webview.getSettings();
      webSettings.setJavaScriptEnabled(true);
      webSettings.setDefaultTextEncodingName("gb2312");
      webSettings.setAllowFileAccess(true);
      webSettings.setDomStorageEnabled(true);

      //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //   webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
      //}
      // introduce_webview.setOnClickListener(this);

      if (root_url == null || root_url.equals("")) {
         this.finish();
      }
      if (!root_url.startsWith("http://")&&!root_url.startsWith("https://")) {
         root_url = "http://" + root_url;
      }
      Trace.d("root_url:"+root_url);

      introduce_webview.loadUrl(root_url);
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {

         case R.id.back:
            goBack();
            break;
      }
   }

   private class MyWebViewDownLoadListener implements DownloadListener {

      @Override public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                            String mimetype, long contentLength) {
         Uri uri = Uri.parse(url);
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);
      }
   }

   @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
      if ((keyCode == KeyEvent.KEYCODE_BACK)
              && introduce_webview != null
              && introduce_webview.canGoBack()) {
         goBack();
         return true;
      } else if (keyCode == KeyEvent.KEYCODE_BACK) {
         this.finish();
      }
      return super.onKeyDown(keyCode, event);
   }

   private void goBack() {

      int page_index = introduce_webview.copyBackForwardList().getCurrentIndex();
      Trace.d("page index:" + page_index);

      if (is_from_list && introduce_webview != null && introduce_webview.canGoBack()) {
         introduce_webview.goBack();
      } else if (introduce_webview != null && introduce_webview.canGoBack() && page_index > 1) {
         introduce_webview.goBack();
      } else {
         if (back_home) {
            startActivity(new Intent(UpLoadFileWebViewActivity.this, AULiveHomeActivity.class));
            this.finish();
         } else {
            this.finish();
         }
      }
   }

   ///////////////////
   //图片上传
   private UploadHandler mUploadHandler;

   @Override protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

      if (requestCode == Controller.FILE_SELECTED) {
         // Chose reset file from the file picker.
         if (mUploadHandler != null) {
            mUploadHandler.onResult(resultCode, intent);
         }
      }else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5){
         if (null == mUploadMessageForAndroid5)
            return;
         Uri result = (intent == null || resultCode != RESULT_OK) ? null: intent.getData();
         if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
         } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
         }
         mUploadMessageForAndroid5 = null;
      }

      super.onActivityResult(requestCode, resultCode, intent);
   }

   class MyChromeViewClient extends WebChromeClient {

      @Override public void onCloseWindow(WebView window) {
         UpLoadFileWebViewActivity.this.finish();
         super.onCloseWindow(window);
      }

      public void onProgressChanged(WebView view, final int progress) {

      }

      @Override
      public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

         new AlertDialog.Builder(UpLoadFileWebViewActivity.this).setTitle("提示信息")
                 .setMessage(message)
                 .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override public void onClick(DialogInterface dialog, int which) {
                       result.confirm();
                    }
                 })
                 .setCancelable(false)
                 .create()
                 .show();
         return true;
      }

      @Override
      public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

         new AlertDialog.Builder(UpLoadFileWebViewActivity.this).setTitle("提示信息")
                 .setMessage(message)
                 .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override public void onClick(DialogInterface dialog, int which) {
                       result.confirm();
                    }
                 })
                 .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       result.cancel();
                    }
                 })
                 .setCancelable(false)
                 .create()
                 .show();
         return true;
      }

      // Android 2.x
      public void openFileChooser(ValueCallback<Uri> uploadMsg) {
         openFileChooser(uploadMsg, "");
      }

      // Android 3.0
      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
         openFileChooser(uploadMsg, "", "filesystem");
      }

      // Android 4.1
      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

         mUploadHandler = new UploadHandler(new Controller());
         mUploadHandler.openFileChooser(uploadMsg, acceptType, capture);
      }

      // For Android > 5.0
      public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
         openFileChooserImplForAndroid5(uploadMsg);
         return true;
      }
   }
   public ValueCallback<Uri[]> mUploadMessageForAndroid5;
   public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

   private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
      mUploadMessageForAndroid5 = uploadMsg;
      Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
      contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
      contentSelectionIntent.setType("image/*");

      Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
      chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
      chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择上传文件");

      startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
   }


   // copied from android-4.4.3_r1/src/com/android/browser/UploadHandler.java

   class UploadHandler {
      /*
       * The Object used to inform the WebView of the file to upload.
       */
      private ValueCallback<Uri> mUploadMessage;
      private String mCameraFilePath;
      private boolean mHandled;
      private boolean mCaughtActivityNotFoundException;
      private Controller mController;

      public UploadHandler(Controller controller) {
         mController = controller;
      }

      public String getFilePath() {
         return mCameraFilePath;
      }

      boolean handled() {
         return mHandled;
      }

      public void onResult(int resultCode, Intent intent) {
         if (resultCode == Activity.RESULT_CANCELED && mCaughtActivityNotFoundException) {
            // Couldn't resolve an activity, we are going to try again so skip
            // this result.
            mCaughtActivityNotFoundException = false;
            return;
         }
         Uri result =
                 (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();

         // As we ask the camera to save the result of the user taking
         // reset picture, the camera application does not return anything other
         // than RESULT_OK. So we need to check whether the file we expected
         // was written to disk in the in the case that we
         // did not get an intent returned but did get reset RESULT_OK. If it was,
         // we assume that this result has came back from the camera.
         if (result == null && intent == null && resultCode == Activity.RESULT_OK) {
            File cameraFile = new File(mCameraFilePath);
            if (cameraFile.exists()) {
               result = Uri.fromFile(cameraFile);
               // Broadcast to the media scanner that we have reset new photo
               // so it will be added into the gallery for the user.
               mController.getActivity()
                       .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
            }
         }
         mUploadMessage.onReceiveValue(result);
         mHandled = true;
         mCaughtActivityNotFoundException = false;
      }

      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
         final String imageMimeType = "image/*";
         final String videoMimeType = "video/*";
         final String audioMimeType = "audio/*";
         final String mediaSourceKey = "capture";
         final String mediaSourceValueCamera = "camera";
         final String mediaSourceValueFileSystem = "filesystem";
         final String mediaSourceValueCamcorder = "camcorder";
         final String mediaSourceValueMicrophone = "microphone";
         // According to the spec, media source can be 'filesystem' or 'camera' or 'camcorder'
         // or 'microphone' and the default value should be 'filesystem'.
         String mediaSource = mediaSourceValueFileSystem;
         if (mUploadMessage != null) {
            // Already reset file picker operation in progress.
            return;
         }
         mUploadMessage = uploadMsg;
         // Parse the accept type.
         String params[] = acceptType.split(";");
         String mimeType = params[0];
         if (capture.length() > 0) {
            mediaSource = capture;
         }
         if (capture.equals(mediaSourceValueFileSystem)) {
            // To maintain backwards compatibility with the previous implementation
            // of the media capture API, if the value of the 'capture' attribute is
            // "filesystem", we should examine the accept-type for reset MIME type that
            // may specify reset different capture value.
            for (String p : params) {
               String[] keyValue = p.split("=");
               if (keyValue.length == 2) {
                  // Process key=value parameters.
                  if (mediaSourceKey.equals(keyValue[0])) {
                     mediaSource = keyValue[1];
                  }
               }
            }
         }
         //Ensure it is not still set from reset previous upload.
         mCameraFilePath = null;
         if (mimeType.equals(imageMimeType)) {
            if (mediaSource.equals(mediaSourceValueCamera)) {
               // Specified 'image/*' and requested the camera, so go ahead and launch the
               // camera directly.
               startActivity(createCameraIntent());
               return;
            } else {
               // Specified just 'image/*', capture=filesystem, or an invalid capture parameter.
               // In all these cases we show reset traditional picker filetered on accept type
               // so launch an intent for both the Camera and image/* OPENABLE.
               Intent chooser = createChooserIntent(createCameraIntent());
               chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
               startActivity(chooser);
               return;
            }
         } else if (mimeType.equals(videoMimeType)) {
            if (mediaSource.equals(mediaSourceValueCamcorder)) {
               // Specified 'video/*' and requested the camcorder, so go ahead and launch the
               // camcorder directly.
               startActivity(createCamcorderIntent());
               return;
            } else {
               // Specified just 'video/*', capture=filesystem or an invalid capture parameter.
               // In all these cases we show an intent for the traditional file picker, filtered
               // on accept type so launch an intent for both camcorder and video/* OPENABLE.
               Intent chooser = createChooserIntent(createCamcorderIntent());
               chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
               startActivity(chooser);
               return;
            }
         } else if (mimeType.equals(audioMimeType)) {
            if (mediaSource.equals(mediaSourceValueMicrophone)) {
               // Specified 'audio/*' and requested microphone, so go ahead and launch the sound
               // recorder.
               startActivity(createSoundRecorderIntent());
               return;
            } else {
               // Specified just 'audio/*',  capture=filesystem of an invalid capture parameter.
               // In all these cases so go ahead and launch an intent for both the sound
               // recorder and audio/* OPENABLE.
               Intent chooser = createChooserIntent(createSoundRecorderIntent());
               chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
               startActivity(chooser);
               return;
            }
         }
         // No special handling based on the accept type was necessary, so trigger the default
         // file upload chooser.
         startActivity(createDefaultOpenableIntent());
      }

      private void startActivity(Intent intent) {
         try {
            mController.getActivity().startActivityForResult(intent, Controller.FILE_SELECTED);
         } catch (ActivityNotFoundException e) {
            // No installed app was able to handle the intent that
            // we sent, so fallback to the default file upload control.
            try {
               mCaughtActivityNotFoundException = true;
               mController.getActivity()
                       .startActivityForResult(createDefaultOpenableIntent(), Controller.FILE_SELECTED);
            } catch (ActivityNotFoundException e2) {
               // Nothing can return us reset file, so file upload is effectively disabled.
               Toast.makeText(mController.getActivity(), "文件上传不可用.", Toast.LENGTH_LONG).show();
            }
         }
      }

      private Intent createDefaultOpenableIntent() {
         // Create and return reset chooser with the default OPENABLE
         // actions including the camera, camcorder and sound
         // recorder where available.
         Intent i = new Intent(Intent.ACTION_GET_CONTENT);
         i.addCategory(Intent.CATEGORY_OPENABLE);
         i.setType("*/*");
         Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
                 createSoundRecorderIntent());
         chooser.putExtra(Intent.EXTRA_INTENT, i);
         return chooser;
      }

      private Intent createChooserIntent(Intent... intents) {
         Intent chooser = new Intent(Intent.ACTION_CHOOSER);
         chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
         chooser.putExtra(Intent.EXTRA_TITLE, "选择上传文件");
         return chooser;
      }

      private Intent createOpenableIntent(String type) {
         Intent i = new Intent(Intent.ACTION_GET_CONTENT);
         i.addCategory(Intent.CATEGORY_OPENABLE);
         i.setType(type);
         return i;
      }

      private Intent createCameraIntent() {
         Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         File externalDataDir =
                 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
         File cameraDataDir =
                 new File(externalDataDir.getAbsolutePath() + File.separator + "browser-photos");
         cameraDataDir.mkdirs();
         mCameraFilePath =
                 cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
         cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
         return cameraIntent;
      }

      private Intent createCamcorderIntent() {
         return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
      }

      private Intent createSoundRecorderIntent() {
         return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
      }
   }

   class Controller {

      final static int FILE_SELECTED = 4;

      Activity getActivity() {
         return UpLoadFileWebViewActivity.this;
      }
   }



   //进入房间接口
   private void enterRoom(String liveuid, final String userid, final String uid, final String face,
                          final String nickname) {
      RequestInformation request = null;

      try {
         StringBuilder sb =
                 new StringBuilder(UrlHelper.enterRoomUrl + "?liveuid=" + liveuid + "&userid=" + userid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
         request.addPostParams("roomid", liveuid + "");
         request.addPostParams("userid", userid);
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
               MedalListEvent medalListEvent = new MedalListEvent();
               medalListEvent.anchor_medal = callback.anchor_medal;
               medalListEvent.wanjia_medal = callback.wanjia_medal;
               medalListEvent.act = callback.act;
               EventBus.getDefault().postSticky(medalListEvent);

            /*   DanmuRainListEntity danmuRainListEntity = new DanmuRainListEntity();
               danmuRainListEntity.words=callback.words;
               EventBus.getDefault().postSticky(danmuRainListEntity);*/

               UpLoadFileWebViewActivity.this.startActivity(
                       new Intent(UpLoadFileWebViewActivity.this, AvActivity.class).putExtra(
                               AvActivity.GET_UID_KEY, uid)
                               .putExtra(AvActivity.IS_CREATER_KEY, false)
                               .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_FACE, face)
                               .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_NICKNAME, nickname)
                               .putExtra(AvActivity.EXTRA_RECIVE_DIAMOND, callback.recv_diamond)
                               .putExtra(AvActivity.EXTRA_SYS_MSG, callback.sys_msg)
                               .putExtra(AvActivity.EXTRA_IS_ON_SHOW, callback.is_live)
                               .putExtra(AvActivity.EXTRA_ONLINE_NUM, callback.total)
                               .putExtra(AvActivity.EXTRA_IS_MANAGER, callback.is_manager)
                               .putExtra(AvActivity.EXTRA_IS_GAG, callback.is_gag)
                               .putExtra(AvActivity.EXTRA_IS_SUPER_MANAGER, callback.show_manager)

               );

            } else {
               Utils.showMessage("" + callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }
}
