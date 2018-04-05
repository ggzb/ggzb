package com.ilikezhibo.ggzb.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.AppConstants;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.JsonParser;
import com.jack.utils.MobileConfig;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.login.entity.QQTokenEntity;
import com.ilikezhibo.ggzb.login.entity.WeiXinTokenEntity;
import com.ilikezhibo.ggzb.login.weibo.AccessTokenKeeper;
import com.ilikezhibo.ggzb.login.weibo.Constants;
import com.ilikezhibo.ggzb.register.RegisterActivity;
import com.ilikezhibo.ggzb.register.RegisterNextActivity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import tinker.android.util.TinkerManager;

public class LoginActivity extends BaseFragmentActivity implements OnClickListener, TitleListener {
   public static final String INTENT_IS_CHANGE_PHONE_KEY = "INTENT_IS_CHANGE_PHONE_KEY";
   private boolean isChangePhone = false;

   private EditText editAccount;
   private EditText editpass;
   private Button loginButton;
   private Button regiButton;
   private TextView forget_password;
   private TextView fast_register;

   private boolean back_home = true;

   public static String back_home_key = "back_home_key";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      overridePendingTransition(0,0);
   }

   @Override protected void setContentView() {
      isChangePhone = getIntent().getBooleanExtra(INTENT_IS_CHANGE_PHONE_KEY, false);
      setContentView(R.layout.activity_login_new);

     /* Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      TextView tv_title = (TextView) this.findViewById(R.id.title);
      tv_title.setText("登录");

      TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
      topRightBtn.setText("注册");
      topRightBtn.setOnClickListener(this);
       */
      back_home = getIntent().getBooleanExtra(back_home_key, true);
   }

   @Override protected void initializeViews() {
      editAccount = (EditText) findViewById(R.id.editAccount);
      editpass = (EditText) findViewById(R.id.editPassword);
      loginButton = (Button) findViewById(R.id.buttonLogin);
      if (isChangePhone) {
         loginButton.setText("确认");
      }
      regiButton = (Button) findViewById(R.id.buttonReg);
      regiButton.setVisibility(View.GONE);
      fast_register = (TextView) findViewById(R.id.fast_register);
      fast_register.setOnClickListener(this);

      forget_password = (TextView) findViewById(R.id.forget_password);
      forget_password.setOnClickListener(this);
      if (isChangePhone) {
         forget_password.setVisibility(View.GONE);
      }

//      LinearLayout layout_weixin = (LinearLayout) findViewById(R.id.layout_one);
//      layout_weixin.setOnClickListener(this);
//
//      LinearLayout layout_qq = (LinearLayout) findViewById(R.id.layout_two);
//      layout_qq.setOnClickListener(this);
//
//      LinearLayout layout_sina = (LinearLayout) findViewById(R.id.layout_three);
//      layout_sina.setOnClickListener(this);

      //EventBus.getDefault().register(this, "weixn_register", WeiXinRegEvent.class);
      EventBus.getDefault().register(this);
   }

   @Override protected void initializeData() {
      loginButton.setOnClickListener(this);
      regiButton.setOnClickListener(this);
      String phone =
          SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.LOGIN_USER_PHONE, "");
      if (!isChangePhone) {
         editAccount.setText(phone);
      }
      if (!phone.equals("")) {
         editpass.requestFocus();
      }
   }

   @Override public void onBack() {

   }

   @Override public void onTopRightEvent() {
      if (SharedPreferenceTool.getInstance()
          .getBoolean(SharedPreferenceTool.REG_FINISH_FIRST, false)) {
         Intent regIntent = new Intent(LoginActivity.this, RegisterNextActivity.class);
         startActivity(regIntent);
      } else {
         addReg();
      }
   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }

      switch (v.getId()) {
         case R.id.buttonLogin:
            checkField();
            break;
         case R.id.fast_register:
            startActivity(new Intent(this, RegisterNextActivity.class));
            break;
         case R.id.topRightBtn:
            startActivity(new Intent(this, RegisterNextActivity.class));
            // startActivity(new Intent(this, StepMainActivity.class));
            break;
         case R.id.forget_password:
            Intent intent = new Intent(this, ForgetPassWord.class);
            startActivity(intent);
            break;
         case R.id.layout_three:
            startProgressDialog();
            doWeiboLogin();
            break;
         case R.id.layout_two:
            startProgressDialog();
            qqLogin();
            break;
         case R.id.layout_one:
            startProgressDialog();
            IWXAPI wxApi = WXAPIFactory.createWXAPI(this, AppConstants.WEI_XIN_ID, true);
            wxApi.registerApp(AppConstants.WEI_XIN_ID);

            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            wxApi.sendReq(req);
            break;

         case R.id.back:
            // 如果AULiveHomeActivity已经打开,则关闭它
            if(AULiveHomeActivity.auLiveHomeActivity != null) {
               AULiveHomeActivity.auLiveHomeActivity.finish();
            }
            this.finish();
            break;
      }
   }

   @Override protected void onResume() {
      super.onResume();
      stopProgressDialog();
   }

   private void checkField() {
      // 正则表达匹配手机
      if (editAccount.getText() == null || editAccount.getText().equals("") || !matchPhone(
          editAccount.getText().toString())) {
         Utils.showMessage("请输入手机号");
         editAccount.requestFocus();
         return;
      } else if (editpass.getText() == null
          || editpass.getText().equals("")
          || editpass.getText().length() < 6) {
         Utils.showMessage("请输入6位以上密码");
         editpass.requestFocus();
         return;
      }
      if (isChangePhone) {
         doModify();
      } else {
         doLogin(editAccount.getText().toString(), editpass.getText().toString());
      }
   }

   private void doModify() {
      showProgressDialog("正在修改，请稍候...");
      RequestInformation request = new RequestInformation(UrlHelper.MY_BASIC_PROFILE_URL,
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("phone", editAccount.getText().toString());
      request.addPostParams("pwd", Utils.encryption(editpass.getText().toString()));

      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }

         @Override public void onCallback(String callback) {
            cancelProgressDialog();
            try {
               JSONObject json = new JSONObject(callback);
               if (json.optInt("stat") == 200) {
                  SharedPreferenceTool.getInstance()
                      .saveString(SharedPreferenceTool.LOGIN_USER_PHONE,
                          editAccount.getText().toString());
                  LoginActivity.this.finish();
               } else {
                  Utils.showMessage(json.optString("msg"));
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
      });
      request.execute();
   }

   private boolean isReg = false;

   private void addReg() {
      if (isReg) {
         return;
      }
      isReg = true;
      RequestInformation request =
          new RequestInformation(UrlHelper.ADD_REG_URL, RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            isReg = false;
         }

         @Override public void onCallback(String callback) {
            isReg = false;
            try {
               JSONObject json = new JSONObject(callback);
               if (json.optInt("stat") == 200) {
                  Trace.d("reg uid:" + json.optString("uid"));

                  SharedPreferenceTool.getInstance()
                      .saveString(SharedPreferenceTool.REG_UID, json.optString("uid"));

                  Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                  regIntent.putExtra("first_time", true);
                  startActivity(regIntent);
                  // OldLoginActivity.this.finish();
               } else {
                  Utils.showMessage(json.optString("msg"));
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
      });
      request.execute();
   }

   private void doLogin(final String account, String pwd) {
      showProgressDialog("正在登录" + Utils.trans(R.string.app_name) + "，请稍候...");
      RequestInformation request =
          new RequestInformation(UrlHelper.LOGIN_URL, RequestInformation.REQUEST_METHOD_POST);
      try {
         MobileConfig config = MobileConfig.getMobileConfig(this);
         request.addPostParams("account", account);
         request.addPostParams("pwd", Utils.encryption(pwd));
         request.addPostParams("udid", config.getIemi());
         request.addPostParams("system_name", config.getOS());
         request.addPostParams("system_version", config.getMobileOsVersion());
         request.addPostParams("platform", URLEncoder.encode(config.getMobileModel(), "utf-8"));
         request.addPostParams("carrier", URLEncoder.encode(config.getSimOperatorName(), "utf-8"));
         request.addPostParams("app_version", config.getPkgVerCode() + "");
         request.addPostParams("app_channel", config.getCurrMarketName());

         AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
         if (application.getAddress() != null) {
            request.addPostParams("lat", application.getLatitude() + "");
            request.addPostParams("lng", application.getLongitude() + "");
            //request.addPostParams("city", URLEncoder.encode(application.getCity(), "utf-8"));
            boolean is_show = SharedPreferenceTool.getInstance()
                .getBoolean(SharedPreferenceTool.NO_LOCATION_KEY, true);
            try {
               if (is_show) {
                  request.addPostParams("city", URLEncoder.encode(application.getCity(), "utf-8"));
                  request.addPostParams("prov",
                      URLEncoder.encode(application.getProvince(), "utf-8"));
               } else {
                  request.addPostParams("city", URLEncoder.encode("火星", "utf-8"));
                  request.addPostParams("prov", URLEncoder.encode("火星", "utf-8"));
               }
            } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
            }
         }
      } catch (UnsupportedEncodingException e1) {
         e1.printStackTrace();
      }

      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            cancelProgressDialog();
            if (callback == null) {
               Utils.showMessage("获取数据失败");
               return;
            }

            if (callback.getStat() == 200) {
               if (callback.getUserinfo() != null) {
//                  AULiveApplication.setUserInfo(callback.getUserinfo());
                  LoginUserEntity loginUserEntity = callback.getUserinfo();
                  AULiveApplication.setUserInfo(loginUserEntity);


                  //存userSig
                  AULiveApplication.setMyselfUserInfo(callback.getUserinfo().getUid(),
                          callback.getUserinfo().getNickname(), callback.getUserinfo().getFace(),
                          callback.getUserSig(), callback.getUserinfo().getMsg_tip(), callback.getUserinfo().getPrivate_chat_status());

                  // 缓存手机号，用户名
                  SharedPreferenceTool.getInstance()
                      .saveString(SharedPreferenceTool.LOGIN_USER_PHONE, account);



                  // SocketManager.getInstance(LoginActivity.this)
                  // .shutdown();
                  //
                  // DealSocketMsg dealSocketMsg = new DealSocketMsg();
                  // dealSocketMsg.setNotifyContext(LoginActivity.this);
                  // // dealSocketMsg.setListener(this);
                  // dealSocketMsg.startSocket();
               }

               // unBindService();
               Trace.d("userinfo:" + JsonParser.serializeToJson(callback.getUserinfo()));
               //连接融信
               connect(callback.getUserinfo().getIm_token());
               //bindService();

               MainActivity.getAttenList(AULiveApplication.getUserInfo().getUid());

               startActivity(new Intent(LoginActivity.this, AULiveHomeActivity.class));
               LoginActivity.this.finish();
               //if (back_home) {
               //	startActivity(new Intent(LoginActivity.this,
               //			AULiveHomeActivity.class));
               //	LoginActivity.this.finish();
               //} else {
               //	LoginActivity.this.finish();
               //}
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }

   /**
    * 建立与融云服务器的连接
    */
   private void connect(String token) {

      if (getApplicationInfo().packageName.equals(
          AULiveApplication.getCurProcessName(getApplicationContext()))) {
         if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().logout();
         }
         /**
          * IMKit SDK调用第二步,建立与服务器的连接
          */
         RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override public void onTokenIncorrect() {

               Trace.d("LoginActivity--onTokenIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override public void onSuccess(String userid) {

               Trace.d("LoginActivity --onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override public void onError(RongIMClient.ErrorCode errorCode) {

               Trace.d("LoginActivity--onError" + errorCode);
            }
         });
      }
   }

   private boolean matchPhone(String text) {
      if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
         return true;
      }
      return false;
   }

   @Override protected void onDestroy() {
      super.onDestroy();
      // ****切换帐号时偶尔出现progressDialog没有dismiss的bug
      if(progressDialog != null && progressDialog.isShowing()) {
         progressDialog.dismiss();
      }
      EventBus.getDefault().unregister(this);
      Utils.unbindLayout(findViewById(R.id.rootLayout));
      // unBindService();
   }

   public void onEvent(WeiXinRegEvent token_event) {
      Trace.d("LoginActivity onEvent(WeiXinRegEvent token_event)");
      weixn_register(token_event);
   }

   // /关于第三方登录
   // 微信回调处理
   public void weixn_register(WeiXinRegEvent token_event) {

      final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
          + AppConstants.WEI_XIN_ID
          + "&secret="
          + AppConstants.WEI_XIN_SCRET
          + "&code="
          + token_event.token
          + "&grant_type=authorization_code";

      //RequestInformation request =
      //    new RequestInformation(url, RequestInformation.REQUEST_METHOD_GET);
      //
      //Trace.d("token url:" + url);
      //request.setCallback(new JsonCallback<WeiXinTokenEntity>() {
      //
      //   @Override public void onCallback(WeiXinTokenEntity callback) {
      //
      //      if (callback != null && callback.getAccess_token() != null) {
      //         register_next(callback.getAccess_token());
      //      }
      //   }
      //
      //   @Override public void onFailure(AppException e) {
      //   }
      //}.setReturnType(WeiXinTokenEntity.class));
      //request.execute();
      new Thread(new Runnable() {
         @Override public void run() {
            try {
               HttpGet httpRequest = new HttpGet(url);// 建立http get联机
               HttpResponse httpResponse =
                   new DefaultHttpClient().execute(httpRequest);   //取得http响应
               if (httpResponse.getStatusLine().getStatusCode() == 200) {
                  String strResult =
                      EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);   //获取字符串

                  Trace.d("WeiXinTokenEntity callback1:" +  strResult.toString());
                  WeiXinTokenEntity weiXinTokenEntity =
                      JsonParser.deserializeByJson(strResult, WeiXinTokenEntity.class);
                  if (weiXinTokenEntity != null && weiXinTokenEntity.getAccess_token() != null) {
                     register_next(weiXinTokenEntity.getAccess_token());
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }).start();
   }

   private void register_next(final String token_event) {

      String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
          + token_event
          + "&openid="
          + AppConstants.WEI_XIN_ID
          + "&secret="
          + AppConstants.WEI_XIN_SCRET
          + "&lang=zh_CN";

      //RequestInformation request =
      //    new RequestInformation(url, RequestInformation.REQUEST_METHOD_GET);

      Trace.d("token url:" + url);
      //request.setCallback(new JsonCallback<WeiXinTokenEntity>() {
      //
      //   @Override public void onCallback(WeiXinTokenEntity callback) {
      //      Trace.d("WeiXinTokenEntity callback:" + callback.toString());
      //      doOtherLogin(callback.getSex(), callback.getNickname(), callback.getHeadimgurl(),
      //          "weixin", callback.getUnionid());
      //   }
      //
      //   @Override public void onFailure(AppException e) {
      //   }
      //}.setReturnType(WeiXinTokenEntity.class));

      //request.setCallback(new StringCallback() {
      //
      //   @Override public void onFailure(AppException e) {
      //
      //   }
      //
      //   @Override public void onCallback(String callback) {
      //      Trace.d("WeiXinTokenEntity callback1:" + callback.toString());
      //      try {
      //         //去乱码
      //         String result = new String(callback.getBytes(), "utf-8");
      //         WeiXinTokenEntity weiXinTokenEntity =
      //             JsonParser.deserializeByJson(result, WeiXinTokenEntity.class);
      //         doOtherLogin(weiXinTokenEntity.getSex(), weiXinTokenEntity.getNickname(),
      //             weiXinTokenEntity.getHeadimgurl(), "weixin", weiXinTokenEntity.getUnionid());
      //
      //         Trace.d("WeiXinTokenEntity callback2:" + result.toString());
      //      } catch (UnsupportedEncodingException e) {
      //         e.printStackTrace();
      //      }
      //   }
      //});
      //
      //request.execute();
      try {
         HttpGet httpRequest = new HttpGet(url);// 建立http get联机
         HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);   //取得http响应
         if (httpResponse.getStatusLine().getStatusCode() == 200) {
            final String strResult =
                EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);   //获取字符串

            Trace.d("WeiXinTokenEntity callback2:" + strResult.toString());

            LoginActivity.this.runOnUiThread(new Runnable() {
               @Override public void run() {
                  WeiXinTokenEntity weiXinTokenEntity =
                      JsonParser.deserializeByJson(strResult, WeiXinTokenEntity.class);
                  doOtherLogin(weiXinTokenEntity.getSex(), weiXinTokenEntity.getNickname(),
                      weiXinTokenEntity.getHeadimgurl(), "weixin", weiXinTokenEntity.getOpenid(),
                      token_event, weiXinTokenEntity.getUnionid());
               }
            });
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // if (back_home) {
   // startActivity(new Intent(LoginActivity.this,
   // AULiveHomeActivity.class));
   // } else {
   // LoginActivity.this.finish();
   // }
   Tencent mTencent = null;
   String openidString = null;
   IUiListener tencentLoginListener;
   String qq_access_token;

   // QQ授权登陆
   public void qqLogin() {
      mTencent = Tencent.createInstance(AppConstants.QQ_APP_ID, this.getApplicationContext());
      if (!mTencent.isSessionValid()) {

         tencentLoginListener = new IUiListener() {

            @Override public void onError(UiError arg0) {
               Trace.d("qqLogin onError(" + arg0.errorDetail);
            }

            @Override public void onComplete(Object response) {
               JSONObject jsonObject = (JSONObject) response;
               try {
                  openidString = ((JSONObject) response).getString("openid");
                  Trace.d("openid:" + openidString);

                  String token =
                      jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                  String expires =
                      jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                  String openId =
                      jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                  if (!TextUtils.isEmpty(token)
                      && !TextUtils.isEmpty(expires)
                      && !TextUtils.isEmpty(openId)) {
                     mTencent.setAccessToken(token, expires);
                     mTencent.setOpenId(openId);
                  }
               } catch (JSONException e) {
                  e.printStackTrace();
               }
               /**
                * 到此已经获得OpneID以及其他你想获得的内容了
                * QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
                * sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
                * 如何得到这个UserInfo类呢？
                */
               QQToken qqToken = mTencent.getQQToken();
               Trace.d("qqToken.getAccessToken():" + qqToken.getAccessToken());
               qq_access_token = qqToken.getAccessToken();
               com.tencent.connect.UserInfo info =
                   new com.tencent.connect.UserInfo(getApplicationContext(), qqToken);
               // 这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON

               info.getUserInfo(new IUiListener() {

                  @Override public void onError(UiError arg0) {

                  }

                  @Override public void onComplete(Object userInfo1) {
                     JSONObject userInfo = (JSONObject) userInfo1;
                     QQTokenEntity qqTokenEntity =
                         JsonParser.deserializeByJson(userInfo.toString(), QQTokenEntity.class);
                     Trace.d(userInfo.toString());
                     String sex1 = qqTokenEntity.getGender();
                     String sex = null;
                     if (sex1 != null && sex1.equals("男")) {
                        sex = "1";
                     } else {
                        sex = "2";
                     }
                     // 获取以后要logout，不然不可以退出后再登录
                     mTencent.logout(LoginActivity.this);

                     doOtherLogin(sex, qqTokenEntity.getNickname(),
                         qqTokenEntity.getFigureurl_qq_2(), "qq", openidString, qq_access_token,
                         "");
                  }

                  @Override public void onCancel() {

                  }
               });
            }

            @Override public void onCancel() {

            }
         };
         mTencent.login(this, "all", tencentLoginListener);
      }
   }

   //由于android之前把unionid当成openid处理了
   private void doOtherLogin(String sex, String nickname, String face, String type,
       String unique_flag, String access_token, String unionid) {
      if (nickname == null || sex == null || face == null || type == null) {
         Utils.showCroutonText(LoginActivity.this, "昵称与性别不能为空");
         return;
      }
      showProgressDialog("正在登录" + Utils.trans(R.string.app_name) + "，请稍候...");
      String url = null;
      RequestInformation request = null;
      MobileConfig config = MobileConfig.getMobileConfig(this);
      try {
         url = UrlHelper.URL_HEAD
             + "/index/oauth?udid="
             + config.getIemi()
             + "&sex="
             + sex
             + "&nickname="
             + URLEncoder.encode(nickname, "utf-8")
             + "&face="
             + URLEncoder.encode(face, "utf-8")
             + "&pf="
             + type
             + "&openid="
             + URLEncoder.encode(unique_flag, "utf-8")
             + "&birthday="
             + "19950101"
             + "&app_version="
             + config.getPkgVerCode()
             + "&access_token="
             + access_token
             + "&unionid="
             + unionid;

         AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
         if (application.getAddress() != null) {
            url = url
                + "&lat="
                + application.getLatitude()
                + "&lng="
                + application.getLongitude()
                + "&city="
                + URLEncoder.encode(application.getCity(), "utf-8");
         }

         Trace.d("doOtherLogin url:" + url);
         request = new RequestInformation(url, RequestInformation.REQUEST_METHOD_POST);
         request.addPostParams("udid", config.getIemi());
         request.addPostParams("sex", sex);
         request.addPostParams("nickname", nickname);
         request.addPostParams("face", URLEncoder.encode(face, "utf-8"));
         request.addPostParams("pf", type);
         request.addPostParams("openid", URLEncoder.encode(unique_flag, "utf-8"));
         request.addPostParams("birthday", "19950101");
         request.addPostParams("app_version", config.getPkgVerCode() + "");
         request.addPostParams("access_token", access_token + "");
         request.addPostParams("unionid", URLEncoder.encode(unionid, "utf-8"));

         if (application.getAddress() != null) {
            request.addPostParams("lat", application.getLatitude() + "");
            request.addPostParams("lng", application.getLongitude() + "");
            //request.addPostParams("city", URLEncoder.encode(application.getCity(), "utf-8"));
            boolean is_show = SharedPreferenceTool.getInstance()
                .getBoolean(SharedPreferenceTool.NO_LOCATION_KEY, true);
            try {
               if (is_show) {
                  request.addPostParams("city", URLEncoder.encode(application.getCity(), "utf-8"));
                  request.addPostParams("prov",
                      URLEncoder.encode(application.getProvince(), "utf-8"));
               } else {
                  request.addPostParams("city", URLEncoder.encode("火星", "utf-8"));
                  request.addPostParams("prov", URLEncoder.encode("火星", "utf-8"));
               }
            } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
            }
         }
      } catch (UnsupportedEncodingException e1) {
         e1.printStackTrace();
      }
      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            cancelProgressDialog();
            if (callback == null) {
               Utils.showMessage("获取数据失败");
               return;
            }

            if (callback.getStat() == 200) {
               if (callback.getUserinfo() != null) {
                  AULiveApplication.setUserInfo(callback.getUserinfo());

                  AULiveApplication.setMyselfUserInfo(callback.getUserinfo().getUid(),
                      callback.getUserinfo().getNickname(), callback.getUserinfo().getFace(),
                      callback.getUserSig(),callback.getUserinfo().getMsg_tip(),callback.getUserinfo().getPrivate_chat_status());
                  // 缓存手机号，用户名
                  // SharedPreferenceTool.getInstance().saveString(
                  // SharedPreferenceTool.LOGIN_USER_PHONE, account);

                  // DealSocketMsg dealSocketMsg = new DealSocketMsg();
                  // dealSocketMsg.setNotifyContext(LoginActivity.this);
                  // // dealSocketMsg.setListener(this);
                  // dealSocketMsg.startSocket();

               }
               Trace.d("connect getIm_token:" + callback.getUserinfo().getIm_token());
               //连接融信
               connect(callback.getUserinfo().getIm_token());
               //bindService();

               MainActivity.getAttenList(AULiveApplication.getUserInfo().getUid());

               //停止提示
               stopProgressDialog();
               startActivity(new Intent(LoginActivity.this, AULiveHomeActivity.class));
               LoginActivity.this.finish();
            } else {
               cancelProgressDialog();
               //Utils.showMessage(callback.getMsg());
               Utils.showCroutonText(LoginActivity.this, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(com.ilikezhibo.ggzb.entity.UserInfo.class));
      request.execute();
   }

   // /微博第三方登录相关

   private AuthInfo mAuthInfo;

   /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
   private Oauth2AccessToken mAccessToken;

   /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
   private SsoHandler mSsoHandler;

   private void doWeiboLogin() {
      // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
      mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
      mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

      mSsoHandler.authorize(new AuthListener());
   }

   /**
    * 当 SSO 授权 Activity 退出时，该函数被调用。
    *
    * @see {@link Activity#onActivityResult}
    */
   @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN
          || requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
         Tencent.onActivityResultData(requestCode, resultCode, data, tencentLoginListener);
      }
      // SSO 授权回调
      // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
      if (mSsoHandler != null) {
         mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
      }
   }

   /**
    * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
    * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
    * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
    * SharedPreferences 中。
    */
   class AuthListener implements WeiboAuthListener {

      @Override public void onComplete(Bundle values) {
         // 从 Bundle 中解析 Token
         mAccessToken = Oauth2AccessToken.parseAccessToken(values);
         // 从这里获取用户输入的 电话号码信息
         String phoneNum = mAccessToken.getPhoneNum();
         if (mAccessToken.isSessionValid()) {

            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
            Utils.showMessage("授权成功");

            UsersAPI mUsersAPI = new UsersAPI(LoginActivity.this, Constants.APP_KEY, mAccessToken);
            // String uid = mAccessToken.getUid();
            long uid = Long.parseLong(mAccessToken.getUid());
            mUsersAPI.show(uid, mListener);
         } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = "授权失败";
            if (!TextUtils.isEmpty(code)) {
               message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
         }
      }

      @Override public void onCancel() {
         Utils.showMessage("取消授权");
      }

      @Override public void onWeiboException(WeiboException e) {
         Utils.showMessage("授权出错");
      }
   }

   /**
    * 微博 OpenAPI 回调接口。
    */
   private RequestListener mListener = new RequestListener() {
      @Override public void onComplete(String response) {
         if (!TextUtils.isEmpty(response)) {
            Trace.d(response);
            // 调用 User#parse 将JSON串解析成User对象
            User user = User.parse(response);
            if (user != null) {
               // Toast.makeText(LoginActivity.this,
               // "获取User信息成功，用户昵称：" + user.screen_name,
               // Toast.LENGTH_LONG).show();
               String sex1 = user.gender;
               String sex = null;
               if (sex1 != null && sex1.equals("m")) {
                  sex = "1";
               } else {
                  sex = "2";
               }
               doOtherLogin(sex, user.name, user.avatar_large, "weibo", user.idstr,
                   mAccessToken.getToken(), "");
            } else {
               Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
            }
         }
      }

      @Override public void onWeiboException(WeiboException e) {
         Trace.d(e.getMessage());
         ErrorInfo info = ErrorInfo.parse(e.getMessage());
         Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
      }
   };

   private CustomProgressDialog progressDialog = null;

   private void startProgressDialog() {
      try {
         if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("请稍后");
         }

         progressDialog.show();
      } catch (Exception e) {
      }
   }

   private void stopProgressDialog() {
      try {
         if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
         }
      } catch (Exception e) {
      }
   }

   @Override
   public void onBackPressed() {
      // 如果aulivehome打开过  则关闭
      if(AULiveHomeActivity.auLiveHomeActivity != null) {
         AULiveHomeActivity.auLiveHomeActivity.finish();
      }
      finish();
   }

   @Override protected void exitAnim() {

   }
}
