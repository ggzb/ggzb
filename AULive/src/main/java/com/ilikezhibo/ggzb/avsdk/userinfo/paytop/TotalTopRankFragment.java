package com.ilikezhibo.ggzb.avsdk.userinfo.paytop;

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
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListAdapter;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.DayTopRankFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.WeekTopRankFragment;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

/**
 * Created by hasee on 2017/9/4.
 */

public class TotalTopRankFragment extends BaseFragment {

    public final static String TAG = "TotalTopRankFragment";
    private NavigationListener listener;
    private View root_view;

    private PrivateChatListAdapter viewpager_adapter;
    public static int current_selected_index = 1;

    private TotalRankFragment userrankfragment;
    private TotalRankFragment anchorrankfragment;

    private TopRankFragment topRankFragment;
    private WeekTopRankFragment weekTopRankFragment;
    private DayTopRankFragment dayTopRankFragment;

    private ViewGroup tab;
    private ViewPager viewPager;
    private String uid = "14088887";
    public TotalTopRankFragment(){

    }
/*
    public TotalTopRankFragment(NavigationListener listener) {
        this.listener = listener;
    }
*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityStackManager.getInstance().pushActivity(getActivity());
        root_view = inflater.inflate(R.layout.total_top_rank_layout, null);
        tab = (ViewGroup) root_view.findViewById(R.id.tab);
        tab.addView(LayoutInflater.from(this.getActivity())
                .inflate(R.layout.custom_total_top_rank_new_layout, tab, false));
        tab.findViewById(R.id.button1).setVisibility(View.GONE);
        tab.findViewById(R.id.clean_unread_tv).setVisibility(View.GONE);
        tab.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) root_view.findViewById(R.id.viewpager);
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
                                AULiveApplication.mContext.getResources().getColor(R.color.white));
                    } else {
                        custom_tab_text.setTextColor(
                                AULiveApplication.mContext.getResources().getColor(R.color.line_color));
                    }
                    custom_tab_icon.setVisibility(View.INVISIBLE);
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });


        userrankfragment = new TotalRankFragment(TotalRankFragment.TYPE_USER);
        anchorrankfragment = new TotalRankFragment(TotalRankFragment.TYPE_ANCHOR);
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        fragments.add(anchorrankfragment);
        fragments.add(userrankfragment);
        String[] tabs = { "主播榜","土豪榜"};

        viewpager_adapter =
                new PrivateChatListAdapter(TotalTopRankFragment.this.getChildFragmentManager(), fragments,
                        tabs);
        viewPager.setAdapter(viewpager_adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
        return  root_view;
    }
}
