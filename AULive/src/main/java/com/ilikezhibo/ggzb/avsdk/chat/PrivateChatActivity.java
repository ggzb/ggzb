package com.ilikezhibo.ggzb.avsdk.chat;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.jack.utils.Trace;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatGiftPagerUtil;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.dbhelper.DatabaseHelper;
import com.ilikezhibo.ggzb.dbhelper.entity.DBUserInfo;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import java.sql.SQLException;
import java.util.Locale;
import popwindow.PopupWindowUtil;

/**
 * Created by big on 4/10/16.
 */
public class PrivateChatActivity extends BaseFragmentActivity {
   /**
    * 目标 Id
    */
   public String mTargetId;
   public String uid;
   /**
    * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
    */
   private String mTargetIds;

   /**
    * 会话类型
    */
   private Conversation.ConversationType mConversationType;

   public static String STAR_PRIVATE_CHAT_BY_CODE_UID_KEY = "STAR_PRIVATE_CHAT_BY_CODE_UID_KEY";
   private TextView title;
   private TextView topRight_clean_Btn;
   private ConversationFragment fragment;
   private Button chat_more_btn;
   private boolean has_lahei;

   @Override protected void setContentView() {

      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
          | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

      setContentView(R.layout.qav_private_chat_layout);

      //rongcloud 不是在连接以及连接中,做重连处理
      RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus =
          RongIM.getInstance().getCurrentConnectionStatus();
      if (connectionStatus != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED
          && connectionStatus
          != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
         String im_token = AULiveApplication.getUserInfo().getIm_token();
         if (im_token != null) {
            Trace.d("不是在连接以及连接中,做重连处理");
            AULiveHomeActivity.connect(im_token, PrivateChatActivity.this);
         }
      }

   }

   @Override protected void initializeViews() {

      title = (TextView) findViewById(R.id.title);
      title.setText("聊天");

      topRight_clean_Btn = (TextView) findViewById(R.id.topRightBtn);
//      topRight_clean_Btn.setText("清空");
      topRight_clean_Btn.setVisibility(View.GONE);


      Button close_bt = (Button) findViewById(R.id.back);
      close_bt.setVisibility(View.VISIBLE);
      close_bt.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            PrivateChatActivity.this.finish();
         }
      });
   }

   @Override protected void initializeData() {
      Intent intent = getIntent();

      uid = intent.getStringExtra(STAR_PRIVATE_CHAT_BY_CODE_UID_KEY);
      if (uid != null && !uid.equals("")) {
         //手动启动
         enterFragment(Conversation.ConversationType.PRIVATE, uid);
      } else {
         //push启动会
         getIntentDate(intent);
      }

      DBUserInfo dbUserInfo = null;
      ////获取缓存新数据
      try {
         if (uid != null && !uid.equals("")) {
            //手动启动
            dbUserInfo = DatabaseHelper.getHelper(PrivateChatActivity.this).getUserInfo(uid);
         } else {
            //push启动会
            dbUserInfo = DatabaseHelper.getHelper(PrivateChatActivity.this).getUserInfo(mTargetId);
         }
      } catch (SQLException e) {

      }
      if (dbUserInfo != null && dbUserInfo.getName() != null) {
         title.setText(dbUserInfo.getName());
      }
   }

   @Override public void finish() {

      if (uid != null && !uid.equals("")) {
         //手动启动
      } else {
         //push启动会或通知栏启动
         startActivity(new Intent(this, MainActivity.class));
      }

      privateChat_giftPagerUtil = null;
      privateChat_giftpager_popupWindow = null;

      super.finish();
   }

   /**
    * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
    */
   private void getIntentDate(Intent intent) {

      mTargetId = intent.getData().getQueryParameter("targetId");
      mTargetIds = intent.getData().getQueryParameter("targetIds");
      //intent.getData().getLastPathSegment();//获得当前会话类型
      mConversationType = Conversation.ConversationType.valueOf(
          intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

      enterFragment(mConversationType, mTargetId);
   }

   AvActivity.MySendMessageListener mySendMessageListener = new AvActivity().new MySendMessageListener();
   /**
    * 加载会话页面 ConversationFragment
    *
    * @param mConversationType 会话类型
    * @param mTargetId 目标 Id
    */
   private void enterFragment(Conversation.ConversationType mConversationType,
       final String mTargetId) {
      chat_more_btn = (Button) findViewById(R.id.chat_more_btn);
      chat_more_btn.setVisibility(View.VISIBLE);
      chat_more_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            openPopwindow(mTargetId);
         }
      });
     /* topRight_clean_Btn.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            Trace.d("**>>>竟然是在这里");
            if (mTargetId == null || mTargetId.equals("")) {
               return;
            }
            if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
               return;
            }
            RongIM.getInstance()
                .getRongIMClient()
                .clearMessages(Conversation.ConversationType.PRIVATE, mTargetId,
                    new RongIMClient.ResultCallback<Boolean>() {
                       @Override public void onSuccess(Boolean aBoolean) {
                          Utils.showCroutonText(PrivateChatActivity.this, "成功清空");
                       }

                       @Override public void onError(RongIMClient.ErrorCode errorCode) {

                       }
                    });
         }
      });*/

      fragment = new ConversationFragment();

      Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
          .buildUpon()
          .appendPath("conversation")
          .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
          .appendQueryParameter("targetId", mTargetId)
          .build();

      fragment.setUri(uri);
      //自己发送的内容监听
      if (RongIM.getInstance() != null) {
         //设置自己发出的消息监听器。
         RongIM.getInstance().setSendMessageListener(mySendMessageListener);
      }
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.add(R.id.rong_content, fragment);
      transaction.commit();
   }

   /**
    * 打开查看资料等popwindow
    * @param chatId
    */
   private void openPopwindow(final String chatId) {

      // >显示popwindow
      final PopupWindowUtil popupWindow = new PopupWindowUtil(chat_more_btn);
      popupWindow.setContentView(R.layout.dialog_chat_more);
      popupWindow.setOutsideTouchable(true);

      popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
         @Override
         public void onDismiss() {

         }
      });
      // >显示popwindow
      popupWindow.showAlignParentRight(-10, 0);
      // >查看资料
      TextView chat_more_check = (TextView) popupWindow.findId(R.id.chat_more_check);
      chat_more_check.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent homepage_Intent =
                    new Intent(PrivateChatActivity.this,
                            HomePageActivity.class);
            homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                    chatId);
            PrivateChatActivity.this.startActivity(homepage_Intent);
            popupWindow.dismiss();
         }
      });
      // >清空
      TextView chat_more_clear = (TextView) popupWindow.findId(R.id.chat_more_clear);
      chat_more_clear.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if(chatId == null) {
               return;
            }
            SoloMgmtUtils.clearChat(PrivateChatActivity.this, chatId);
            popupWindow.dismiss();
         }
      });
      // >拉黑
      final TextView chat_more_lahei = (TextView) popupWindow.findId(R.id.chat_more_lahei);
      SoloMgmtUtils.checkIsLahei(chatId, new SoloRequestListener() {
         @Override
         public void onSuccess() {
            // >在黑名单
            chat_more_lahei.setText("解除");
            has_lahei = true;
            setTextViewListener();
         }

         @Override
         public void onFailure() {
            // >不在
            chat_more_lahei.setText("拉黑");
            has_lahei = false;
            setTextViewListener();
         }

         private void setTextViewListener() {
            chat_more_lahei.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  // >在这里拉黑
                  if (has_lahei) {
                     SoloMgmtUtils.removeLahei(chatId, new SoloRequestListener() {
                        @Override
                        public void onSuccess() {
                           Utils.showCroutonText(PrivateChatActivity.this, "解除拉黑");
                           chat_more_lahei.setText("拉黑");
                           has_lahei = false;
                        }

                        @Override
                        public void onFailure() {

                        }
                     });
                  } else {
                     SoloMgmtUtils.addLahei(chatId, new SoloRequestListener() {
                        @Override
                        public void onSuccess() {
                           Utils.showCroutonText(PrivateChatActivity.this, "成功拉黑");
                           chat_more_lahei.setText("解除");
                           has_lahei = true;
                        }

                        @Override
                        public void onFailure() {

                        }
                     });
                  }
                  popupWindow.dismiss();
               }
            });
         }
      });
      // >取消
      TextView chat_more_cancel = (TextView) popupWindow.findId(R.id.chat_more_cancel);
      chat_more_cancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            popupWindow.dismiss();
         }
      });
   }


   @Override protected void onResume() {
      super.onResume();

      //私聊送礼相关
      AULiveApplication.mPrivateChatActivity = this;
      AULiveApplication.mAvActivity = null;
   }

   /////////////////////////////////////////////////////
   private PrivateChatGiftPagerUtil privateChat_giftPagerUtil;
   public PopupWindowUtil privateChat_giftpager_popupWindow;

   //私聊里的送礼物
   public void showPrivateChatGiftPager() {
      if (privateChat_giftPagerUtil == null) {
         privateChat_giftpager_popupWindow = new PopupWindowUtil(title);
         privateChat_giftpager_popupWindow.setContentView(R.layout.room_gift_pager);
         privateChat_giftpager_popupWindow.setOutsideTouchable(true);
         privateChat_giftpager_popupWindow.showBottom();
         privateChat_giftpager_popupWindow.setOnDismissListener(
             new PopupWindow.OnDismissListener() {
                @Override public void onDismiss() {
                   //刷新界面
                   if (uid != null && !uid.equals("")) {
                      //手动启动
                      enterFragment(Conversation.ConversationType.PRIVATE, uid);
                   } else {
                      //push启动会
                      getIntentDate(getIntent());
                   }
                }
             });

         if (uid != null && !uid.equals("")) {
            //手动启动
            privateChat_giftPagerUtil = new PrivateChatGiftPagerUtil(PrivateChatActivity.this,
                privateChat_giftpager_popupWindow.findId(R.id.root_view), uid, uid);
         } else {
            //push启动会
            privateChat_giftPagerUtil = new PrivateChatGiftPagerUtil(PrivateChatActivity.this,
                privateChat_giftpager_popupWindow.findId(R.id.root_view), mTargetId, mTargetId);
         }
      } else {
         if (uid != null && !uid.equals("")) {
            //手动启动
            privateChat_giftPagerUtil.room_id = uid;
            privateChat_giftPagerUtil.live_uid = uid;
         } else {
            //push启动会

            privateChat_giftPagerUtil.room_id = mTargetId;
            privateChat_giftPagerUtil.live_uid = mTargetId;
         }

         privateChat_giftpager_popupWindow.showBottom();
      }
   }
}
