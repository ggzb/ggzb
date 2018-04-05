package com.ilikezhibo.ggzb.avsdk.userinfo.toprank;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.utils.Utils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.event.UnReadMsgCountEvent;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListAdapter;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class TopRankActivity extends BaseFragmentActivity {

   public static String MemberInfo_key = "MemberInfo_key";
   private ViewPager viewPager;
   private PrivateChatListAdapter viewpager_adapter;
   public static int current_selected_index = 1;
   private TopRankFragment topRankFragment;
   private WeekTopRankFragment weekTopRankFragment;
   private DayTopRankFragment dayTopRankFragment;

   @Override protected void setContentView() {
      setContentView(R.layout.top_rank_activity);
      String uid = (String) getIntent().getStringExtra(MemberInfo_key);

      /////init view
      ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
      tab.addView(LayoutInflater.from(this)
          .inflate(R.layout.custom_chatlist_icon_and_text_layout, tab, false));

      tab.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {

            TopRankActivity.this.finish();
         }
      });
      tab.findViewById(R.id.clean_unread_tv).setVisibility(View.GONE);

      viewPager = (ViewPager) findViewById(R.id.viewpager);
      final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

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
                      AULiveApplication.mContext.getResources().getColor(R.color.black));
               } else {
                  custom_tab_text.setTextColor(
                      AULiveApplication.mContext.getResources().getColor(R.color.black));
               }
               custom_tab_icon.setVisibility(View.INVISIBLE);
            }
         }

         @Override public void onPageScrollStateChanged(int state) {

         }
      });
      topRankFragment = new TopRankFragment(uid);
      weekTopRankFragment = new WeekTopRankFragment(uid);
      dayTopRankFragment = new DayTopRankFragment(uid);

      ArrayList<Fragment> fragments = new ArrayList<Fragment>();
      fragments.add(topRankFragment);
      fragments.add(weekTopRankFragment);
      fragments.add(dayTopRankFragment);

      String[] tabs = {"贡献总榜", "贡献周榜", "贡献日榜"};

      viewpager_adapter =
          new PrivateChatListAdapter(TopRankActivity.this.getSupportFragmentManager(), fragments,
              tabs);
      viewPager.setAdapter(viewpager_adapter);
      viewPagerTab.setViewPager(viewPager);

      EventBus.getDefault().register(this);
   }

   //显示有多少条未读信息
   public void onEvent(UnReadMsgCountEvent unread_event) {
      Utils.showCroutonText(this, "您有" + unread_event.count + "条信息未阅读");
   }

   @Override protected void initializeViews() {

   }

   @Override protected void initializeData() {

   }

   @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
   }

   @Override protected void onRestart() {
      super.onRestart();
   }

   @Override public void onStart() {
      super.onStart();
   }

   @Override public void onResume() {
      super.onResume();
   }

   @Override public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
   }

   @Override public void onPause() {
      super.onPause();
   }

   @Override public void onStop() {

      super.onStop();
   }

   @Override public void onDestroy() {
      super.onDestroy();
   }

   @Override public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
   }
}
