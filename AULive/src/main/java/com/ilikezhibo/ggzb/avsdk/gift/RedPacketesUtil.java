package com.ilikezhibo.ggzb.avsdk.gift;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.SendGiftEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoDialogEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoDialogListEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoHelper;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import popwindow.PopupWindowUtil;

/**
 * Created by big on 3/28/16.
 */
public class RedPacketesUtil
    implements PullToRefreshView.OnRefreshListener, AdapterView.OnItemClickListener {

   private BaseFragmentActivity avActivity;
   private String uid;
   private String nickname;
   private String face;
   private static RedPacketesUtil redPacketesUtil = null;
   private View anchor;

   public static boolean is_show_redbag = false;
   //已经抢过的红包
   public static ArrayList<String> red_packet_ids = new ArrayList<String>();

   public static RedPacketesUtil getInstance(BaseFragmentActivity avActivity, String uid,
       String nickname, String face, View anchor) {

      if (redPacketesUtil == null) {
         redPacketesUtil = new RedPacketesUtil();
      }
      redPacketesUtil.avActivity = avActivity;
      redPacketesUtil.uid = uid;
      redPacketesUtil.face = face;
      redPacketesUtil.nickname = nickname;
      redPacketesUtil.anchor = anchor;
      return redPacketesUtil;
   }

   private void grabRedPackets(int redbag_id) {
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.getGrabRedBag
             + "?packetid="
             + redbag_id
             + "&liveuid="
             + AULiveApplication.currLiveUid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<SendGiftEntity>() {

         @Override public void onCallback(SendGiftEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               //更新还剩的diamond
               AULiveApplication.getUserInfo().diamond = callback.getDiamond();
               int grab = callback.getGrab();
               if (popupWindow1 != null) {
                  popupWindow1.dismiss();
               }
               if (callback.getMsg() != null && !callback.getMsg().equals("")) {
                  Utils.showCroutonText(avActivity, "" + callback.getMsg());
               }
               openRedBagPopUpWindows(grab);
            }
            if (callback.getStat() == 201 || callback.getStat() == 503) {
               //已经抢过
               int grab = callback.getGrab();
               if (popupWindow1 != null) {
                  popupWindow1.dismiss();
               }
               if (callback.getMsg() != null && !callback.getMsg().equals("")) {
                  Utils.showCroutonText(avActivity, "" + callback.getMsg());
               }
               openRedBagPopUpWindows(grab);
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(SendGiftEntity.class));
      request.execute();
   }

   PopupWindowUtil popupWindow1 = null;

   //记录是否是点击打开按钮而关闭PopupWindowUtil
   boolean is_click_open = false;

   private int packet_id_global;

   //将要打开的红包
   public void grabRedBagPopUpWindows(final int packet_id) {
      //正在显示红包动画
      packet_id_global = packet_id;

      is_show_redbag = true;

      //如果点击了已经抢过的红包
      if (red_packet_ids.contains(packet_id + "")) {
         grabRedPackets(packet_id);
         return;
      }

      popupWindow1 = new PopupWindowUtil(anchor);
      popupWindow1.setContentView(R.layout.dialog_room_redpacket);
      popupWindow1.setOutsideTouchable(true);

      popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
         @Override public void onDismiss() {
            //如果是点击打开而关闭红包，则不打开新红包
            if (is_click_open) {

            } else {
               is_show_redbag = false;

               //打开新红包
               if (avActivity instanceof AvActivity) {
                  AvActivity temp_activity = (AvActivity) avActivity;
                  //显示礼物动画
                  temp_activity.hasAnyRedBagGift();
               }
            }
            is_click_open = false;
         }
      });

      //头像
      ImageView face_iv = (ImageView) popupWindow1.findId(R.id.user_portrait);
      String face1 = Utils.getImgUrl(face);
      ImageLoader.getInstance()
          .displayImage(face1, face_iv, AULiveApplication.getGlobalImgOptions());

      TextView txt_username = (TextView) popupWindow1.findId(R.id.txt_username);
      txt_username.setText(nickname);

      ImageView img_close = (ImageView) popupWindow1.findId(R.id.img_close);
      img_close.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            is_click_open = false;
            popupWindow1.dismiss();
         }
      });

      //打开按钮
      popupWindow1.findId(R.id.img_open).setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            is_click_open = true;
            grabRedPackets(packet_id);
            //加入已经抢过的红包
            if (red_packet_ids.contains(packet_id + "")) {
            } else {
               red_packet_ids.add(packet_id + "");
            }
            popupWindow1.dismiss();
         }
      });
      popupWindow1.showCenter();
   }

   PopupWindowUtil popupWindow2 = null;
   View redpacket_history = null;
   LinearLayout list_layout;

   //打开了的红包
   public void openRedBagPopUpWindows(int grab) {
      is_show_redbag = true;
      popupWindow2 = new PopupWindowUtil(anchor);
      popupWindow2.setContentView(R.layout.dialog_room_redpacket_open);
      popupWindow2.setOutsideTouchable(true);

      popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
         @Override public void onDismiss() {
            is_show_redbag = false;
            //打开新红包

            if (avActivity instanceof AvActivity) {
               AvActivity temp_activity = (AvActivity) avActivity;
               //显示礼物动画
               temp_activity.hasAnyRedBagGift();
            }
         }
      });
      //头像
      ImageView face_iv = (ImageView) popupWindow2.findId(R.id.user_portrait);
      String face1 = Utils.getImgUrl(face);
      ImageLoader.getInstance()
          .displayImage(face1, face_iv, AULiveApplication.getGlobalImgOptions());

      TextView txt_username = (TextView) popupWindow2.findId(R.id.txt_username);
      txt_username.setText(nickname);

      //抢到的红包数
      TextView txt_received = (TextView) popupWindow2.findId(R.id.txt_received);
      txt_received.setText(grab + "");

      ImageView img_close = (ImageView) popupWindow2.findId(R.id.img_close);
      img_close.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            popupWindow2.dismiss();
         }
      });

      entities = new ArrayList<UserInfoDialogEntity>();

      dialog_list = (PullToRefreshView) popupWindow2.findId(R.id.dialog_list);
      dialgoListAdapter = new RedPacketDialgoListAdapter(avActivity);
      dialog_list.setAdapter(dialgoListAdapter);
      dialgoListAdapter.setEntities(entities);
      dialog_list.setOnRefreshListener(RedPacketesUtil.this);
      dialog_list.setOnItemClickListener(RedPacketesUtil.this);
      dialog_list.setRefreshAble(false);

      redpacket_history = popupWindow2.findId(R.id.redpacket_history);
      list_layout = (LinearLayout) popupWindow2.findId(R.id.list_layout);
      //看看大家的手气
      redpacket_history.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            redpacket_history.setVisibility(View.GONE);
            dialog_list.setVisibility(View.VISIBLE);
            list_layout.setVisibility(View.VISIBLE);

            dialog_list.initRefresh(PullToRefreshView.HEADER);
         }
      });

      popupWindow2.showCenter();
   }

   ////////////////////刷新相关

   private ArrayList<UserInfoDialogEntity> entities;
   private PullToRefreshView dialog_list;
   private RedPacketDialgoListAdapter dialgoListAdapter;
   private int currPage = 1;
   private CustomProgressDialog progressDialog = null;
   private String data_url = UrlHelper.ROOM_ATTEN;

   private void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(avActivity);
         progressDialog.setMessage("加载中");
      }

      //progressDialog.show();
   }

   private void stopProgressDialog() {
      if (progressDialog != null) {
         progressDialog.dismiss();
         progressDialog = null;
      }
   }

   @Override public void onRefresh(final int mode) {
      startProgressDialog();
      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(UrlHelper.getRedbaglist
          + "?page="
          + currPage
          + "&packetid="
          + packet_id_global
          + "&liveuid="
          + AULiveApplication.currLiveUid);

      Trace.d(sb.toString());
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UserInfoDialogListEntity>() {

         @Override public void onCallback(UserInfoDialogListEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               currPage--;
               dialog_list.setVisibility(View.VISIBLE);
               dialog_list.onRefreshComplete(mode, false);
               dialog_list.enableFooter(false);
               return;
            }

            if (callback.getStat() == 200) {

               if (mode == PullToRefreshView.HEADER) {
                  entities.clear();
               }

               if (callback.getList() != null) {
                  if (data_url.equals(UrlHelper.ROOM_IN_GOLD_TOPS)) {
                     ArrayList<UserInfoDialogEntity> tem_List = callback.getList();
                     for (UserInfoDialogEntity entity : tem_List) {
                        entity.setIs_ranking(true);
                        entities.add(entity);
                     }
                  } else {
                     //按大小排序一下
                     ArrayList<UserInfoDialogEntity> tem_List = callback.getList();
                     Collections.sort(tem_List, new SortComparator());
                     entities.addAll(tem_List);
                  }
               }

               dialgoListAdapter.setEntities(entities);
               dialgoListAdapter.notifyDataSetChanged();
               dialog_list.onRefreshComplete(mode, true);

               if (mode == PullToRefreshView.HEADER || (callback.getList() != null
                   && callback.getList().size() > 0)) {
                  dialog_list.enableFooter(true);
               } else {
                  dialog_list.enableFooter(false);
               }
            } else {
               if (callback.getStat() == 500) {
                  //没登录
               }
               stopProgressDialog();
               currPage--;
               // 因为可能网络恢复，success改为true
               dialog_list.onRefreshComplete(mode, false);
               dialog_list.enableFooter(false);
            }
         }

         @Override public void onFailure(AppException e) {
            stopProgressDialog();
            currPage--;
            entities.clear();
            // 因为可能网络恢复，success改为true
            dialog_list.onRefreshComplete(mode, false);
            dialog_list.enableFooter(false);
         }
      }.setReturnType(UserInfoDialogListEntity.class));

      request.execute();
   }

   public class SortComparator implements Comparator {
      @Override public int compare(Object lhs, Object rhs) {
         UserInfoDialogEntity a = (UserInfoDialogEntity) lhs;
         UserInfoDialogEntity b = (UserInfoDialogEntity) rhs;

         return (b.getGrab() - a.getGrab());
      }
   }

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      UserInfoDialogEntity entity = (UserInfoDialogEntity) parent.getAdapter().getItem(position);

      MemberInfo memberInfo =
          new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
      popupWindow2.dismiss();

      PopupWindowUtil popupWindow1 = new PopupWindowUtil(anchor);
      popupWindow1.setContentView(R.layout.dialog_myroom_userinfo);
      popupWindow1.setOutsideTouchable(true);
      UserInfoHelper userInfoHelper =
          UserInfoHelper.getInstance(anchor, popupWindow1, avActivity, memberInfo);
   }
}
