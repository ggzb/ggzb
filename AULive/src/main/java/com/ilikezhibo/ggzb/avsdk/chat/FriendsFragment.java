package com.ilikezhibo.ggzb.avsdk.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.chat.event.CleanUnreadEvent;
import com.ilikezhibo.ggzb.avsdk.chat.event.RefreshChatListEvent;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatHelper;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListHelper;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import java.util.ArrayList;
import java.util.List;
import tinker.android.util.TinkerManager;

/**
 * Created by big on 2016/3/25.
 */
public class FriendsFragment extends BaseFragment
    implements View.OnClickListener, AdapterView.OnItemLongClickListener,
    AdapterView.OnItemClickListener, PullToRefreshView.OnRefreshListener {
   private UserInfo mSelfUserInfo;

   private View view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;

   private TextView msgInfoTv;
   private ArrayList<Conversation> entities;
   private FriendChatListAdapter groupFragmentAdapter;

   private String uid;

   public FriendsFragment() {

      this.uid = AULiveApplication.getUserInfo().getUid();
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_chat_list_layout, null);

      ActivityStackManager.getInstance().pushActivity(getActivity());

      AULiveApplication mAULiveApplication =
          (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      mSelfUserInfo = mAULiveApplication.getMyselfUserInfo();

      //view init
      msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) view.findViewById(R.id.title);
      title.setText("好友列表");

      Button rl_back = (Button) view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      entities = new ArrayList<Conversation>();

      home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);
      home_listview.setOnItemLongClickListener(this);

      groupFragmentAdapter = new FriendChatListAdapter(FriendsFragment.this.getActivity());
      home_listview.setAdapter(groupFragmentAdapter);
      groupFragmentAdapter.setEntities(entities);

      startProgressDialog();

      EventBus.getDefault().register(this);
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
                new Intent(FriendsFragment.this.getActivity(), LoginActivity.class);
            startActivity(login_intent);
            break;
      }
   }

   @Override public void onDetach() {
      super.onDetach();
   }

   @Override public void onDestroy() {
      super.onDestroy();
      EventBus.getDefault().unregister(this);
   }

   //判断是否要调用半屏的聊天界面
   public PrivateChatHelper privateChatHelper;
   public PrivateChatListHelper privateChatListHelper;

   public void setPrivateChatHelper(PrivateChatListHelper privateChatListHelper1,
       PrivateChatHelper privateChatHelper1) {
      privateChatHelper = privateChatHelper1;
      privateChatListHelper = privateChatListHelper1;
   }

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Conversation entity = (Conversation) parent.getAdapter().getItem(position);
      if (privateChatHelper != null) {
         privateChatHelper.startPrivateChat(entity.getTargetId(), entity.getSenderUserName());
         //关闭对话列表
         privateChatListHelper.onClosPrivate();
      } else {
         startActivityForResult(
             new Intent(FriendsFragment.this.getActivity(), PrivateChatActivity.class).putExtra(
                 PrivateChatActivity.STAR_PRIVATE_CHAT_BY_CODE_UID_KEY, entity.getTargetId()), 0);
      }
   }

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      Conversation entity = (Conversation) parent.getAdapter().getItem(position);
      showPromptDialog(entity);
      return true;
   }

   private void showPromptDialog(final Conversation entity) {
      final CustomDialog customDialog =
          new CustomDialog(FriendsFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      doDeleteChatList(entity);
                      break;
                }
             }
          });

      customDialog.setCustomMessage("确认要删除吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   private void doDeleteChatList(Conversation entity) {
      RongIM.getInstance()
          .getRongIMClient()
          .removeConversation(Conversation.ConversationType.PRIVATE, entity.getTargetId());
      //Utils.showMessage("删除成功");
      Utils.showCroutonText(FriendsFragment.this.getActivity(), "删除成功");
      //刷新
      home_listview.initRefresh(PullToRefreshView.HEADER);
   }

   public void onEvent(RefreshChatListEvent token_event) {
      if (privateChatHelper != null) {
         home_listview.initRefresh(PullToRefreshView.HEADER);
      }
   }

   @Override public void onRefresh(final int mode) {

      if (RongIM.getInstance().getRongIMClient() == null) {
         return;
      }

      RongIM.getInstance()
          .getRongIMClient()
          .getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
             @Override public void onSuccess(List<Conversation> conversations) {
                entities.clear();
                if ((conversations != null && conversations.size() != 0)) {
                   for (Conversation conversation : conversations) {
                      //Conversation是相对自己来说，getTargetId即是对方id,不管是对方发起还是自己发起的聊天
                      String sender_id=   conversation.getTargetId();
                      //存在关注列表，或10000号
                      if (MainActivity.atten_uids.contains(conversation.getTargetId())||sender_id.equals("10000")) {
                         entities.add(conversation);
                      }
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
          }, Conversation.ConversationType.PRIVATE);
   }

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

   public void onEvent(CleanUnreadEvent token_event) {

      RongIM.getInstance()
          .getRongIMClient()
          .getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
             @Override public void onSuccess(List<Conversation> conversations) {

                if ((conversations != null && conversations.size() != 0)) {
                   entities.clear();
                   for (Conversation conversation : conversations) {

                      //如果有未读，清空
                      if (conversation.getUnreadMessageCount() > 0) {
                         RongIM.getInstance()
                             .getRongIMClient()
                             .clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE,
                                 conversation.getTargetId(),
                                 new RongIMClient.ResultCallback<Boolean>() {
                                    @Override public void onSuccess(Boolean aBoolean) {

                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                 });
                      }
                      conversation.setUnreadMessageCount(0);
                      //存在关注列表
                      if (MainActivity.atten_uids.contains(conversation.getTargetId())) {
                         entities.add(conversation);
                      }
                   }
                   groupFragmentAdapter.setEntities(entities);
                   groupFragmentAdapter.notifyDataSetChanged();
                }
                if ((entities == null || entities.size() == 0)) {
                   if (view != null) {
                      view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
                      home_listview.setVisibility(View.GONE);
                   }
                } else {
                   if (view != null) {
                      view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.GONE);
                      home_listview.setVisibility(View.VISIBLE);
                   }
                }

                stopProgressDialog();
             }

             @Override public void onError(RongIMClient.ErrorCode errorCode) {
                stopProgressDialog();
             }
          }, Conversation.ConversationType.PRIVATE);
   }
}
