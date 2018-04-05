package com.ilikezhibo.ggzb.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
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

import de.greenrobot.event.EventBus;

public class UserInfoWebViewActivity extends BaseActivity implements OnClickListener {

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
//      MobclickAgent.onResume(this);
   }

   @Override protected void onPause() {
      super.onPause();
//      MobclickAgent.onPause(this);
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

      //我的收益做特殊处理，有收益记录
      if (root_url.contains("profile/earnings")) {
         TextView topRight_clean_Btn = (TextView) findViewById(R.id.topRightBtn);
         topRight_clean_Btn.setText("兑换记录");
         topRight_clean_Btn.setVisibility(View.VISIBLE);
         topRight_clean_Btn.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
               Intent intent =
                       new Intent(UserInfoWebViewActivity.this, UserInfoWebViewActivity.class);

               intent.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "profile/exchangelog?page=1");
               intent.putExtra(WebViewActivity.back_home_key, false);
               intent.putExtra(WebViewActivity.actity_name, "兑换记录");
               UserInfoWebViewActivity.this.startActivity(intent);
            }
         });
      }
      //设置webview 共享httpClient的cookie
      String mCookie =
              SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.COOKIE_KEY, "");
      Trace.d("webview cookie:"+mCookie);
      //CookieSyncManager.createInstance(this);
      //CookieManager cookieManager = CookieManager.getInstance();
      //cookieManager.setAcceptCookie(true);
      //cookieManager.removeSessionCookie();
      //cookieManager.setCookie(root_url, mCookie);
      //if (Build.VERSION.SDK_INT < 21) {
      //   CookieSyncManager.getInstance().sync();
      //} else {
      //   CookieManager.getInstance().flush();
      //}

      WebViewFileUtils.syncCookie(root_url,mCookie,UserInfoWebViewActivity.this);

      introduce_webview = (WebView) findViewById(R.id.introduce_webview);
      introduce_webview.setWebChromeClient(new WebChromeClient());
      introduce_webview.setDownloadListener(new MyWebViewDownLoadListener());
      introduce_webview.setWebViewClient(new WebViewClient() {

         public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 当开启新的页面的时候用webview来进行处理而不是用系统自带的浏览器处理
            // view.loadUrl(url);
            Trace.d("url:" + url);
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            Trace.d("shouldOverrideUrlLoading Cookies = " + CookieStr);

            //自定义处理
            if (url.startsWith("http://protocal.")) {
               //Utils.showMessage("兑换成功");
               String type = Uri.parse(url).getQueryParameter("type");
               if (type == null) {
                  type = "1";
               }
               if (type.equals("1")) {
                  //UserInfoWebViewActivity.this.finish();
               } else if (type.equals("2")) {
                  String uid = Uri.parse(url).getQueryParameter("uid");
                  String nickname = Uri.parse(url).getQueryParameter("nickname");
                  String face = Uri.parse(url).getQueryParameter("face");
                  Intent homepage_Intent =
                          new Intent(UserInfoWebViewActivity.this, HomePageActivity.class);
                  homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, uid);
                  UserInfoWebViewActivity.this.startActivity(homepage_Intent);
                  //UserInfoWebViewActivity.this.finish();
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
            Trace.d("onPageStarted");
            super.onPageStarted(view, url, favicon);
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            Trace.d("onPageStarted Cookies = " + CookieStr);
         }

         @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            Trace.d("onPageFinished Cookies = " + CookieStr);
         }
      });
      WebSettings webSettings = introduce_webview.getSettings();
      webSettings.setJavaScriptEnabled(true);
      webSettings.setDefaultTextEncodingName("gb2312");
      webSettings.setAllowFileAccess(true);
      webSettings.setDomStorageEnabled(true);
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
      } else if (introduce_webview != null && introduce_webview.canGoBack() && page_index > 0) {
         introduce_webview.goBack();
      } else {
         if (back_home) {
            startActivity(new Intent(UserInfoWebViewActivity.this, AULiveHomeActivity.class));
            this.finish();
         } else {
            this.finish();
         }
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

               /*DanmuRainListEntity danmuRainListEntity = new DanmuRainListEntity();
               danmuRainListEntity.words=callback.words;
               EventBus.getDefault().postSticky(danmuRainListEntity);
*/
               UserInfoWebViewActivity.this.startActivity(
                       new Intent(UserInfoWebViewActivity.this, AvActivity.class).putExtra(
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

   //////////////////

   //private static final int FILE_SELECT_CODE = 0;
   //private ValueCallback<Uri> mUploadMessage;
   //private class MyWebChromeClient extends WebChromeClient {
   //
   //   // For Android 3.0+
   //   public void openFileChooser(ValueCallback<Uri> uploadMsg) {
   //
   //      mUploadMessage = uploadMsg;
   //      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
   //      i.addCategory(Intent.CATEGORY_OPENABLE);
   //      i.setType("image/*");
   //      startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);
   //
   //   }
   //
   //   // For Android 3.0+
   //   public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
   //      mUploadMessage = uploadMsg;
   //      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
   //      i.addCategory(Intent.CATEGORY_OPENABLE);
   //      i.setType("*/*");
   //      startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
   //   }
   //
   //   // For Android 4.1
   //   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
   //      mUploadMessage = uploadMsg;
   //      Intent i = new Intent(Intent.ACTION_GET_CONTENT);
   //      i.addCategory(Intent.CATEGORY_OPENABLE);
   //      i.setType("image/*");
   //      startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);
   //
   //   }
   //
   //}
   //
   //private class MyWebViewClient extends WebViewClient {
   //   private Context context;
   //
   //   public MyWebViewClient(Context context) {
   //      super();
   //      this.context = context;
   //   }
   //
   //   @Override
   //   public boolean shouldOverrideUrlLoading(WebView view, String url) {
   //      view.loadUrl(url);
   //      return true;
   //   }
   //
   //   @Override
   //   public void onPageStarted(WebView view, String url, Bitmap favicon) {
   //      super.onPageStarted(view, url, favicon);
   //   }
   //
   //   @Override
   //   public void onPageFinished(WebView view, String url) {
   //      super.onPageFinished(view, url);
   //   }
   //
   //}
   //
   //// flipscreen not loading again
   //@Override
   //public void onConfigurationChanged(Configuration newConfig) {
   //   super.onConfigurationChanged(newConfig);
   //}
   //
   //@Override
   //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   //
   //   if (resultCode != RESULT_OK) {
   //      return;
   //   }
   //
   //   switch (requestCode) {
   //      case FILE_SELECT_CODE : {
   //         Uri uri = data.getData();
   //         Trace.d( "Path:" + uri.toString());
   //         mUploadMessage.onReceiveValue(uri);
   //         mUploadMessage = null;
   //      }
   //      break;
   //   }
   //}
}