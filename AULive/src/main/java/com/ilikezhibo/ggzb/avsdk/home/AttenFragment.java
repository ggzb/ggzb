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
import com.ilikezhibo.ggzb.views.VariousDialog;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVListEntity;
import com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper.LiveListEvent;
import com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

/**
 * Created by big on 2016/3/25.
 */
public class AttenFragment extends BaseFragment
    implements View.OnClickListener, PullToRefreshView.OnRefreshListener,
    AdapterView.OnItemClickListener {

   private UserInfo mSelfUserInfo;
   private AVEntity mChoseLiveVideoInfo = null;

   private View view;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<AVEntity> entities;
   private LiveVideoInfoAdapter groupFragmentAdapter;

   private String uid;
   private ProgramListFragment mProgramListFragment;

   public AttenFragment() {

      this.uid = AULiveApplication.getUserInfo().getUid();
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      mProgramListFragment = new ProgramListFragment();

      view = inflater.inflate(R.layout.fragment_atten_list_layout, null);
      //服务更新

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

      groupFragmentAdapter = new LiveVideoInfoAdapter(this.getActivity(), R.layout.live_item);
      home_listview.setAdapter(groupFragmentAdapter);
      groupFragmentAdapter.setEntities(entities);

      startProgressDialog();
      // 开始获取数据
      home_listview.initRefresh(PullToRefreshView.HEADER);
      return view;
   }

   @Override public void onResume() {
      super.onResume();
      mSelfUserInfo = AULiveApplication.getMyselfUserInfo();
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
            Intent login_intent = new Intent(AttenFragment.this.getActivity(), LoginActivity.class);
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
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }
      mChoseLiveVideoInfo = (AVEntity) parent.getAdapter().getItem(position);
      if (mChoseLiveVideoInfo != null && mChoseLiveVideoInfo.uid.equals(
              mSelfUserInfo.getUserPhone())) {
         Utils.showCroutonText(AttenFragment.this.getActivity(), "不能加入自己创建的房间");
         return;
      }
      if (mChoseLiveVideoInfo.payliving == 1) {
         if (AULiveApplication.getUserInfo().diamond > 10) {
            VariousDialog dialog = new VariousDialog(AttenFragment.this.getActivity());
            dialog.payDialog();
            dialog.setDialogListener(new VariousDialog.DialogListener() {
               @Override
               public void buttonClick(boolean payResult) {
                  if (payResult) {
                     enterRoom(mChoseLiveVideoInfo.uid, mSelfUserInfo.getUserPhone(), mChoseLiveVideoInfo.url);
                  }
               }
            });

         } else {
            Utils.showCroutonText(AttenFragment.this.getActivity(), "余额不足");
            return;
         }
      } else {
         enterRoom(mChoseLiveVideoInfo.uid, mSelfUserInfo.getUserPhone(), mChoseLiveVideoInfo.url);
      }
      //预加载
      //LiveFragment.doPreLoad(mChoseLiveVideoInfo.url);
//      enterRoom(mChoseLiveVideoInfo.uid, mSelfUserInfo.getUserPhone(), mChoseLiveVideoInfo.url);
   }

   //进入房间接口
   private void enterRoom(String liveuid, final String userid, final String url) {
      RequestInformation request = null;

      try {
         StringBuilder sb =
             new StringBuilder(UrlHelper.enterRoomUrl + "?liveuid=" + liveuid + "&userid=" + userid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
         request.addPostParams("roomid", liveuid + "");
         request.addPostParams("userid", userid);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<EnterRoomEntity>() {

         @Override public void onCallback(EnterRoomEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }
            try {
               if (callback.getStat() == 200) {

                  MedalListEvent medalListEvent = new MedalListEvent();
                  medalListEvent.anchor_medal = callback.anchor_medal;
                  medalListEvent.wanjia_medal = callback.wanjia_medal;
                  medalListEvent.act= callback.act;
                  EventBus.getDefault().postSticky(medalListEvent);

                  startActivity(
                      new Intent(AttenFragment.this.getActivity(), AvActivity.class).putExtra(
                          AvActivity.GET_UID_KEY, mChoseLiveVideoInfo.uid)
                          .putExtra(AvActivity.IS_CREATER_KEY, false)
                          .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_FACE, mChoseLiveVideoInfo.face)
                          .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_NICKNAME,
                              mChoseLiveVideoInfo.nickname)
                          .putExtra(AvActivity.EXTRA_RECIVE_DIAMOND, callback.recv_diamond)
                          .putExtra(AvActivity.EXTRA_SYS_MSG, callback.sys_msg)
                          .putExtra(AvActivity.EXTRA_IS_ON_SHOW, callback.is_live)
                          .putExtra(AvActivity.EXTRA_ONLINE_NUM, callback.total)
                          .putExtra(AvActivity.EXTRA_IS_MANAGER, callback.is_manager)
                          .putExtra(AvActivity.EXTRA_IS_GAG, callback.is_gag)
                          .putExtra(AvActivity.EXTRA_IS_SUPER_MANAGER, callback.show_manager)
                          .putExtra(AvActivity.EXTRA_play_url_KEY, url)
                          .putExtra(AvActivity.EXTRA_MSG_SEND_GRADE_CONTROL,
                              callback.sendmsg_grade).putExtra(AvActivity.GET_GRADE_KEY,
                          mChoseLiveVideoInfo.grade));

               } else {

               }
            } catch (Exception e) {
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }

   private long last_time;

   @Override public void onRefresh(final int mode) {

      Trace.d("AttenFragment void onRefresh ");
      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      if (mode == PullToRefreshView.HEADER) {
         last_time = 0;
      }
      //page作废，暂时
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(UrlHelper.getLiveAttenListUrl
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
               if (mode == PullToRefreshView.HEADER) {
                  // 当为第一页时，初始化广告栏
                  if (callback.getAdv() != null && callback.getAdv().size() > 0) {
                     advEntities = callback.getAdv();
                     listHeadView.findViewById(R.id.viewpager_frameLayout)
                         .setVisibility(View.VISIBLE);
                     initComtent(listHeadView, advEntities);
                  } else {
                     listHeadView.findViewById(R.id.viewpager_frameLayout).setVisibility(View.GONE);
                  }
               }
               if (callback.getList() != null) {
                  ArrayList<AVEntity> avList = callback.getList();

                  if (avList != null && avList.size() > 0) {
                     //last_time,用于分页
                     last_time = avList.get(avList.size() - 1).time;

                     for (AVEntity entity : avList) {
                        //下发,预加载
                        LiveFragment.doPreLoad(entity.url);
                     }
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
                     view.findViewById(R.id.iv_kuangkuang).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if (mProgramListFragment == null) {
                              System.out.println("广场为空");
                              return;
                           }
                           mProgramListFragment.jumpHot();
                        }
                     });
                     home_listview.setVisibility(View.VISIBLE);
                  } else {
                     view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.GONE);
                     home_listview.setVisibility(View.VISIBLE);
                  }
               } else {
                  if (callback.getStat() == 500 && has_ask_login == false) {
                     has_ask_login = true;
                     Intent login_intent =
                         new Intent(AttenFragment.this.getActivity(), LoginActivity.class);
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
   private CustomProgressDialog progressDialog = null;

   private void startProgressDialog() {
      //try {
      //   if (progressDialog == null) {
      //      progressDialog = CustomProgressDialog.createDialog(this.getActivity());
      //      progressDialog.setMessage("加载中");
      //   }
      //   progressDialog.show();
      //} catch (Exception e) {
      //
      //}
   }

   private void stopProgressDialog() {
      //try {
      //   if (progressDialog != null) {
      //      progressDialog.dismiss();
      //      progressDialog = null;
      //   }
      //} catch (Exception e) {
      //
      //}
   }

   public void canReflesh() {
      if (entities != null && entities.size() > 0) {
         home_listview.initRefresh(PullToRefreshView.HEADER);
      }
   }

   //////////////////////////////////////////
   //方法2
   private ArrayList<AdvEntity> advEntities;
   private View listHeadView;
   private RollPagerView mRollViewPager;
   private LoopPageAdapter loopPageAdapter;

   private void initComtent(View view, ArrayList<AdvEntity> advEntities) {
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

   //防销毁
   /** Fragment当前状态是否可见 */
   protected boolean isVisible;

   //setUserVisibleHint  adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
   @Override public void setUserVisibleHint(boolean isVisibleToUser) {
      super.setUserVisibleHint(isVisibleToUser);
      if (isVisibleToUser) {
         isVisible = true;
         onVisible();
      } else {
         isVisible = false;
         onInvisible();
      }
   }

   /**
    * 可见
    */
   protected void onVisible() {
      //canReflesh();
   }

   /**
    * 不可见
    */
   protected void onInvisible() {

   }
}
