package com.ilikezhibo.ggzb.avsdk.userinfo;

import android.view.View;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.PixelDpHelper;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.entity.UidListEntity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

import de.greenrobot.event.EventBus;
import popwindow.PopupWindowUtil;

/**
 * Created by big on 4/5/16.
 */
public class RoomManagerHelper {

   private PopupWindowUtil popupWindow;
   private AvActivity context;
   private LoginUserEntity userinfo;
   private static RoomManagerHelper roomManagerHelper;
   //false即主播，true则是普通管理员
   private boolean is_manager;

   private TextView txt_setmanage;
   private TextView txt_removemanage;
   private TextView txt_managelist;
   private TextView txt_reportorfbchat;
   private TextView txt_remove_gag;
   private TextView txt_cancel;
   private TextView txt_report;

   private RoomManagerHelper(PopupWindowUtil popupWindow, AvActivity context,
       LoginUserEntity userinfo, boolean is_manager) {
      this.popupWindow = popupWindow;
      this.context = context;
      this.userinfo = userinfo;
      this.is_manager = is_manager;
      initViews();
   }

   public static RoomManagerHelper getInstance(PopupWindowUtil popupWindow, AvActivity context,
       LoginUserEntity userinfo, boolean is_manager) {
      if (roomManagerHelper == null) {
         roomManagerHelper = new RoomManagerHelper(popupWindow, context, userinfo, is_manager);
      }
      roomManagerHelper.popupWindow = popupWindow;
      roomManagerHelper.context = context;
      roomManagerHelper.userinfo = userinfo;
      roomManagerHelper.is_manager = is_manager;
      roomManagerHelper.initViews();

      return roomManagerHelper;
   }

   private void initViews() {

      txt_setmanage = (TextView) popupWindow.findId(R.id.txt_setmanage);
      txt_setmanage.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            setManager(userinfo.getUid());
         }
      });
      txt_removemanage = (TextView) popupWindow.findId(R.id.txt_removemanage);
      txt_removemanage.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            removeManager(userinfo.getUid());
         }
      });

      txt_managelist = (TextView) popupWindow.findId(R.id.txt_managelist);
      txt_managelist.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            showManagersList();
         }
      });

      //禁言
      txt_reportorfbchat = (TextView) popupWindow.findId(R.id.txt_reportorfbchat);
      txt_reportorfbchat.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            setGag(userinfo.getUid());
         }
      });

      //删除禁言
      txt_remove_gag = (TextView) popupWindow.findId(R.id.txt_remove_gag);
      txt_remove_gag.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            setRemoveGag(userinfo.getUid());
         }
      });

      txt_cancel = (TextView) popupWindow.findId(R.id.txt_cancel);
      txt_cancel.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            popupWindow.dismiss();
         }
      });

      txt_report = (TextView) popupWindow.findId(R.id.txt_report);
      txt_report.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            showPromptDialog(userinfo.getUid());
         }
      });

      //设置是显示添加还是删除管理
      if (context.is_manager) {
         txt_setmanage.setVisibility(View.GONE);
         txt_removemanage.setVisibility(View.VISIBLE);
      }

      //打开dialog的当是管理员，而不是房间创建者时
      if (is_manager) {
         txt_setmanage.setVisibility(View.GONE);
         txt_removemanage.setVisibility(View.GONE);
         txt_managelist.setVisibility(View.GONE);

         txt_report.setVisibility(View.VISIBLE);
      }

      //如果是自己，不能禁言
      if (userinfo.getUid().equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
         txt_reportorfbchat.setVisibility(View.GONE);
      } else {
         txt_reportorfbchat.setVisibility(View.VISIBLE);
      }

      //判断是禁言还是删除禁言
      if (context.gag_list.contains(userinfo.getUid())) {
         txt_reportorfbchat.setVisibility(View.GONE);
         txt_remove_gag.setVisibility(View.VISIBLE);
      } else {
         txt_reportorfbchat.setVisibility(View.VISIBLE);
         txt_remove_gag.setVisibility(View.GONE);
      }

      //是超管
      //重置信息
      TextView txt_reset_data = (TextView) popupWindow.findId(R.id.txt_reset_data);
      txt_reset_data.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            context.showSuperMangerDialog(userinfo.getUid(),"重置信息", 9);
         }
      });

      TextView txt_forbid_login = (TextView) popupWindow.findId(R.id.txt_forbid_login);
      txt_forbid_login.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
//            context.showSuperMangerDialog("封设备", 2);
         }
      });
      if (context.is_super_manager) {
         txt_reset_data.setVisibility(View.VISIBLE);
         //txt_forbid_login.setVisibility(View.VISIBLE);
      } else {
         txt_reset_data.setVisibility(View.GONE);
         txt_forbid_login.setVisibility(View.GONE);
      }
   }

   public void setManager(String uid1) {

      RequestInformation request = new RequestInformation(UrlHelper.SET_MANAGER_ROOM
          + "?liveuid="
          + AULiveApplication.currLiveUid
          + "&user="
          + uid1
          + "&type=1", RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UidListEntity>() {

         @Override public void onCallback(UidListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               //
               Utils.showMessage("设置成功");
               //context.is_manager=true;

               txt_setmanage.setVisibility(View.GONE);
               txt_removemanage.setVisibility(View.VISIBLE);

               context.sendManagerAndGagMsg(userinfo, "manager");
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

   public void removeManager(String uid1) {

      RequestInformation request = new RequestInformation(UrlHelper.SET_MANAGER_ROOM
          + "?liveuid="
          + AULiveApplication.currLiveUid
          + "&user="
          + uid1
          + "&type=0", RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UidListEntity>() {

         @Override public void onCallback(UidListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               //context.is_manager=false;
               Utils.showMessage("设置成功");
               txt_setmanage.setVisibility(View.VISIBLE);
               txt_removemanage.setVisibility(View.GONE);

               context.sendManagerAndGagMsg(userinfo, "remove_manager");
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

   public void setGag(String uid1) {
      RequestInformation request = new RequestInformation(
          UrlHelper.SET_GAG_ROOM + "?liveuid=" + AULiveApplication.currLiveUid + "&user=" + uid1,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UidListEntity>() {

         @Override public void onCallback(UidListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               Utils.showMessage("禁言成功");
               //发送一个socket禁言消息，做禁言处理
               context.sendManagerAndGagMsg(userinfo, "gag");
               //判断是禁言还是删除禁言
               if (context.gag_list.contains(userinfo.getUid())) {
                  txt_reportorfbchat.setVisibility(View.GONE);
                  txt_remove_gag.setVisibility(View.VISIBLE);
               } else {
                  txt_reportorfbchat.setVisibility(View.VISIBLE);
                  txt_remove_gag.setVisibility(View.GONE);
               }
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

   public void setRemoveGag(String uid1) {
      RequestInformation request = new RequestInformation(UrlHelper.SET_GAG_ROOM
          + "?liveuid="
          + AULiveApplication.currLiveUid
          + "&user="
          + uid1
          + "&type=1", RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UidListEntity>() {

         @Override public void onCallback(UidListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               Utils.showMessage("解除禁言");
               //发送一个socket禁言消息，做禁言处理
               context.sendManagerAndGagMsg(userinfo, "remove_gag");
               //判断是禁言还是删除禁言
               if (context.gag_list.contains(userinfo.getUid())) {
                  txt_reportorfbchat.setVisibility(View.GONE);
                  txt_remove_gag.setVisibility(View.VISIBLE);
               } else {
                  txt_reportorfbchat.setVisibility(View.VISIBLE);
                  txt_remove_gag.setVisibility(View.GONE);
               }
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

   public void showManagersList() {
      popupWindow.dismiss();
      EventBus.getDefault().post(new CloseAllPopUpDialogEvent());
      //Intent gag_list = new Intent(context, ManagerListActivity.class);
      //context.startActivity(gag_list);
      if (context.managerListFragment == null) {
         context.managerListFragment = new ManagerListFragment();
      }
      context.getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.mManagerList, context.managerListFragment)
          .commitAllowingStateLoss();
      View fragment_container = context.findViewById(R.id.mManagerList);
      fragment_container.setVisibility(View.VISIBLE);
      ViewAnimator.animate(fragment_container)
          .translationX(PixelDpHelper.dip2px(context, 500), 0)
          .duration(300)

          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {

             }
          }).start();
   }

   //提示消息Dialog
   private void showPromptDialog(final String uid) {
      final CustomDialog customDialog = new CustomDialog(context, new CustomDialogListener() {

         @Override public void onDialogClosed(int closeType) {
            switch (closeType) {
               case CustomDialogListener.BUTTON_POSITIVE:
                  doReport(uid);
            }
         }
      });

      customDialog.setCustomMessage("确认举报?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   private void doReport(final String uid) {

      RequestInformation request = new RequestInformation(
          UrlHelper.SERVER_URL + "live/report" + "?liveuid=" + uid + "&type=0",
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               Utils.showCroutonText(context, "成功举报");
            } else {
               Utils.showCroutonText(context, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }
}
