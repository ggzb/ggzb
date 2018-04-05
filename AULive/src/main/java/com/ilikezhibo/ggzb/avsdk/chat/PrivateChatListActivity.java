package com.ilikezhibo.ggzb.avsdk.chat;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.chat.event.CleanUnreadEvent;
import de.greenrobot.event.EventBus;
import tinker.android.util.TinkerManager;

/**
 * Created by big on 4/10/16.
 */
public class PrivateChatListActivity extends BaseFragmentActivity {
   private Context ctx = null;
   private UserInfo mSelfUserInfo;
   private ViewPager viewPager;

   public static int current_selected_index = 1;

   public boolean is_on_chat_context() {
      return is_on_chat_context;
   }

   public static boolean is_on_chat_context;

   @Override protected void setContentView() {
      setContentView(R.layout.qav_private_chat_list_layout);
      is_on_chat_context=true;
   }

   @Override protected void initializeViews() {
      ctx = this;

      AULiveApplication mAULiveApplication = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      mSelfUserInfo = mAULiveApplication.getMyselfUserInfo();

      /////init view

      ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
      tab.addView(LayoutInflater.from(this)
          .inflate(R.layout.custom_chatlist_icon_and_text_layout, tab, false));

      tab.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //更新未读信息数
            //EventBus.getDefault().post(new UpDateUnReadEvent());
            PrivateChatListActivity.this.finish();
         }
      });
      tab.findViewById(R.id.clean_unread_tv).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            EventBus.getDefault().post(new CleanUnreadEvent());
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

      FragmentPagerItems pages = new FragmentPagerItems(this);
      String[] tabs = { "好友", "未关注" };
      for (String tab1 : tabs) {
         if (tab1.equals("好友")) {
            pages.add(FragmentPagerItem.of(tab1, FriendsFragment.class));
         } else if (tab1.equals("未关注")) {
            pages.add(FragmentPagerItem.of(tab1, NoFriendsFragment.class));
         }
      }

      FragmentPagerItemAdapter adapter =
          new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);

      viewPager.setAdapter(adapter);
      viewPagerTab.setViewPager(viewPager);
   }

   @Override protected void initializeData() {

   }

   @Override protected void onDestroy() {
      super.onDestroy();
      is_on_chat_context=false;
   }
}
