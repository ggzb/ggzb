package com.ilikezhibo.ggzb.avsdk.userinfo.toprankfragment;

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
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.DayTopRankFragment;
import com.jack.utils.PixelDpHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListAdapter;
import java.util.ArrayList;

public class TopRankMainFragment extends BaseFragment {

   public static String MemberInfo_key = "MemberInfo_key";
   private ViewPager viewPager;
   private PrivateChatListAdapter viewpager_adapter;
   public static int current_selected_index = 1;
   private TopRankFragment topRankFragment;
   private WeekTopRankFragment weekTopRankFragment;

   private View view;
   private ViewGroup tab;
   private boolean TopRankIsOpen;
   private DayTopRankFragment dayTopRankFragment;

   public static final TopRankMainFragment newInstance(String uid) {
      TopRankMainFragment f = new TopRankMainFragment();
      Bundle bdl = new Bundle(2);
      bdl.putString(MemberInfo_key, uid);
      f.setArguments(bdl);
      return f;
   }

   @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.top_rank_activity, null);

      String uid = getArguments().getString(MemberInfo_key);

      /////init view
      tab = (ViewGroup) view.findViewById(R.id.tab);
      tab.addView(LayoutInflater.from(this.getActivity())
          .inflate(R.layout.custom_chatlist_icon_and_text_layout, tab, false));

      tab.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            // >关闭了界面
            TopRankIsOpen = false;
            AvActivity avActivity = (AvActivity) TopRankMainFragment.this.getActivity();
            View fragment_container = avActivity.findViewById(R.id.mManagerList);
            ViewAnimator.animate(fragment_container)
                .translationX(0, PixelDpHelper.dip2px(avActivity, 500))
                .duration(300)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      AvActivity avActivity = (AvActivity) TopRankMainFragment.this.getActivity();
                      avActivity.findViewById(R.id.mManagerList).setVisibility(View.GONE);
                   }
                }).start();
         }
      });
      tab.findViewById(R.id.clean_unread_tv).setVisibility(View.GONE);

      viewPager = (ViewPager) view.findViewById(R.id.viewpager);
      final SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);

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

      topRankFragment = new TopRankFragment(uid);
      weekTopRankFragment = new WeekTopRankFragment(uid);
      dayTopRankFragment = new DayTopRankFragment(uid);

      ArrayList<Fragment> fragments = new ArrayList<Fragment>();
      fragments.add(topRankFragment);
      fragments.add(weekTopRankFragment);
      fragments.add(dayTopRankFragment);

      String[] tabs = {"贡献总榜", "贡献周榜", "贡献日榜"};

      viewpager_adapter =
          new PrivateChatListAdapter(TopRankMainFragment.this.getChildFragmentManager(), fragments,
              tabs);
      viewPager.setAdapter(viewpager_adapter);
      viewPagerTab.setViewPager(viewPager);

      return view;
   }

   ////显示有多少条未读信息
   //public void onEvent(UnReadMsgCountEvent unread_event) {
   //   Utils.showCroutonText(this.getActivity(), "您有" + unread_event.count + "条信息未阅读");
   //}

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

   public void showManagersList(BaseFragmentActivity context) {
      //EventBus.getDefault().post(new CloseAllPopUpDialogEvent());

      //Intent gag_list = new Intent(context, ManagerListActivity.class);
      //context.startActivity(gag_list);
      // >开启了界面
      TopRankIsOpen = true;

     context.getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.mManagerList, this)
          .commitAllowingStateLoss();
      View fragment_container = context.findViewById(R.id.mManagerList);
      fragment_container.setVisibility(View.VISIBLE);
      ViewAnimator.animate(fragment_container)
          .translationX(PixelDpHelper.dip2px(context, 500), 0)
          .duration(300)

          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {

             }
          }).start();
   }


   // >关闭贡献页面的方法
   public void closeTopRankMainFragment() {
      tab.findViewById(R.id.button1).performClick();
   }

   // >获得界面是否被打开
   public boolean getTopRankIsOpen() {
      return TopRankIsOpen;
   }
}
