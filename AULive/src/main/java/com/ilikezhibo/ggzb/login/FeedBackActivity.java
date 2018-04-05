package com.ilikezhibo.ggzb.login;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.MobileConfig;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

public class FeedBackActivity extends BaseFragmentActivity
    implements OnClickListener, TitleListener {
   public static final String INTENT_IS_FORGET_PASSWORD_KEY = "INTENT_IS_FORGET_PASSWORD_KEY";

   private EditText feed_back_text;

   private boolean isForgetPassword = false;

   @Override protected void setContentView() {
      isForgetPassword = getIntent().getBooleanExtra(INTENT_IS_FORGET_PASSWORD_KEY, false);

      setContentView(R.layout.activity_feedback);

      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);

      TextView tv_title = (TextView) this.findViewById(R.id.title);

      TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
      topRightBtn.setText("注册");
      topRightBtn.setOnClickListener(this);
      topRightBtn.setVisibility(View.GONE);

      if (isForgetPassword) {
         tv_title.setText("客服帮助");
      } else {
         tv_title.setText("帮助与反馈");
      }

      feed_back_text = (EditText) findViewById(R.id.feed_back_text);
      if (isForgetPassword) {
         feed_back_text.setHint("请输入您的资料信息（例如：手机号，用户昵称），以便于我们审核。最后再加上您的联系方式，以便官方把处理结果通知您");
      }
      feed_back_text.setOnClickListener(this);

      Button post_button = (Button) findViewById(R.id.post_button);
      post_button.setOnClickListener(this);
   }

   @Override protected void initializeViews() {

   }

   @Override protected void initializeData() {

   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }

      switch (v.getId()) {
         case R.id.post_button:
            if (feed_back_text.getText().length() < 5) {
               Utils.showMessage("你输入的内容过少");
               return;
            }

            if (isForgetPassword) {
               findPassword(feed_back_text.getText().toString());
            } else {
               postFeedBack("memo", feed_back_text.getText().toString());
            }
            break;
         case R.id.back:
            this.finish();
            break;
      }
   }

   private void postFeedBack(String name, String value) {

      // url = url.replaceAll("&", "%26");
      value = value.replaceAll(" ", "%20");
      RequestInformation request =
          new RequestInformation(UrlHelper.FEED_BACK, RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams(name, value);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               // feed_back_text.setText("");
               showSuccPrompt("提交成功,我们会尽快处理你拉反馈");
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

   private CustomDialog promptDialog;

   private void showSuccPrompt(String msg) {
      if (promptDialog == null) {
         promptDialog = new CustomDialog(this, new CustomDialogListener() {

            @Override public void onDialogClosed(int closeType) {
               // TODO Auto-generated method stub
               switch (closeType) {
                  case CustomDialogListener.BUTTON_POSITIVE:
                     FeedBackActivity.this.finish();
                     break;
               }
            }
         });
      }
      promptDialog.setCustomMessage(msg);
      promptDialog.setCancelable(false);
      promptDialog.setType(CustomDialog.SINGLE_BTN);

      if (null != promptDialog) {
         promptDialog.show();
      }
   }

   private void findPassword(String value) {
      value = value.replaceAll(" ", "%20");
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
          + "/other/contact?udid="
          + MobileConfig.getMobileConfig(this).getIemi()
          + "&memo="
          + value, RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            if (callback != null && callback.getStat() == 200) {
               showSuccPrompt("您的申请已提交，我们会尽快处理！");
               // FeedBackActivity.this.finish();
            } else {
               if (callback != null) {
                  Utils.showMessage(callback.getMsg());
               }
            }
         }

         @Override public void onFailure(AppException e) {

         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   @Override public void onBack() {
      this.finish();
   }

   @Override public void onTopRightEvent() {

   }
}
