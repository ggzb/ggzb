package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.PixelDpHelper;
import com.jack.utils.Trace;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.dbhelper.DatabaseHelper;
import com.ilikezhibo.ggzb.dbhelper.entity.DBUserInfo;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import java.sql.SQLException;
import popwindow.PopupWindowUtil;

/**
 * 对话界面操作类
 * Created by big on 4/9/16.
 */
public class PrivateChatHelper {
   private AvActivity activity;
   private TextView title_tv;
   private TextView topRight_clean_Btn;
   private Button chat_more_btn;
   private boolean has_lahei;

   //对话列表界面
   public PrivateChatHelper(AvActivity activity1) {
      activity = activity1;
      //只有每次在直播间关闭聊天时摧毁fragment才能即是接收到聊天消息
      initConversation();
   }

   private boolean private_chat_open = false;
   private LinearLayout conversation_ly;
   private LinearLayout conversation_click;
   private LinearLayout chat_placeholder_ly;

   //conversation对应的id
   public String mTargetId;

   private void initConversation() {

      //处理开关的layout
      conversation_ly = (LinearLayout) activity.findViewById(R.id.conversation_ly);
      conversation_ly.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //什么也不做
         }
      });
      conversation_ly.setVisibility(View.GONE);

      conversation_click = (LinearLayout) activity.findViewById(R.id.conversation_click);
      conversation_click.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            // 关闭
            if (private_chat_open == true) {
               onClosPrivate();
            }
         }
      });
      conversation_click.setVisibility(View.GONE);

      chat_placeholder_ly = (LinearLayout) activity.findViewById(R.id.chat_placeholder_ly);
      chat_placeholder_ly.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            // 关闭
            if (private_chat_open == true) {
               //关闭软键盘
               InputMethodManager imm =
                   (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
               onClosPrivate();
            }
         }
      });
      chat_placeholder_ly.setVisibility(View.GONE);

      //title init
      title_tv = (TextView) activity.findViewById(R.id.title);
      title_tv.setText("聊天");

      topRight_clean_Btn = (TextView) activity.findViewById(R.id.topRightBtn);
      topRight_clean_Btn.setVisibility(View.GONE);
      chat_more_btn = (Button) activity.findViewById(R.id.chat_more_btn);

      Button righ_bt = (Button) activity.findViewById(R.id.back);
      righ_bt.setVisibility(View.VISIBLE);
      righ_bt.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //关闭软键盘
            InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            onClosPrivate();
         }
      });
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
      final TextView chat_more_check = (TextView) popupWindow.findId(R.id.chat_more_check);
      chat_more_check.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            popupWindow.dismiss();
            Trace.d("***" +  "半屏聊天打开 popwindow");
            SoloMgmtUtils.openMemberInfo(chatId, chat_more_check, activity);
         }
      });
      // >清空
      TextView chat_more_clear = (TextView) popupWindow.findId(R.id.chat_more_clear);
      chat_more_clear.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            SoloMgmtUtils.clearChat(activity, chatId);
            popupWindow.dismiss();
         }
      });
      // >拉黑
      final TextView chat_more_lahei = (TextView) popupWindow.findId(R.id.chat_more_lahei);
      Trace.d("**>第一次打开popwindow");
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
                           Utils.showCroutonText(activity, "解除拉黑");
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
                           Utils.showCroutonText(activity, "成功拉黑");
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

   /**
    * 检查是否拉黑
    */
   private void checkIsLahei(final String chatId, final TextView chat_more_lahei) {
      try {
         if (RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance()
                    .getRongIMClient()
                    .getBlacklistStatus(chatId,
                            new RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>() {
                               @Override public void onSuccess(
                                       RongIMClient.BlacklistStatus blacklistStatus) {
                                  if (blacklistStatus
                                          == RongIMClient.BlacklistStatus.IN_BLACK_LIST) {
                                     has_lahei = true;
                                     Trace.d("**>在黑名单");
                                     chat_more_lahei.setText("解除");
                                  } else {
                                     has_lahei = false;
                                     chat_more_lahei.setText("拉黑");
                                  }
                               }

                               @Override public void onError(RongIMClient.ErrorCode errorCode) {

                               }
                            });
         }
      } catch (Exception e) {

      }
   }

   //打开界面
   public void startPrivateChat(String uid, String nickname) {

      title_tv.setText(nickname);

      mTargetId = uid;
      if (mTargetId == null || mTargetId.equals("")) {
         Utils.showCroutonText(activity, "uid为空");
         return;
      }
      //自己发送的内容监听
      if (RongIM.getInstance() != null) {
         //设置自己发出的消息监听器。
         RongIM.getInstance().setSendMessageListener(activity.mySendMessageListener);
      }
      DBUserInfo dbUserInfo = null;
      //获取新数据
      try {
         if (mTargetId != null) {
            dbUserInfo = DatabaseHelper.getHelper(activity).getUserInfo(mTargetId);
         }
      } catch (SQLException e) {

      }
      if (dbUserInfo != null && dbUserInfo.getName() != null) {
         title_tv.setText(dbUserInfo.getName());
         chat_more_btn.setVisibility(View.VISIBLE);
         chat_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openPopwindow(mTargetId);
            }
         });
      }

      enterFragment(mTargetId);
      if (private_chat_open == false) {
         onOpenPrivateChat();
      }
   }


   /**
    * 加载会话页面 ConversationFragment
    * 44
    *
    * @param mConversationType 会话类型
    * @param mTargetId 目标 Id
    */
   ConversationFragment fragment;

   private void enterFragment(String mTargetId) {

      fragment = new ConversationFragment();
      Uri uri = Uri.parse("rong://" + activity.getApplicationInfo().packageName)
          .buildUpon()
          .appendPath("conversation")
          .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
          .appendQueryParameter("targetId", mTargetId)
          .build();

      fragment.setUri(uri);
      FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
      transaction.add(R.id.rong_content, fragment);
      //transaction.addToBackStack(fragment.getTag());
      transaction.commit();


      //InputProvider.MainInputProvider text_provider = RongContext.getInstance().getPrimaryInputProvider();
      //if (text_provider instanceof TextInputProvider) {
      //   TextInputProvider textInputProvider = (TextInputProvider) text_provider;
      //   textInputProvider.setEditTextChangedListener(new TextWatcher() {
      //      @Override
      //      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      //
      //      }
      //
      //      @Override
      //      public void onTextChanged(CharSequence s, int start, int before, int count) {
      //         Trace.d("onTextChanged:" + s);
      //      }
      //
      //      @Override public void afterTextChanged(Editable s) {
      //
      //      }
      //   });
      //}


   }

   public void onOpenPrivateChat() {
      private_chat_open = true;
      conversation_ly.setVisibility(View.VISIBLE);
      conversation_click.setVisibility(View.VISIBLE);
      chat_placeholder_ly.setVisibility(View.VISIBLE);
      ViewAnimator.animate(conversation_ly)
          .translationY(PixelDpHelper.dip2px(activity, 800), 0)
          .duration(300)

          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {

             }
          }).start();
   }

   public void onClosPrivate() {

      private_chat_open = false;

      ViewAnimator.animate(conversation_ly)
          .translationY(0, PixelDpHelper.dip2px(activity, 800))
          .duration(300)

          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {
                conversation_ly.setVisibility(View.GONE);
                conversation_click.setVisibility(View.GONE);
                chat_placeholder_ly.setVisibility(View.GONE);
             }
          }).start();
      if (fragment != null) {
         fragment.onPause();
         fragment.onStop();
         fragment.onDestroyView();
         //fragment.onBackPressed();
         fragment.onDestroy();
      }
   }

   public void cleanViews() {
      conversation_ly = null;
      conversation_click = null;
      chat_placeholder_ly = null;

      activity=null;
   }

   /////////////////////////////////////////////////////
   private PrivateChatGiftPagerUtil privateChat_giftPagerUtil;
   public PopupWindowUtil privateChat_giftpager_popupWindow;

   //私聊里的送礼物
   public void showPrivateChatGiftPager() {

      if (privateChat_giftPagerUtil == null) {
         privateChat_giftpager_popupWindow = new PopupWindowUtil(conversation_ly);
         privateChat_giftpager_popupWindow.setContentView(R.layout.room_gift_pager);
         privateChat_giftpager_popupWindow.setOutsideTouchable(true);
         privateChat_giftpager_popupWindow.showBottom();
         privateChat_giftpager_popupWindow.setOnDismissListener(
             new PopupWindow.OnDismissListener() {
                @Override public void onDismiss() {
                   enterFragment(mTargetId);
                }
             });

         privateChat_giftPagerUtil = new PrivateChatGiftPagerUtil(activity,
             privateChat_giftpager_popupWindow.findId(R.id.root_view), mTargetId, mTargetId);
      } else {
         privateChat_giftPagerUtil.room_id = mTargetId;
         privateChat_giftPagerUtil.live_uid = mTargetId;
         privateChat_giftpager_popupWindow.showBottom();
      }
   }

   // >获得私聊界面是否打开
   public boolean getPrivateChatIsOpen() {
      return private_chat_open;
   }
}
