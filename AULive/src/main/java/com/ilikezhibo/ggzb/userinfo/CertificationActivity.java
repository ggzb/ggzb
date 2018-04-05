package com.ilikezhibo.ggzb.userinfo;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.blacklist.BlackListActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

public class CertificationActivity extends BaseFragmentActivity implements OnClickListener {

   @Override protected void setContentView() {
      setContentView(R.layout.activity_certification);

      init();
   }

   private void init() {
      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setVisibility(View.VISIBLE);
      rl_back.setOnClickListener(this);

      TextView title = (TextView) this.findViewById(R.id.title);
      title.setOnClickListener(this);
      title.setText("实名认证");

   }

   @Override protected void initializeViews() {

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
         case R.id.blacklist_layout:
            Intent intent2 = new Intent(this, BlackListActivity.class);
            startActivity(intent2);
            break;
      }
   }

   private CustomDialog userBlackDialog;

   private void exitApp() {
      if (userBlackDialog == null) {
         userBlackDialog = new CustomDialog(CertificationActivity.this, new CustomDialogListener() {

            @Override public void onDialogClosed(int closeType) {
               switch (closeType) {
                  case CustomDialogListener.BUTTON_POSITIVE:
                     // 调用服务器清除cookie

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

}
