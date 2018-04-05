package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.widget.provider.InputProvider;

/**
 * Created by big on 5/10/16.
 */
public class GiftProvider extends InputProvider.ExtendProvider {
   public GiftProvider(RongContext context) {
      super(context);
   }

   /**
    * 设置展示的图标
    */
   @Override public Drawable obtainPluginDrawable(Context context) {
      //R.drawable.de_contacts 通讯录图标
      return context.getResources().getDrawable(R.drawable.rc_ic_gift);
   }

   /**
    * 设置图标下的title
    */
   @Override public CharSequence obtainPluginTitle(Context context) {
      //R.string.add_contacts 通讯录
      return "礼物";
   }

   /**
    * click 事件
    */
   @Override public void onPluginClick(View view) {
      if (AULiveApplication.mAvActivity != null) {

         AULiveApplication.mAvActivity.privateChatHelper.showPrivateChatGiftPager();
      } else {
         AULiveApplication.mPrivateChatActivity.showPrivateChatGiftPager();
      }
   }

   @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode != Activity.RESULT_OK) {
         return;
      }
      super.onActivityResult(requestCode, resultCode, data);
   }
}