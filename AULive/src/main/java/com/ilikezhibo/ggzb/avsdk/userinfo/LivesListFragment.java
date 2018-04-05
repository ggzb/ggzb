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

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
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

@SuppressLint("ValidFragment") public class LivesListFragment extends BaseFragment
    implements OnClickListener, PullToRefreshView.OnRefreshListener, OnItemClickListener,
    OnItemLongClickListener {

   private View view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<UserInfoDialogEntity> entities;
   private LivesListAdapter groupFragmentAdapter;

   private String uid;

   public LivesListFragment() {

      this.uid = AULiveApplication.getUserInfo().getUid();
   }

   //在个人主页使用，隐藏topBar
   private boolean is_use_in_homepage=false;
   public LivesListFragment(String uid) {
      this.uid = uid;
      is_use_in_homepage=true;
   }
   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_atten_list_layout, null);

      msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) view.findViewById(R.id.title);
      title.setText("直播列表");

      //返回可见
      view.findViewById(R.id.topLayout).setVisibility(View.VISIBLE);

      Button rl_back = (Button) view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      if(is_use_in_homepage){
         view.findViewById(R.id.topLayout).setVisibility(View.GONE);
      }

      entities = new ArrayList<UserInfoDialogEntity>();

      home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);
      home_listview.setOnItemLongClickListener(this);

      groupFragmentAdapter = new LivesListAdapter(this.getActivity());
      home_listview.setAdapter(groupFragmentAdapter);
      groupFragmentAdapter.setEntities(entities);

      //View header =
      //    LayoutInflater.from(this.getContext()).inflate(R.layout.gift_contributor_header, null);
      //TextView txt_total_coin = (TextView) header.findViewById(R.id.txt_total_coin);
      //txt_total_coin.setText(AULiveApplication.getUserInfo().recv_diamond + "");
      //home_listview.addHeaderView(header);

      startProgressDialog();
   // 开始获取数据
      home_listview.initRefresh(PullToRefreshView.HEADER);
      //Utils.showCroutonText(LivesListFragment.this.getActivity(),"长按删除");
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
            Intent login_intent =
                new Intent(LivesListFragment.this.getActivity(), LoginActivity.class);
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
      UserInfoDialogEntity entity = (UserInfoDialogEntity) parent.getAdapter().getItem(position);

      //MemberInfo memberInfo =
      //    new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
      //
      //PopupWindowUtil popupWindow1 = new PopupWindowUtil(home_listview);
      //popupWindow1.setContentView(R.layout.dialog_myroom_userinfo);
      //popupWindow1.setOutsideTouchable(true);
      //UserInfoHelper userInfoHelper =
      //    new UserInfoHelper(home_listview, popupWindow1, FansListFragment.this.getActivity(),
      //        memberInfo);

      startActivity(new Intent(LivesListFragment.this.getActivity(), AvActivity.class).putExtra(
          AvActivity.GET_UID_KEY, uid)
          .putExtra(AvActivity.IS_CREATER_KEY, false)
          .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_FACE, entity.getFace())
          .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_NICKNAME, entity.getNickname())
          .putExtra(AvActivity.EXTRA_RECIVE_DIAMOND, entity.getRecv_diamond())
          //.putExtra(AvActivity.EXTRA_SYS_MSG, null)
          .putExtra(AvActivity.EXTRA_IS_ON_SHOW, true)
          .putExtra(AvActivity.EXTRA_ONLINE_NUM, 1000)
          .putExtra(AvActivity.EXTRA_IS_MANAGER, false)
          .putExtra(AvActivity.EXTRA_IS_GAG, true)
          .putExtra(AvActivity.EXTRA_IS_SUPER_MANAGER, false)
          .putExtra(AvActivity.EXTRA_play_url_KEY, entity.url)
          .putExtra(AvActivity.EXTRA_MSG_SEND_GRADE_CONTROL, 0)
          .putExtra(AvActivity.EXTRA_IS_RECORD, true).putExtra(AvActivity.EXTRA_IS_RECORD_ID, entity.id).putExtra(AvActivity.GET_GRADE_KEY,
              entity.getGrade()));
   }

   @Override public void onRefresh(final int mode) {

      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(
          UrlHelper.SERVER_URL + "home/dianbo" + "?page=" + currPage + "&uid=" + uid);
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
                      new Intent(LivesListFragment.this.getActivity(), LoginActivity.class);
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

   // /删除
   private void doDelete(final UserInfoDialogEntity entity) {

      Trace.d("doDelete id:"+entity.id);
      RequestInformation request =
          new RequestInformation(com.jack.utils.UrlHelper.URL_HEAD + "/home/delvdoid?id=" + entity.id,
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               entities.remove(entity);
               groupFragmentAdapter.notifyDataSetChanged();
               Utils.showCroutonText(LivesListFragment.this.getActivity(),"删除成功");
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void showPromptDialog(final UserInfoDialogEntity entity) {
      final CustomDialog customDialog =
          new CustomDialog(LivesListFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      doDelete(entity);
                }
             }
          });

      customDialog.setCustomMessage("确认要取消关注吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      UserInfoDialogEntity entity = (UserInfoDialogEntity) parent.getAdapter().getItem(position);
      showPromptDialog(entity);
      return true;
   }
}
