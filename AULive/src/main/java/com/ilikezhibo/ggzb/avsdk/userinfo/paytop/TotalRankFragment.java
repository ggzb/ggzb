package com.ilikezhibo.ggzb.avsdk.userinfo.paytop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListAdapter;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;
import com.ilikezhibo.ggzb.views.ChildViewPager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

/**
 * Created by hasee on 2017/9/4.
 */

public class TotalRankFragment extends BaseFragment {
    public static final int TYPE_USER = 0;
    public static final int TYPE_ANCHOR = 1;
    private int type;

    public final static String TAG = "TotalRankFragment";
    private NavigationListener listener;
    private View root_view;

    private PrivateChatListAdapter viewpager_adapter;
    public static int current_selected_index = 1;

    private TTTopRankFragment ttDayRankFragment;
    private TTTopRankFragment ttWeekRankFragment;
    private TTTopRankFragment ttTopRankFragment;

    private ViewGroup tab;
    private ChildViewPager viewPager;
    public TotalRankFragment(){

    }
    @SuppressLint("ValidFragment")
    public TotalRankFragment(int type){
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.total_child_rank_layout, null);
        tab = (ViewGroup) root_view.findViewById(R.id.tab);
        tab.addView(LayoutInflater.from(this.getActivity())
                .inflate(R.layout.custom_toprank_second_layout, tab, false));
        tab.findViewById(R.id.button1).setVisibility(View.GONE);
        tab.findViewById(R.id.clean_unread_tv).setVisibility(View.GONE);
        tab.setVisibility(View.VISIBLE);
        viewPager = (ChildViewPager) root_view.findViewById(R.id.viewpager);
        final SmartTabLayout viewPagerTab = (SmartTabLayout) root_view.findViewById(R.id.viewpagertab);
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
                                AULiveApplication.mContext.getResources().getColor(R.color.global_main_bg));
                    } else {
                        custom_tab_text.setTextColor(
                                AULiveApplication.mContext.getResources().getColor(R.color.global_text_color));
                    }
                    custom_tab_icon.setVisibility(View.INVISIBLE);
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        ttDayRankFragment = new TTTopRankFragment(type,TTTopRankFragment.TYPE_DAY);
        ttWeekRankFragment = new TTTopRankFragment(type,TTTopRankFragment.TYPE_WEEK);
        ttTopRankFragment = new TTTopRankFragment(type,TTTopRankFragment.TYPE_TOTAL);
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(ttDayRankFragment);
        fragments.add(ttWeekRankFragment);
        fragments.add(ttTopRankFragment);
        String[] tabs = { "日榜", "周榜" ,"总榜"};

        viewpager_adapter =
                new PrivateChatListAdapter(TotalRankFragment.this.getChildFragmentManager(), fragments,
                        tabs);
        viewPager.setAdapter(viewpager_adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
        return root_view;
    }
}
