package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfoList;

import java.util.ArrayList;

/**
 * 发起1v1聊天的帮助类
 * Created by r on 16/10/21.
 */
public class SoloHelper {

   private final Activity activity;

   public SoloHelper(Activity activity) {
      this.activity = activity;
   }

   private boolean checkCanCallSolo() {
      return true;
   }

   public void callSolo() {
      if (checkCanCallSolo()) {
         Trace.d("**>>呼叫");

         try {
            String targetId = null;
            if (AULiveApplication.mAvActivity != null) {
               targetId = AULiveApplication.mAvActivity.privateChatHelper.mTargetId;
            } else {
               if (AULiveApplication.mPrivateChatActivity.uid != null
                   && !AULiveApplication.mPrivateChatActivity.uid.equals("")) {
                  //手动启动
                  targetId = AULiveApplication.mPrivateChatActivity.uid;
               } else {
                  //push启动会
                  targetId = AULiveApplication.mPrivateChatActivity.mTargetId;
               }
            }

            getUserinfo(targetId);
         } catch (Exception e) {

         }
      }
   }

   private void getUserinfo(final String uid) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.user_home_Url + "/userinfo" + "?uid=" + uid + "&pf=android");
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<UserInfoList>() {

         @Override public void onCallback(UserInfoList callback) {
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {

               ArrayList<LoginUserEntity> userinfo = callback.getUserinfo();

               io.rong.imlib.model.UserInfo tem_userInfo =
                   new io.rong.imlib.model.UserInfo(userinfo.get(0).getUid(),
                       userinfo.get(0).getNickname(), Uri.parse(userinfo.get(0).getFace()));
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(UserInfoList.class));
      request.execute();
   }
}
