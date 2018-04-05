package com.ilikezhibo.ggzb.avsdk.userinfo.toprank;

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
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

import java.util.ArrayList;

/**
 * @author big
 * @ClassName: QiuZuFragment
 * @Description:消息的fragment
 * @date 2014-3-19 下午9:49:31
 */

@SuppressLint("ValidFragment") public class WeekTopRankFragment extends BaseFragment
    implements OnClickListener, PullToRefreshView.OnRefreshListener, OnItemClickListener,
    OnItemLongClickListener {

   private View view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<TopRankEntity> entities;
   private TopRankAdapter groupFragmentAdapter;

   private String uid;
   private TextView txt_total_coin;

   public WeekTopRankFragment(String uid) {
      this.uid = uid;
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.activity_toprank_layout, null);

      view.findViewById(R.id.topLayout).setVisibility(View.GONE);

      msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) view.findViewById(R.id.title);
      title.setText(Utils.trans(R.string.app_money) + "贡献榜");

      Button rl_back = (Button) view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      entities = new ArrayList<TopRankEntity>();

      home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);
      home_listview.setOnItemLongClickListener(this);

      groupFragmentAdapter = new TopRankAdapter(this.getActivity());
      home_listview.setAdapter(groupFragmentAdapter);
      groupFragmentAdapter.setEntities(entities);

      View header =
          LayoutInflater.from(this.getContext()).inflate(R.layout.gift_contributor_header, null);
      txt_total_coin = (TextView) header.findViewById(R.id.txt_total_coin);
      //txt_total_coin.setText(AULiveApplication.getUserInfo().recv_diamond + "");
      home_listview.addHeaderView(header);

      startProgressDialog();

      return view;
   }

   @Override public void onResume() {
      super.onResume();
      // 开始获取数据
      home_listview.initRefresh(PullToRefreshView.HEADER);
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
                new Intent(WeekTopRankFragment.this.getActivity(), LoginActivity.class);
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
      TopRankEntity entity = (TopRankEntity) parent.getAdapter().getItem(position);

      //MemberInfo memberInfo =
      //    new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
      //final PopupWindowUtil popupWindow = new PopupWindowUtil(home_listview);
      //popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
      //popupWindow.setOutsideTouchable(true);
      //popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
      //   @Override public void onDismiss() {
      //
      //   }
      //});
      ////做View init
      //UserInfoHelper userInfoHelper =
      //    new UserInfoHelper(home_listview, popupWindow, this.getActivity(), memberInfo);
      Intent homepage_Intent =
          new Intent(WeekTopRankFragment.this.getActivity(), HomePageActivity.class);
      if (homepage_Intent == null || entity == null || entity.getUid() == null) {
         return;
      }
      homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, entity.getUid());
      WeekTopRankFragment.this.getActivity().startActivity(homepage_Intent);
   }

   @Override public void onRefresh(final int mode) {

      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(
          UrlHelper.SERVER_URL + "live/weekrank" + "?page=" + currPage + "&liveuid=" + uid);
      Trace.d(sb.toString());
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<TopRankListEntity>() {

         @Override public void onCallback(TopRankListEntity callback) {
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
               if (currPage == 1) {
                  txt_total_coin.setText(callback.total + "");
               }
               msgInfoTv.setVisibility(View.GONE);
               if (mode == PullToRefreshView.HEADER) {
                  entities.clear();
               }

               if (callback.getList() != null) {
                  //测试，加4次
                  entities.addAll(callback.getList());
                  //entities.addAll(callback.getList());
                  //entities.addAll(callback.getList());
                  //entities.addAll(callback.getList());
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
                      new Intent(WeekTopRankFragment.this.getActivity(), LoginActivity.class);
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
      }.setReturnType(TopRankListEntity.class));

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
   private void doDelete(final TopRankEntity qiuZuEntity) {

      RequestInformation request =
          new RequestInformation(com.jack.utils.UrlHelper.URL_HEAD + "/friend/del?user=" + qiuZuEntity.getUid(),
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               entities.remove(qiuZuEntity);
               groupFragmentAdapter.notifyDataSetChanged();
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

   private void showPromptDialog(final TopRankEntity qiuZuEntity) {
      final CustomDialog customDialog =
          new CustomDialog(WeekTopRankFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      doDelete(qiuZuEntity);
                }
             }
          });

      customDialog.setCustomMessage("确认要删除此好友吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      //TopRankEntity entity = (TopRankEntity) parent.getAdapter().getItem(position);
      //showPromptDialog(entity);
      return true;
   }
}
