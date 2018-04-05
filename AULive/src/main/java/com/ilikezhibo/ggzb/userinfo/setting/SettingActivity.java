package com.ilikezhibo.ggzb.userinfo.setting;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.blacklist.BlackListActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.login.GetPassWordByPhoneActivity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.userinfo.AboatActivity;
import com.ilikezhibo.ggzb.userinfo.SuggestActivity;
import com.ilikezhibo.ggzb.userinfo.VideoChatMgmtActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import tinker.android.util.TinkerManager;

public class SettingActivity extends BaseFragmentActivity implements OnClickListener {

   @Override protected void setContentView() {
      setContentView(R.layout.jiesihuo_my_info_setting);

      init();
   }

   private void init() {
      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setVisibility(View.VISIBLE);
      rl_back.setOnClickListener(this);

      TextView title = (TextView) this.findViewById(R.id.title);
      title.setOnClickListener(this);
      title.setText("设置");

      Button quit_button = (Button) this.findViewById(R.id.quit_button);
      quit_button.setOnClickListener(this);
   }

   private CheckBox new_msg_checkbox;
   private CheckBox wuyao_checkbox;
   private CheckBox privisy_checkbox;

   @Override protected void initializeViews() {

      new_msg_checkbox = (CheckBox) findViewById(R.id.new_msg_checkbox);
      new_msg_checkbox.setOnClickListener(this);
      new_msg_checkbox.setChecked(SharedPreferenceTool.getInstance()
          .getBoolean(SharedPreferenceTool.NEW_MSG_NOTICE_KEY, true));

      wuyao_checkbox = (CheckBox) findViewById(R.id.wuyao_checkbox);
      wuyao_checkbox.setOnClickListener(this);
      wuyao_checkbox.setChecked(SharedPreferenceTool.getInstance()
          .getBoolean(SharedPreferenceTool.NO_MSG_RECIEVED_KEY, false));

      privisy_checkbox = (CheckBox) findViewById(R.id.privisy_checkbox);
      privisy_checkbox.setOnClickListener(this);
      privisy_checkbox.setChecked(SharedPreferenceTool.getInstance()
          .getBoolean(SharedPreferenceTool.NO_LOCATION_KEY, true));

      privisy_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            changeLocation(isChecked);
         }
      });
      //关于
      RelativeLayout aboat_ly = (RelativeLayout) findViewById(R.id.aboat_ly);
      aboat_ly.setOnClickListener(this);

      //反馈
      RelativeLayout reply_ly = (RelativeLayout) findViewById(R.id.reply_ly);
      reply_ly.setOnClickListener(this);

      RelativeLayout password_modify_layout =
          (RelativeLayout) findViewById(R.id.password_modify_layout);
      password_modify_layout.setOnClickListener(this);

      // >视频聊天管理
      RelativeLayout rl_videochat_managerment =
              (RelativeLayout) findViewById(R.id.rl_videochat_managerment);
      // >如果是性别为男,则隐藏视频管理
      //if(AULiveApplication.getUserInfo().getSex() == 1) {
      //   rl_videochat_managerment.setVisibility(View.GONE);
      //}
      rl_videochat_managerment.setOnClickListener(this);

      RelativeLayout blacklist_layout = (RelativeLayout) findViewById(R.id.blacklist_layout);
      blacklist_layout.setOnClickListener(this);

//      findViewById(R.id.problem_layout).setOnClickListener(this);

      TextView version_tv = (TextView) findViewById(R.id.version_tv);
      PackageInfo pi = getPackageInfo(SettingActivity.this);
      if (pi != null) {
         version_tv.setText("版本号:" + pi.versionName);
      }
   }

   //版本号
   public static int getVersionCode(Context context) {
      return getPackageInfo(context).versionCode;
   }

   private static PackageInfo getPackageInfo(Context context) {
      PackageInfo pi = null;

      try {
         PackageManager pm = context.getPackageManager();
         pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

         return pi;
      } catch (Exception e) {
         e.printStackTrace();
      }

      return pi;
   }

   @Override protected void initializeData() {

   }

   @Override protected void onPause() {

      super.onPause();
   }

   @Override protected void onResume() {

      super.onResume();
   }

   @Override protected void onDestroy() {

      super.onDestroy();
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {

         case R.id.aboat_ly:
            Intent intent_aboat = new Intent(this, AboatActivity.class);
            startActivity(intent_aboat);
            break;
         case R.id.reply_ly:
            Intent intent_reply_ly = new Intent(this, SuggestActivity.class);
            startActivity(intent_reply_ly);
            break;
         case R.id.quit_button:
            exitApp();
            break;
         case R.id.back:
            this.finish();
            break;
         case R.id.password_modify_layout:
            Intent intent1 = new Intent(this, GetPassWordByPhoneActivity.class);
            startActivity(intent1);
            break;

         case R.id.blacklist_layout:
            Intent intent2 = new Intent(this, BlackListActivity.class);
            startActivity(intent2);
            break;
         case R.id.new_msg_checkbox:
            SharedPreferenceTool.getInstance()
                .saveBoolean(SharedPreferenceTool.NEW_MSG_NOTICE_KEY, new_msg_checkbox.isChecked());
            break;
         case R.id.wuyao_checkbox:
            SharedPreferenceTool.getInstance()
                .saveBoolean(SharedPreferenceTool.NO_MSG_RECIEVED_KEY, wuyao_checkbox.isChecked());
            break;
         case R.id.privisy_checkbox:

            break;
         // >视频聊天管理
         case R.id.rl_videochat_managerment:
            Intent intent_videochat = new Intent(this, VideoChatMgmtActivity.class);
            startActivity(intent_videochat);
            break;
//         case R.id.problem_layout:
//            Intent intent_problem = new Intent(this, UserInfoWebViewActivity.class);
//            intent_problem.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "faq/index");
//            intent_problem.putExtra(WebViewActivity.back_home_key, false);
//            intent_problem.putExtra(WebViewActivity.actity_name, "常见问题");
//            startActivity(intent_problem);
//            break;
      }
   }

   private CustomDialog userBlackDialog;

   private void exitApp() {
      if (userBlackDialog == null) {
         userBlackDialog = new CustomDialog(SettingActivity.this, new CustomDialogListener() {

            @Override public void onDialogClosed(int closeType) {
               switch (closeType) {
                  case CustomDialogListener.BUTTON_POSITIVE:
                     // 调用服务器清除cookie
                     doQuit();
                     break;
               }
            }
         });

         userBlackDialog.setCustomMessage("退出后,你将不能及时接收到信息,是否退出?");
         userBlackDialog.setCancelable(true);
         userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
      }

      if (null != userBlackDialog) {
         userBlackDialog.show();
      }
   }

   private void doQuit() {
      RequestInformation request =
          new RequestInformation(UrlHelper.EXIT_APP_URL, RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               AULiveApplication.removeAllCookie();
               SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.COOKIE_KEY, "");

               AULiveApplication.setUserInfo(new LoginUserEntity());

               Intent intent = new Intent(AULiveApplication.mContext, MainActivity.class);
               PendingIntent restartIntent =
                   PendingIntent.getActivity(AULiveApplication.mContext, 0, intent,
                       PendingIntent.FLAG_UPDATE_CURRENT);

               // initUserData();
               // 退出程序
               // AlarmManager mgr = (AlarmManager)
               // AULiveApplication.mContext
               // .getSystemService(Context.ALARM_SERVICE);
               // mgr.set(AlarmManager.RTC, System.currentTimeMillis() +
               // 500,
               // restartIntent); // 1秒钟后重启应用
               // ActivityStackManager.getInstance().exitActivity();
               Intent login_intent = new Intent(SettingActivity.this, LoginActivity.class);
               startActivity(login_intent);
               // 退出后按返回键又会返回到设置界面
               finish();

            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   //修改定位
   private void changeLocation(boolean is_show) {
      LoginUserEntity userEntity = AULiveApplication.getUserInfo();
      AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/profile/save",
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("nickname", userEntity.getNickname());

      request.addPostParams("sex", userEntity.getSex() + "");

      request.addPostParams("birthday", "1990-01-01");

      request.addPostParams("face", userEntity.getFace());

      request.addPostParams("signature", userEntity.signature);

      if (application.getCity() != null) {
         request.addPostParams("longitude", application.getLongitude() + "");
         request.addPostParams("latitude", application.getLatitude() + "");
         try {
            if (is_show) {
               request.addPostParams("city", URLEncoder.encode(application.getCity(), "utf-8"));
               request.addPostParams("prov", URLEncoder.encode(application.getProvince(), "utf-8"));
            } else {
               request.addPostParams("city", URLEncoder.encode("火星", "utf-8"));
               request.addPostParams("prov", URLEncoder.encode("火星", "utf-8"));
            }
         } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
         }
      }

      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            cancelProgressDialog();
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               //if (callback.getUserinfo() != null) {
               //   AULiveApplication.setUserInfo(callback.getUserinfo());
               //   AULiveApplication.setMyselfUserInfo(callback.getUserinfo().getUid(),
               //       callback.getUserinfo().getNickname(), callback.getUserinfo().getFace(),
               //       callback.getUserSig());
               //}
               SharedPreferenceTool.getInstance()
                   .saveBoolean(SharedPreferenceTool.NO_LOCATION_KEY, privisy_checkbox.isChecked());
               Utils.showCroutonText(SettingActivity.this, "修改成功");
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.net_error));
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }
}
