package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.PixelDpHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.FriendsFragment;
import com.ilikezhibo.ggzb.avsdk.chat.NoFriendsFragment;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatListActivity;
import com.ilikezhibo.ggzb.avsdk.chat.event.CleanUnreadEvent;
import com.ilikezhibo.ggzb.avsdk.chat.event.RefreshChatListEvent;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

/**
 * 对话列表操作类
 * Created by big on 4/9/16.
 */
public class PrivateChatListHelper {
   private BaseFragmentActivity activity;
   PrivateChatHelper privateChatHelper;
   private PrivateChatListAdapter viewpager_adapter;
   private FriendsFragment friendsfragment;
   private NoFriendsFragment nofriendsfragment;

   public PrivateChatListHelper(BaseFragmentActivity activity1,
       PrivateChatHelper privateChatHelper1) {
      activity = activity1;
      privateChatHelper = privateChatHelper1;
      initConversationList();
   }

   //记录是否已经打开或关闭
   private boolean private_chat_open = false;
   private LinearLayout conversationlist_ly;
   private LinearLayout conversationlist_click;
   private LinearLayout chatlist_placeholder_ly;

   private ViewPager viewPager;
   public static int current_selected_index = 1;

   private void initConversationList() {
      //处理开关的layout
      conversationlist_ly = (LinearLayout) activity.findViewById(R.id.conversationlist_ly);
      conversationlist_ly.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //什么也不做
         }
      });
      conversationlist_ly.setVisibility(View.GONE);

      conversationlist_click = (LinearLayout) activity.findViewById(R.id.conversationlist_click);
      conversationlist_click.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            // 关闭
            if (private_chat_open == true) {
               onClosPrivate();
            }
         }
      });

      conversationlist_click.setVisibility(View.GONE);

      chatlist_placeholder_ly = (LinearLayout) activity.findViewById(R.id.chatlist_placeholder_ly);
      chatlist_placeholder_ly.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            // 关闭
            if (private_chat_open == true) {
               onClosPrivate();
            }
         }
      });
      chatlist_placeholder_ly.setVisibility(View.GONE);

      /////init view

      ViewGroup tab = (ViewGroup) activity.findViewById(R.id.tab);
      tab.addView(LayoutInflater.from(activity)
          .inflate(R.layout.custom_chatlist_icon_and_text_layout, tab, false));

      tab.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            onClosPrivate();
         }
      });
      tab.findViewById(R.id.clean_unread_tv).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            EventBus.getDefault().post(new CleanUnreadEvent());
         }
      });

      viewPager = (ViewPager) activity.findViewById(R.id.viewpager);
      final SmartTabLayout viewPagerTab = (SmartTabLayout) activity.findViewById(R.id.viewpagertab);

      final LayoutInflater inflater = LayoutInflater.from(activity);
      final Resources res = activity.getResources();

      viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
         @Override public void onTabClicked(int position) {

         }
      });
      viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         @Override
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         }

         @Override public void onPageSelected(int position) {
            current_selected_index = position;
            LinearLayout sts = (LinearLayout) viewPagerTab.getChildAt(0);
            for (int i = 0; i < sts.getChildCount(); i++) {
               View ib = sts.getChildAt(i);
               TextView custom_tab_text = (TextView) ib.findViewById(R.id.custom_tab_text);
               ImageView custom_tab_icon = (ImageView) ib.findViewById(R.id.custom_tab_icon);
               if (i == position) {
                  custom_tab_text.setTextColor(
                      AULiveApplication.mContext.getResources().getColor(R.color.white));
               } else {
                  custom_tab_text.setTextColor(
                      AULiveApplication.mContext.getResources().getColor(R.color.white));
               }
               custom_tab_icon.setVisibility(View.INVISIBLE);
            }
         }

         @Override public void onPageScrollStateChanged(int state) {

         }
      });

      friendsfragment = new FriendsFragment();
      friendsfragment.setPrivateChatHelper(PrivateChatListHelper.this, privateChatHelper);
      nofriendsfragment = new NoFriendsFragment();
      nofriendsfragment.setPrivateChatHelper(PrivateChatListHelper.this, privateChatHelper);

      ArrayList<Fragment> fragments = new ArrayList<Fragment>();
      fragments.add(friendsfragment);
      fragments.add(nofriendsfragment);

      String[] tabs = { "好友", "未关注" };

      viewpager_adapter =
          new PrivateChatListAdapter(activity.getSupportFragmentManager(), fragments, tabs);
      viewPager.setAdapter(viewpager_adapter);
      viewPagerTab.setViewPager(viewPager);
   }

   public void onOpenPrivateChat() {
      PrivateChatListActivity.is_on_chat_context = true;
      private_chat_open = true;
      conversationlist_ly.setVisibility(View.VISIBLE);
      conversationlist_click.setVisibility(View.VISIBLE);
      chatlist_placeholder_ly.setVisibility(View.VISIBLE);
      ViewAnimator.animate(conversationlist_ly)
          .translationY(PixelDpHelper.dip2px(activity, 700), 0)
          .duration(300)
          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {
                EventBus.getDefault().post(new RefreshChatListEvent());
             }
          })
          .start();
   }

   public void onClosPrivate() {
      PrivateChatListActivity.is_on_chat_context = false;
      private_chat_open = false;
      ViewAnimator.animate(conversationlist_ly)
          .translationY(0, PixelDpHelper.dip2px(activity, 700))
          .duration(300)
          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {
                conversationlist_ly.setVisibility(View.GONE);
                conversationlist_click.setVisibility(View.GONE);
                chatlist_placeholder_ly.setVisibility(View.GONE);
             }
          })
          .start();
   }

   public void cleanViews() {
      conversationlist_ly = null;
      conversationlist_click = null;
      chatlist_placeholder_ly = null;

      friendsfragment = null;
      nofriendsfragment = null;
      viewpager_adapter = null;
      viewPager = null;

      activity = null;
   }
}
