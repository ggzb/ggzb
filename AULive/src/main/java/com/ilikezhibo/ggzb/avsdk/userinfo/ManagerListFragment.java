package com.ilikezhibo.ggzb.avsdk.userinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.PixelDpHelper;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.entity.UidListEntity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

import java.util.ArrayList;

/**
 * @author big
 * @ClassName: BlackListFragment
 * @Description:关注排行
 * @date 2014-3-19 下午9:49:31
 */

@SuppressLint("ValidFragment") public class ManagerListFragment extends BaseFragment
    implements OnClickListener, PullToRefreshView.OnRefreshListener, OnItemClickListener,
    OnItemLongClickListener {

   private View view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<UserInfoDialogEntity> entities;
   private DialgoListAdapter groupFragmentAdapter;

   public ManagerListFragment() {

   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_atten_list_layout, null);

      msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) view.findViewById(R.id.title);
      title.setText("管理员列表");

      //返回可见
      view.findViewById(R.id.topLayout).setVisibility(View.VISIBLE);

      Button rl_back = (Button) view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      entities = new ArrayList<UserInfoDialogEntity>();

      home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);
      home_listview.setOnItemLongClickListener(this);

      groupFragmentAdapter = new DialgoListAdapter(this.getActivity());
      home_listview.setAdapter(groupFragmentAdapter);
      groupFragmentAdapter.setEntities(entities);

      //View header =
      //    LayoutInflater.from(this.getContext()).inflate(R.layout.gift_contributor_header, null);
      //TextView txt_total_coin = (TextView) header.findViewById(R.id.txt_total_coin);
      //txt_total_coin.setText(AULiveApplication.getUserInfo().recv_diamond + "");
      //home_listview.addHeaderView(header);

      startProgressDialog();

      return view;
   }

   @Override public void onResume() {
      super.onResume();
      // 开始获取数据
      Utils.showCroutonText(ManagerListFragment.this.getActivity(), "长按删除管理员");
      home_listview.initRefresh(PullToRefreshView.HEADER);
   }


   @Override public void onClick(View v) {
      switch (v.getId()) {
         case R.id.search_bt:

            break;
         case R.id.righ_bt:

            break;
         case R.id.back:
            AvActivity avActivity = (AvActivity) this.getActivity();
            View fragment_container = avActivity.findViewById(R.id.mManagerList);
            ViewAnimator.animate(fragment_container)
                .translationX(0, PixelDpHelper.dip2px(avActivity, 500))
                .duration(300)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      AvActivity avActivity = (AvActivity) ManagerListFragment.this.getActivity();
                      avActivity.findViewById(R.id.mManagerList).setVisibility(View.GONE);
                   }
                }).start();

            break;
         case R.id.msgInfoTv:
            Intent login_intent =
                new Intent(ManagerListFragment.this.getActivity(), LoginActivity.class);
            startActivity(login_intent);
            break;
      }
   }

   @Override public void onDetach() {
      super.onDetach();
   }

   @Override public void onDestroy() {
      super.onDestroy();
      Trace.d("ManagerListFragment onDestroy");
      home_listview=null;
   }

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      UserInfoDialogEntity entity = (UserInfoDialogEntity) parent.getAdapter().getItem(position);

      //MemberInfo memberInfo =
      //    new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
      //
      //PopupWindowUtil popupWindow1 = new PopupWindowUtil(home_listview);
      //popupWindow1.setContentView(R.layout.dialog_myroom_userinfo);
      //popupWindow1.setOutsideTouchable(true);
      //UserInfoHelper userInfoHelper =
      //    new UserInfoHelper(home_listview, popupWindow1, BlackListFragment.this.getActivity(),
      //        memberInfo);

      //不能离开直播界面的activity
      //Intent homepage_Intent =
      //    new Intent(ManagerListFragment.this.getActivity(), HomePageActivity.class);
      //homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, entity.getUid());
      //ManagerListFragment.this.getActivity().startActivity(homepage_Intent);
   }

   @Override public void onRefresh(final int mode) {

      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(UrlHelper.SET_MANAGER_LIST_ROOM + "?page=" + currPage);
      Trace.d(sb.toString());
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UserInfoDialogListEntity>() {

         @Override public void onCallback(UserInfoDialogListEntity callback) {
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
                  entities.addAll(callback.getList());
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
                   || callback.getList().size() == 0)) {
                  view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
                  home_listview.setVisibility(View.GONE);
               }
            } else {
               if (callback.getStat() == 500 && has_ask_login == false) {
                  has_ask_login = true;
                  Intent login_intent =
                      new Intent(ManagerListFragment.this.getActivity(), LoginActivity.class);
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
      }.setReturnType(UserInfoDialogListEntity.class));

      request.execute();
   }

   // 只提示一次登录
   private boolean has_ask_login = false;

   private void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(this.getActivity());
         progressDialog.setMessage("正在加载中...");
      }

      progressDialog.show();
   }

   private void stopProgressDialog() {
      if (progressDialog != null) {
         progressDialog.dismiss();
         progressDialog = null;
      }
   }

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

      UserInfoDialogEntity entity = (UserInfoDialogEntity) parent.getAdapter().getItem(position);
      showPromptDialog(entity);
      return true;
   }

   private void showPromptDialog(final UserInfoDialogEntity entity) {
      final CustomDialog customDialog =
          new CustomDialog(ManagerListFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      removeManager(entity);
                }
             }
          });

      customDialog.setCustomMessage("确认要删除吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   public void removeManager(final UserInfoDialogEntity entity) {
      RequestInformation request = new RequestInformation(UrlHelper.SET_MANAGER_ROOM
          + "?liveuid="
          + AULiveApplication.currLiveUid
          + "&user="
          + entity.getUid()
          + "&type=0", RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UidListEntity>() {

         @Override public void onCallback(UidListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               AvActivity avActivity = (AvActivity) ManagerListFragment.this.getActivity();
               avActivity.is_manager=false;
               Utils.showMessage("设置成功");

               LoginUserEntity userinfo = new LoginUserEntity();
               userinfo.setUid(entity.getUid());
               userinfo.setNickname(entity.getNickname());
               userinfo.setFace(entity.getFace());
               home_listview.initRefresh(PullToRefreshView.HEADER);
               avActivity.sendManagerAndGagMsg(userinfo, "remove_manager");
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(UidListEntity.class));
      request.execute();
   }
}
