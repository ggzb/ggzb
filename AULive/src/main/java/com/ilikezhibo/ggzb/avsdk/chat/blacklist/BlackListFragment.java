package com.ilikezhibo.ggzb.avsdk.chat.blacklist;

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
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: BlackListFragment
 * @Description:关注排行
 * @date 2014-3-19 下午9:49:31
 */

@SuppressLint("ValidFragment") public class BlackListFragment extends BaseFragment
    implements OnClickListener, PullToRefreshView.OnRefreshListener, OnItemClickListener,
    OnItemLongClickListener {

   private View view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<String> entities;
   private BlackListAdapter groupFragmentAdapter;

   private String uid;

   public BlackListFragment() {

      this.uid = AULiveApplication.getUserInfo().getUid();
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_blacklist_layout, null);

      msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) view.findViewById(R.id.title);
      title.setText("黑名单列表");

      //返回可见
      view.findViewById(R.id.topLayout).setVisibility(View.VISIBLE);

      Button rl_back = (Button) view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      entities = new ArrayList<String>();

      home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);
      home_listview.setOnItemLongClickListener(this);

      groupFragmentAdapter = new BlackListAdapter(this.getActivity());
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
      home_listview.initRefresh(PullToRefreshView.HEADER);
      Utils.showCroutonText(BlackListFragment.this.getActivity(), "长按删除黑名单");
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
                new Intent(BlackListFragment.this.getActivity(), LoginActivity.class);
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
      String uid = (String) parent.getAdapter().getItem(position);

      Intent homepage_Intent =
          new Intent(BlackListFragment.this.getActivity(), HomePageActivity.class);
      homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, uid);
      BlackListFragment.this.getActivity().startActivity(homepage_Intent);
   }

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      String entity = (String) parent.getAdapter().getItem(position);
      showPromptDialog(entity);
      return true;
   }

   @Override public void onRefresh(final int mode) {
      try {
         RongIM.getInstance()
             .getRongIMClient()
             .getBlacklist(new RongIMClient.GetBlacklistCallback() {
                @Override public void onSuccess(String[] strings) {

                   entities.clear();
                   if ((strings != null && strings.length != 0)) {

                      for (String s : strings) {
                         entities.add(s);
                      }

                      groupFragmentAdapter.setEntities(entities);
                      groupFragmentAdapter.notifyDataSetChanged();
                   }
                   if ((entities == null || entities.size() == 0)) {
                      view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
                      home_listview.setVisibility(View.GONE);
                   } else {
                      view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.GONE);
                      home_listview.setVisibility(View.VISIBLE);
                   }
                   home_listview.onRefreshComplete(mode, true);
                   stopProgressDialog();
                }

                @Override public void onError(RongIMClient.ErrorCode errorCode) {
                   stopProgressDialog();
                }
             });
      } catch (Exception e) {
      }
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
   private void doDelete(final String uid) {
      RongIM.getInstance()
          .getRongIMClient()
          .removeFromBlacklist(uid, new RongIMClient.OperationCallback() {
             @Override public void onSuccess() {
//                Utils.showCroutonText(BlackListFragment.this.getActivity(), "成功拉黑");
                //刷新
                home_listview.initRefresh(PullToRefreshView.HEADER);
             }

             @Override public void onError(RongIMClient.ErrorCode errorCode) {

             }
          });
      Utils.showCroutonText(BlackListFragment.this.getActivity(), "解除拉黑");
   }

   private void showPromptDialog(final String uid) {
      final CustomDialog customDialog =
          new CustomDialog(BlackListFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      doDelete(uid);
                }
             }
          });

      customDialog.setCustomMessage("确认将此用户移出黑名单吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }
}
