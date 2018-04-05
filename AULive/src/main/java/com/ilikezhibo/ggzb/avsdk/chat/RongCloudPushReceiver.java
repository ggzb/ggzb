package com.ilikezhibo.ggzb.avsdk.chat;

import android.content.Context;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by big on 8/17/16.
 */
public class RongCloudPushReceiver extends PushMessageReceiver {
   @Override
   public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
      return false;
   }

   @Override
   public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
      return false;
   }
}