package com.ilikezhibo.ggzb.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.PixelDpHelper;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import de.greenrobot.event.EventBus;

public class WebViewFragment extends BaseFragment implements OnClickListener {

   private WebView introduce_webview;
   public final static String input_url = "input_url";
   public final static String actity_name = "actity_name";
   public final static String back_home_key = "back_home";
   public final static String is_from_list_key = "is_from_list";
   private String root_url;
   public boolean mIsShow;

   @Override public void onResume() {
      super.onResume();
   }

   @Override public void onPause() {
      super.onPause();
   }

   public static String WebViewFragment_url_key="WebViewFragment_url_key";
   public static String WebViewFragment_title_key="WebViewFragment_title_key";
   public static final WebViewFragment newInstance(String title,String url) {
      WebViewFragment f = new WebViewFragment();
      Bundle bdl = new Bundle(2);
      bdl.putString(WebViewFragment_title_key, title);
      bdl.putString(WebViewFragment_url_key, url);
      f.setArguments(bdl);
      return f;
   }

   View rootView;

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);

      rootView = inflater.inflate(R.layout.authorize_webview, null);
      Button rl_back = (Button) rootView.findViewById(R.id.back);
      rl_back.setVisibility(View.VISIBLE);

      rl_back.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            closeActFragment();
         }
      });

      root_url  = getArguments().getString(WebViewFragment_url_key);
      String name = getArguments().getString(WebViewFragment_title_key);
      TextView tv_title = (TextView) rootView.findViewById(R.id.title);
      if (name != null && !name.equals("")) {
         tv_title.setText(name);
      }
      //设置webview 共享httpClient的cookie
      String mCookie =
              SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.COOKIE_KEY, "");
      Trace.d("webview cookie:" + mCookie);
      //CookieSyncManager.createInstance(this.getActivity());
      //CookieManager cookieManager = CookieManager.getInstance();
      //cookieManager.setAcceptCookie(true);
      //cookieManager.removeSessionCookie();
      //cookieManager.setCookie(root_url, mCookie);
      //if (Build.VERSION.SDK_INT < 21) {
      //   CookieSyncManager.getInstance().sync();
      //} else {
      //   CookieManager.getInstance().flush();
      //}

      WebViewFileUtils.syncCookie(root_url,mCookie,WebViewFragment.this.getContext());


      introduce_webview = (WebView) rootView.findViewById(R.id.introduce_webview);
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
                          new Intent(WebViewFragment.this.getActivity(), HomePageActivity.class);
                  homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, uid);
                  WebViewFragment.this.startActivity(homepage_Intent);
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
         closeActFragment();
      }
      if (!root_url.startsWith("http://")&&!root_url.startsWith("https://")) {
         root_url = "http://" + root_url;
      }
      introduce_webview.loadUrl(root_url);

      return rootView;
   }


   @Override public void onClick(View v) {
      switch (v.getId()) {

         case R.id.back:

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


   //进入房间接口
   private void enterRoom(String liveuid, final String userid, final String uid, final String face,
                          final String nickname) {
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
                 UrlHelper.enterRoomUrl + "?liveuid=" + liveuid + "&userid=" + userid);
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

//               DanmuRainListEntity danmuRainListEntity = new DanmuRainListEntity();
//               danmuRainListEntity.words=callback.words;
//               EventBus.getDefault().postSticky(danmuRainListEntity);

               WebViewFragment.this.startActivity(
                       new Intent(WebViewFragment.this.getActivity(), AvActivity.class).putExtra(
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

   public void showActFragment(BaseFragmentActivity context) {
      mIsShow = true;
      context.getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.mManagerList, this)
              .commitAllowingStateLoss();
      View fragment_container = context.findViewById(R.id.mManagerList);
      fragment_container.setVisibility(View.VISIBLE);
      ViewAnimator.animate(fragment_container)
              .translationX(PixelDpHelper.dip2px(context, 500), 0)
              .duration(300)

              .onStop(new AnimationListener.Stop() {
                 @Override public void onStop() {

                 }
              }).start();
   }

   public void closeActFragment() {
      mIsShow = false;
      AvActivity avActivity = (AvActivity) WebViewFragment.this.getActivity();
      View fragment_container = avActivity.findViewById(R.id.mManagerList);
      ViewAnimator.animate(fragment_container)
              .translationX(0, PixelDpHelper.dip2px(avActivity, 500))
              .duration(300)

              .onStop(new AnimationListener.Stop() {
                 @Override public void onStop() {
                    AvActivity avActivity = (AvActivity) WebViewFragment.this.getActivity();
                    avActivity.findViewById(R.id.mManagerList).setVisibility(View.GONE);
                 }
              }).start();
   }

}