package com.ilikezhibo.ggzb.avsdk.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVListEntity;
import com.ilikezhibo.ggzb.avsdk.home.news.FenLeiAdapter;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.step.AdvEntity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;
import tinker.android.util.TinkerManager;

/**
 * Created by big on 2016/3/25.
 */
public class HideFragment extends BaseFragment
    implements View.OnClickListener, PullToRefreshView.OnRefreshListener,
    AdapterView.OnItemClickListener {
   private UserInfo mSelfUserInfo;
   private AdvEntity mChoseAdvEntity = null;

   private View view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<AVEntity> entities;
   private FenLeiAdapter groupFragmentAdapter;

   private String uid;

   public HideFragment() {

      this.uid = AULiveApplication.getUserInfo().getUid();
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_atten_list_layout, null);
      //服务更新
      ActivityStackManager.getInstance().pushActivity(getActivity());

      AULiveApplication mAULiveApplication =
          (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      mSelfUserInfo = mAULiveApplication.getMyselfUserInfo();

      //view init
      msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) view.findViewById(R.id.title);
      title.setText("最热列表");

      Button rl_back = (Button) view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      entities = new ArrayList<AVEntity>();

      home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);

      //viewPager
      listHeadView =
          LayoutInflater.from(this.getActivity()).inflate(R.layout.list_viewpager_item, null);
      home_listview.addHeaderView(listHeadView);
      home_listview.setHeaderDividersEnabled(false);

      groupFragmentAdapter = new FenLeiAdapter(this.getActivity());
      home_listview.setAdapter(groupFragmentAdapter);

      startProgressDialog();
      // 开始获取数据
      home_listview.initRefresh(PullToRefreshView.HEADER);
      return view;
   }

   @Override public void onResume() {
      super.onResume();
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {
         case R.id.search_bt:

            break;
         case R.id.righ_bt:

            break;
         case R.id.back:
            this.getActivity().finish();
            break;
         case R.id.msgInfoTv:
            Intent login_intent = new Intent(HideFragment.this.getActivity(), LoginActivity.class);
            startActivity(login_intent);
            break;
      }
   }

   @Override public void onDetach() {
      super.onDetach();
   }

   @Override public void onDestroy() {
      super.onDestroy();
   }

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //mChoseAdvEntity = (AdvEntity) parent.getAdapter().getItem(position);
      //if (mChoseAdvEntity != null && mChoseAdvEntity.uid.equals(mSelfUserInfo.getUserPhone())) {
      //   Toast.makeText(NewsFragment.this.getContext(), "不能加入自己创建的房间", Toast.LENGTH_SHORT).show();
      //   return;
      //}
      //mSelfUserInfo.setIsCreater(false);
      //
      //Trace.d("NewFragment onItemClick");
      //enterRoom(mChoseAdvEntity.uid, mSelfUserInfo.getUserPhone());
   }

   private long last_time;

   @Override public void onRefresh(final int mode) {

      //if (BtnClickUtils.isFastDoubleClick()) {
      //   return;
      //}

      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      if (mode == PullToRefreshView.HEADER) {
         last_time = 0;
      }
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(UrlHelper.SERVER_URL
          + "live/hide"
          + "?page_remove="
          + currPage
          + "&liveuid="
          + uid
          + "&time="
          + last_time);
      Trace.d(sb.toString());
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<AVListEntity>() {

         @Override public void onCallback(AVListEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               currPage--;
               msgInfoTv.setText(R.string.get_info_fail);
               msgInfoTv.setVisibility(View.VISIBLE);
               home_listview.setVisibility(View.VISIBLE);
               home_listview.onRefreshComplete(mode, false);
               home_listview.enableFooter(false);

               return;
            }

            if (callback.getStat() == 200) {
               msgInfoTv.setVisibility(View.GONE);
               if (mode == PullToRefreshView.HEADER) {
                  entities.clear();
               }

               if (callback.getList() != null) {
                  ArrayList<AVEntity> avList = callback.getList();

                  List<AdvEntity> tmplist = new ArrayList<AdvEntity>();

                  if (mode == PullToRefreshView.HEADER) {
                     // 当为第一页时，初始化广告栏
                     if (callback.getAdv() != null && callback.getAdv().size() > 0) {
                        advEntities = callback.getAdv();
                        listHeadView.findViewById(R.id.viewpager_frameLayout)
                            .setVisibility(View.VISIBLE);
                        initComtent(listHeadView, advEntities);
                     } else {
                        listHeadView.findViewById(R.id.viewpager_frameLayout)
                            .setVisibility(View.GONE);
                     }
                  }
                  if (avList != null && avList.size() > 0) {

                     last_time = avList.get(avList.size() - 1).time;

                     //for (AVEntity entity : avList) {
                     //   AdvEntity item =
                     //       new AdvEntity(entity.face, entity.nickname, entity.uid, entity.url);
                     //   tmplist.add(item);
                     //}

                     entities.addAll(avList);
                  }

                  groupFragmentAdapter.setEntities(entities);
                  groupFragmentAdapter.notifyDataSetChanged();
                  home_listview.onRefreshComplete(mode, true);

                  if (mode == PullToRefreshView.HEADER || (callback.getList() != null
                      && callback.getList().size() > 0)) {
                     home_listview.enableFooter(true);
                  } else {
                     home_listview.enableFooter(false);
                  }

                  //显示内容为空
                  if (mode == PullToRefreshView.HEADER && (callback.getList() == null
                      || callback.getList().size() == 0) && (callback.getAdv() == null
                      || callback.getAdv().size() == 0)) {
                     view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
//                     home_listview.setVisibility(View.GONE);
                  } else {
                     view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.GONE);
                     home_listview.setVisibility(View.VISIBLE);
                  }
               } else {
                  if (callback.getStat() == 500 && has_ask_login == false) {
                     has_ask_login = true;
                     Intent login_intent =
                         new Intent(HideFragment.this.getActivity(), LoginActivity.class);
                     startActivity(login_intent);
                  }
                  stopProgressDialog();
                  currPage--;
                  msgInfoTv.setText(callback.getMsg());
                  msgInfoTv.setVisibility(View.VISIBLE);
                  // 因为可能网络恢复，success改为true
                  home_listview.onRefreshComplete(mode, false);
                  home_listview.enableFooter(false);
               }
            }
         }

         @Override public void onFailure(AppException e) {
            stopProgressDialog();
            currPage--;
            entities.clear();
            // 因为可能网络恢复，success改为true
            home_listview.onRefreshComplete(mode, false);
            home_listview.enableFooter(false);

            msgInfoTv.setText(R.string.get_info_fail);
            msgInfoTv.setVisibility(View.VISIBLE);
         }
      }.setReturnType(AVListEntity.class));

      request.execute();
   }

   // 只提示一次登录
   private boolean has_ask_login = false;

   private void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(this.getActivity());
         progressDialog.setMessage("加载中");
      }

      progressDialog.show();
   }

   private void stopProgressDialog() {
      if (progressDialog != null) {
         progressDialog.dismiss();
         progressDialog = null;
      }
   }

   public void canReflesh() {
      if (entities.size() < 1) {
         home_listview.initRefresh(PullToRefreshView.HEADER);
      }
   }

   //////////////////////////////////////////
   //方法2
   private ArrayList<com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity> advEntities;
   private View listHeadView;
   private RollPagerView mRollViewPager;
   private LoopPageAdapter loopPageAdapter;

   private void initComtent(View view,
       ArrayList<com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity> advEntities) {
      if (mRollViewPager == null) {
         mRollViewPager = (RollPagerView) view.findViewById(R.id.vp_guide);
         mRollViewPager.setPlayDelay(4000);
         mRollViewPager.setAnimationDurtion(500);
         loopPageAdapter = new LoopPageAdapter(this.getActivity(), advEntities, mRollViewPager);
         mRollViewPager.setAdapter(loopPageAdapter);
//        mRollViewPager.setAdapter(new TestNomalAdapter());
         mRollViewPager.setHintView(
             new IconHintView(this.getActivity(), R.drawable.dot_gray, R.drawable.dot_white));
         //mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
         //mRollViewPager.setHintView(new TextHintView(this));
         //mRollViewPager.setHintView(null);
      }
      loopPageAdapter.setEntities(advEntities);
      loopPageAdapter.notifyDataSetChanged();
   }
}
