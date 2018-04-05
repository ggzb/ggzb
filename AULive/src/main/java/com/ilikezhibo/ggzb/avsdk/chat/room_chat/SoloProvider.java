package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.ilikezhibo.ggzb.views.UserInfoWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

import io.rong.imkit.RongContext;
import io.rong.imkit.widget.provider.InputProvider;


/**
 * 私聊下发送1v1视频
 */
public class SoloProvider extends InputProvider.ExtendProvider {

   private Activity activity;
   private String uid;

   private Handler handler = new Handler() {

      private String rightButton;
      private String leftButton;
      private String content;
      private String title;
      private ViewGroup toastRoot;

      @Override
      public void handleMessage(Message msg) {

         switch (msg.what) {
            case SoloMgmtUtils.NO_OPEN:
               // >提示主播未开通1v1
               Trace.d("**>主播没开通1v1");
               toastRoot =
                       (ViewGroup) activity
                               .getLayoutInflater()
                               .inflate(R.layout.my_toast, null);
               if (msg.obj != null) {
                  String str = (String) msg.obj;
                  Utils.setCustomViewToast(toastRoot, false, str);
               } else {
                  Utils.setCustomViewToast(toastRoot, false, "当前主播还没有开通1v1功能~");
               }
               break;

            case SoloMgmtUtils.LIVE_TRUE:
               // >提示主播正忙
               Trace.d("**>主播忙");
               toastRoot = (ViewGroup) activity
                       .getLayoutInflater()
                       .inflate(R.layout.my_toast, null);
               if (msg.obj != null) {
                  String str = (String) msg.obj;
                  Utils.setCustomViewToast(toastRoot, false, str);
               } else {
                  Utils.setCustomViewToast(toastRoot, false, "当前主播正在直播哦~");
               }
               break;
            case SoloMgmtUtils.CAN_SEND:
               // >发起1v1
               SoloHelper soloHelper = new SoloHelper(activity);
               Trace.d("**>>应该显示");
               soloHelper.callSolo();
               break;
            case SoloMgmtUtils.NEED_PREPAID:
               // >提示需要充值
               title = "钻石余额不足哦";
               content = "钻石余额不足以支持本次聊天哦, 您要前往充值吗? ";
               leftButton = "充值";
               rightButton = "取消";
               showCustomDialog(title, content, leftButton, rightButton,
                       SoloMgmtUtils.NEED_PREPAID);
               break;
            case SoloMgmtUtils.SHOW_SEND:
               content = (String) msg.obj;
               title = "发送视频聊天";
               leftButton = "继续";
               rightButton = "取消";
               if ("520".equals(content)) {
                  content = "本次发起的 1 对 1 视频聊天将消耗您 " + SoloMgmtUtils.price + " 钻/分钟, 您确定继续发送视频聊天么?";
                  showCustomDialog(title, content, leftButton, rightButton, SoloMgmtUtils.NEED_PROMPT);
               } else {
                  showCustomDialog(title, content, leftButton, rightButton, SoloMgmtUtils.SEND_VIDEO);

               }
               break;
         }
      }
   };


   public SoloProvider(RongContext context) {
      super(context);
   }

   /**
    * 设置展示的图标
    */
   @Override public Drawable obtainPluginDrawable(Context context) {
      //R.drawable.de_contacts 通讯录图标
      return context.getResources().getDrawable(R.drawable.rc_ic_solo);
   }

   /**
    * 设置图标下的title
    */
   @Override public CharSequence obtainPluginTitle(Context context) {
      //R.string.add_contacts 通讯录
      return "1V1视频";
   }

   /**
    * click 事件发起1v1视频
    */
   @Override
   public void onPluginClick(View view) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }
      if (AULiveApplication.mAvActivity != null) {
         activity = AULiveApplication.mAvActivity;
         uid = AULiveApplication.mAvActivity.privateChatHelper.mTargetId;
         if (activity instanceof AvActivity) {
            AvActivity acti = (AvActivity) activity;
            if (acti.is_creater) {
               ViewGroup toastRoot =
                       (ViewGroup) activity
                               .getLayoutInflater()
                               .inflate(R.layout.my_toast, null);
               Utils.setCustomViewToast(toastRoot, false, "您当前在正在直播, 不能发起1对1哦");
               Trace.d(uid + "**** chathelper");
               return;
            }
         }
      } else if (AULiveApplication.mPrivateChatActivity != null) {
         activity = AULiveApplication.mPrivateChatActivity;
         uid = AULiveApplication.mPrivateChatActivity.uid;
         Trace.d(uid + "****" + "chatactivity");
      } else {
         Utils.showMessage(Utils.trans(R.string.get_info_fail));
         return;
      }
      // >是否需要显示提示对话框
      if (SharedPreferenceTool.getInstance()
              .getBoolean(SharedPreferenceTool.SHOW_SOLO_DIALOG, true)) {
         SoloMgmtUtils.showDialog(activity, handler, new SoloRequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {

            }
         });
      } else {
      }
      // >发送视频对话框

   }



   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode != Activity.RESULT_OK) {
         return;
      }
      super.onActivityResult(requestCode, resultCode, data);
   }





   /**
    * 对话框
    * @param title 标题内容
    * @param content 对话框内容
    * @param leftButton 左按钮
    * @param rightButton 右按钮
    * @param action 动作
    */
   private void showCustomDialog(String title, String content, String leftButton, String rightButton, final int action) {
      final CustomDialog customDialog = new CustomDialog(
              activity,
              new CustomDialogListener() {
                 @Override
                 public void onDialogClosed(int closeType) {

                    switch (closeType) {

                       case CustomDialogListener.BUTTON_POSITIVE:
                          switch (action) {

                             case SoloMgmtUtils.SEND_VIDEO:
                                // **检查其他状态
                                SoloMgmtUtils.checkAnchorOhter(uid, handler);
                                break;

                             // >需要更新
                             case SoloMgmtUtils.NEED_PROMPT:
                                Message msg = Message.obtain();
                                msg.what = SoloMgmtUtils.NEED_PREPAID;
                                handler.sendMessage(msg);
                                break;

                             // >需要充值
                             case SoloMgmtUtils.NEED_PREPAID:
                                Intent moeny_intent =
                                        new Intent(activity, BuyDiamondActivity.class);
                                activity.startActivity(moeny_intent);
//                                Intent intent4 =
//                                        new Intent(activity, UserInfoWebViewActivity.class);
//                                intent4.putExtra(WebViewActivity.input_url,
//                                        UrlHelper.SERVER_URL + "profile/h5charge");
//                                intent4.putExtra(WebViewActivity.back_home_key, false);
//                                intent4.putExtra(WebViewActivity.actity_name, "充值");
//                                activity.startActivity(intent4);
                                break;
                          }
                          break;

                       case CustomDialogListener.BUTTON_NEUTRAL:

                          break;
                    }
                 }
              });
      customDialog.setButtonText(leftButton, rightButton);
      customDialog.setCustomMessage(content);
      customDialog.setCancelable(true);
      customDialog.setAndFormatTitle(title, true, Color.parseColor("#FFFFFF"));
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      if(customDialog.isShowing() || activity.isFinishing()) {
         return;
      } else {
         customDialog.show();
      }
   }

}