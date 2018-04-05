package com.ilikezhibo.ggzb.avsdk.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.utils.Trace;
import com.jack.utils.Utils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatListActivity;
import com.ilikezhibo.ggzb.avsdk.chat.event.UpDateUnReadEvent;
import com.ilikezhibo.ggzb.avsdk.search.SearchListActivity;
import com.ilikezhibo.ggzb.views.BadgeView;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import tinker.android.util.TinkerManager;

/**
 * Created by big on 2016/3/21.
 */
public class ProgramListActivity extends BaseFragmentActivity implements View.OnClickListener {
   private Context ctx = null;
   private UserInfo mSelfUserInfo;
   private long firstTime = 0;
   private ViewPager viewPager;

   public static int current_selected_index = 1;
   private Button private_chat;

   @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (!Utils.isLogin(ProgramListActivity.this)) {
         Utils.showCroutonText(this, "你还没登录");
         return;
      }

      setContentView(R.layout.program_list_activity_new);
      ctx = this;

      AULiveApplication mAULiveApplication = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      mSelfUserInfo = mAULiveApplication.getMyselfUserInfo();

      /////init view

      ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
      tab.addView(
          LayoutInflater.from(this).inflate(R.layout.custom_tab_icon_and_text_layout, tab, false));

      //私聊
      private_chat = (Button) tab.findViewById(R.id.button2);
      private_chat.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            startActivity(new Intent(ProgramListActivity.this, PrivateChatListActivity.class));
         }
      });

      //搜索
      Button search = (Button) tab.findViewById(R.id.button1);
      search.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            startActivity(new Intent(ProgramListActivity.this, SearchListActivity.class));
         }
      });

      viewPager = (ViewPager) findViewById(R.id.viewpager);
      final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

      final LayoutInflater inflater = LayoutInflater.from(this);
      final Resources res = this.getResources();

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
                      AULiveApplication.mContext.getResources().getColor(R.color.qqblue));
                  custom_tab_text.setBackgroundResource(R.drawable.tab_textview_bg);
               } else {
                  custom_tab_text.setTextColor(
                      AULiveApplication.mContext.getResources().getColor(R.color.white));
                  custom_tab_text.setBackgroundResource(R.drawable.tab_textview_bg_white);
               }
               if (position == 1 && i == 1) {
                  custom_tab_icon.setVisibility(View.VISIBLE);
               } else {
                  custom_tab_icon.setVisibility(View.INVISIBLE);
               }
            }
         }

         @Override public void onPageScrollStateChanged(int state) {

         }
      });

      FragmentPagerItems pages = new FragmentPagerItems(this);
      String[] tabs = { "关注", "热门", "最新" };
      for (String tab1 : tabs) {
         if (tab1.equals("热门")) {
            pages.add(FragmentPagerItem.of(tab1, LiveFragment.class));
         } else if (tab1.equals("关注")) {
            pages.add(FragmentPagerItem.of(tab1, AttenFragment.class));
         } else {
            pages.add(FragmentPagerItem.of(tab1, NewsFragment.class));
         }
      }

      FragmentPagerItemAdapter adapter =
          new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);

      viewPager.setAdapter(adapter);
      viewPagerTab.setViewPager(viewPager);
      //初始化第二个tab
      viewPager.setCurrentItem(1);
      LinearLayout sts = (LinearLayout) viewPagerTab.getChildAt(0);
      View ib = sts.getChildAt(1);
      TextView custom_tab_text = (TextView) ib.findViewById(R.id.custom_tab_text);
      custom_tab_text.setTextColor(
          AULiveApplication.mContext.getResources().getColor(R.color.qqblue));
      ImageView custom_tab_icon = (ImageView) ib.findViewById(R.id.custom_tab_icon);
      custom_tab_icon.setVisibility(View.VISIBLE);
      EventBus.getDefault().register(this);
   }

   public void setContentView() {

   }

   public void initializeViews() {
      badgeView = new BadgeView(ProgramListActivity.this);
   }

   public void initializeData() {
   }

   @Override protected void onResume() {
      super.onResume();
   }

   BadgeView badgeView = null;

   public void onEvent(UpDateUnReadEvent token_event) {

      //更新私聊未读数
      badgeView.setTargetView(findViewById(R.id.right_ly));
      //badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
      int count = RongIM.getInstance()
          .getRongIMClient()
          .getUnreadCount(Conversation.ConversationType.PRIVATE);
      badgeView.setBadgeCount(count);
      if (count < 1) {
         badgeView.setVisibility(View.INVISIBLE);
      } else {
         badgeView.setVisibility(View.VISIBLE);
      }

      Trace.d("ProgramListActivity onEvent(UpDateUnReadEvent count:" + count);
   }

   @Override public void onDestroy() {
      super.onDestroy();
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {
         case R.id.live_list:

            break;

         default:
            break;
      }
   }

   @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
      switch (keyCode) {
         case KeyEvent.KEYCODE_BACK:
            long secondTime = System.currentTimeMillis();

            if (secondTime - firstTime > 2000) {
               firstTime = secondTime;
               Utils.showCroutonText(this, "再点击一次退出程序");
               return true;
            } else {
               this.finish();
            }
            break;
      }
      return super.onKeyUp(keyCode, event);
   }
}
