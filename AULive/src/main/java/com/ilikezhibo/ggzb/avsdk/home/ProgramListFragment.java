package com.ilikezhibo.ggzb.avsdk.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.utils.Trace;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatListActivity;
import com.ilikezhibo.ggzb.avsdk.chat.event.UpDateUnReadEvent;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListAdapter;
import com.ilikezhibo.ggzb.avsdk.search.SearchListActivity;
import com.ilikezhibo.ggzb.views.BadgeView;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import java.util.ArrayList;
import tinker.android.util.TinkerManager;

/**
 * Created by big on 2016/3/21.
 */
public class ProgramListFragment extends BaseFragment implements View.OnClickListener {
   private Context ctx = null;
   private UserInfo mSelfUserInfo;
   private long firstTime = 0;
   private ViewPager viewPager;

   public static int current_selected_index = 1;
   private Button private_chat;
   private View root_view;
   private AttenFragment attenFragment;
   private LiveFragment liveFragment;
   private NewsFragment newsFragment;
   private PrivateChatListAdapter viewpager_adapter;
   private ArrayList<Fragment> fragments;
   private String[] tabs;
   private String[] tabs2;
   private SmartTabLayout viewPagerTab;

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      EventBus.getDefault().register(this);

      Trace.d("ProgramListFragment onCreateView");
      root_view = inflater.inflate(R.layout.program_list_activity_new, null);
      //if (!Utils.isLogin(ProgramListFragment.this.getActivity())) {
      //   Trace.d("ProgramListFragment 你还没登录");
      //   Utils.showCroutonText(this.getActivity(), "你还没登录");
      //   return root_view;
      //}

      AULiveApplication mAULiveApplication =
          (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      mSelfUserInfo = mAULiveApplication.getMyselfUserInfo();

      /////init view

      ViewGroup tab = (ViewGroup) root_view.findViewById(R.id.tab);
      tab.addView(LayoutInflater.from(this.getActivity())
          .inflate(R.layout.custom_tab_icon_and_text_layout, tab, false));

      //私聊
      private_chat = (Button) tab.findViewById(R.id.button2);
      private_chat.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            startActivity(
                new Intent(ProgramListFragment.this.getActivity(), PrivateChatListActivity.class));
         }
      });

      //搜索
      Button search = (Button) tab.findViewById(R.id.button1);
      search.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            startActivity(
                new Intent(ProgramListFragment.this.getActivity(), SearchListActivity.class));
         }
      });

      viewPager = (ViewPager) root_view.findViewById(R.id.viewpager);
      viewPagerTab = (SmartTabLayout) root_view.findViewById(R.id.viewpagertab);

      viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
         @Override public void onTabClicked(int position) {
            Trace.d("onTabClicked:" + position);
            if (attenFragment != null) {
               //使用了setOffscreenPageLimit，及setUserVisibleHint,功能重复，现取消
               if (position == 0) {
                  attenFragment.canReflesh();
               } else if (position == 1) {
                  liveFragment.canReflesh();
               } else if (position == 2) {
                  newsFragment.canReflesh();
               }
            }
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
                  //选中后文字为后白色，本为蓝色
                  custom_tab_text.setTextColor(
                      AULiveApplication.mContext.getResources().getColor(R.color.white));
                  custom_tab_text.setBackgroundResource(R.drawable.tab_textview_bg);
               } else {
                  custom_tab_text.setTextColor(
                      AULiveApplication.mContext.getResources().getColor(R.color.black));
                  custom_tab_text.setBackgroundResource(R.drawable.tab_textview_bg_white);
               }
               if (position == 1 && i == 1) {
                  custom_tab_icon.setVisibility(View.VISIBLE);
               } else {
                  custom_tab_icon.setVisibility(View.INVISIBLE);
               }
            }

            //if (position == 1) {
            //   liveFragment.doReflesh();
            //}
         }

         @Override public void onPageScrollStateChanged(int state) {

         }
      });

      doInitView();

      try {
         badgeView = new BadgeView(ProgramListFragment.this.getActivity());
         //更新私聊未读数
         //badgeView.setTargetView(root_view.findViewById(R.id.right_ly));
      } catch (Exception e) {

      }
      return root_view;
   }

   private void doInitView() {
      ////////////////////////////////////////////////////////////////
      //方法1
      //FragmentPagerItems pages = new FragmentPagerItems(this.getActivity());
      //String[] tabs = { "关注", "热门", "最新" };
      //for (String tab1 : tabs) {
      //   if (tab1.equals("热门")) {
      //      pages.add(FragmentPagerItem.of(tab1, LiveFragment.class));
      //   } else if (tab1.equals("关注")) {
      //      pages.add(FragmentPagerItem.of(tab1, AttenFragment.class));
      //   } else {
      //      pages.add(FragmentPagerItem.of(tab1, NewsFragment.class));
      //   }
      //}
      ////做超管处理
      //String[]  tabs2 = new String[] { "审核", "隐藏" };
      //if (AULiveApplication.getUserInfo().show_manager == 1) {
      //   for (String tab1 : tabs2) {
      //      if (tab1.equals("审核")) {
      //         pages.add(FragmentPagerItem.of(tab1, CheckFragment.class));
      //      } else if (tab1.equals("隐藏")) {
      //         pages.add(FragmentPagerItem.of(tab1, HideFragment.class));
      //      }
      //   }
      //}
      //
      //FragmentPagerItemAdapter viewpager_adapter =
      //    new FragmentPagerItemAdapter(this.getActivity().getSupportFragmentManager(), pages);

      ////////////////////////////////////////////////////////////////
      //方法2
      attenFragment = new AttenFragment();
      liveFragment = new LiveFragment();
      newsFragment = new NewsFragment();
      fragments = new ArrayList<Fragment>();
      fragments.add(attenFragment);
      fragments.add(liveFragment);
      fragments.add(newsFragment);

      tabs = new String[] { "关注", "热门", "最新" };
      tabs2 = new String[] { "关注", "热门", "最新", "审核", "隐藏" };
      if (AULiveApplication.getUserInfo().show_manager == 1) {
         CheckFragment checkFragment = new CheckFragment();
         fragments.add(checkFragment);
         HideFragment hideFragment = new HideFragment();
         fragments.add(hideFragment);
         tabs = tabs2;
      } else {

      }

      viewpager_adapter =
          new PrivateChatListAdapter(this.getChildFragmentManager(), fragments,
              tabs);
      ///////////////////////////////////////////////////////////////////
      //不销毁
      viewPager.setOffscreenPageLimit(2);
      viewPager.setAdapter(viewpager_adapter);
      viewPagerTab.setViewPager(viewPager);
      //初始化第二个tab
      viewPager.setCurrentItem(1);
      LinearLayout sts = (LinearLayout) viewPagerTab.getChildAt(0);
      View ib = sts.getChildAt(1);
      TextView custom_tab_text = (TextView) ib.findViewById(R.id.custom_tab_text);
      //选中后文字为白色
      custom_tab_text.setTextColor(
          AULiveApplication.mContext.getResources().getColor(R.color.white));
      ImageView custom_tab_icon = (ImageView) ib.findViewById(R.id.custom_tab_icon);
      custom_tab_icon.setVisibility(View.VISIBLE);
   }

   public void setContentView() {

   }

   public void initializeViews() {

   }

   public void initializeData() {
   }

   @Override public void onResume() {
      super.onResume();
      Trace.d("ProgramListFragment onresume");

      onEvent(null);
   }

   BadgeView badgeView = null;

   public void onEvent(UpDateUnReadEvent token_event) {

      if (badgeView == null || RongIM.getInstance().getRongIMClient() == null) {
         return;
      }

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

      Trace.d("ProgramListFragment onEvent(UpDateUnReadEvent count:" + count);
   }

   @Override public void onPause() {
      super.onPause();
      Trace.d("ProgramListFragment onPause");
   }

   @Override public void onDestroy() {
      super.onDestroy();
      Trace.d("ProgramListFragment onDestroy");
      EventBus.getDefault().unregister(this);
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {
         case R.id.live_list:

            break;

         default:
            break;
      }
   }

   public void jumpHot() {
      if (viewPager == null) {
         System.out.println("viewpager为空");
         return;
      }
      viewPager.setCurrentItem(1);
   }
}
