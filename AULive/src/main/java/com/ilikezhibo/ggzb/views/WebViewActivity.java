package com.ilikezhibo.ggzb.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.umeng.analytics.MobclickAgent;

public class WebViewActivity extends BaseActivity implements OnClickListener {

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

      //CookieSyncManager.createInstance(this);
      //CookieManager cookieManager = CookieManager.getInstance();
      //cookieManager.removeSessionCookie();
      //cookieManager.setCookie(UrlHelper.URL_HEAD, mCookie);
      //CookieSyncManager.getInstance().sync();

      WebViewFileUtils.syncCookie(root_url,mCookie,WebViewActivity.this);

      introduce_webview = (WebView) findViewById(R.id.introduce_webview);
      introduce_webview.setWebChromeClient(new WebChromeClient());
      introduce_webview.setDownloadListener(new MyWebViewDownLoadListener());
      introduce_webview.setWebViewClient(new WebViewClient() {

         public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 当开启新的页面的时候用webview来进行处理而不是用系统自带的浏览器处理
            // view.loadUrl(url);
            // Trace.d("url:" + url);
            //
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
      // introduce_webview.setOnClickListener(this);

      if (root_url == null || root_url.equals("")) {
         this.finish();
      }
      if (!root_url.startsWith("http://")&&!root_url.startsWith("https://")) {
         root_url = "http://" + root_url;
      }
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
            startActivity(new Intent(WebViewActivity.this, AULiveHomeActivity.class));
            this.finish();
         } else {
            this.finish();
         }
      }
   }
}
