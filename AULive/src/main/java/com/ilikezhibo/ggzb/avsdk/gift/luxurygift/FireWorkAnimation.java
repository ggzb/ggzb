package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;
import java.io.InputStream;

public class FireWorkAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView fire_work_start = null;
   ImageView firework1 = null;
   ImageView firework2 = null;
   ImageView firework3 = null;
   ImageView firework4 = null;

   SendGiftEntity sendGiftEntity;

   View root_view;

   public FireWorkAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;
      root_view = rootView;

      fire_work_start = (ImageView) rootView.findViewById(R.id.start_firework);
      firework1 = (ImageView) rootView.findViewById(R.id.firework1);
      firework2 = (ImageView) rootView.findViewById(R.id.firework2);
      firework3 = (ImageView) rootView.findViewById(R.id.firework3);
      firework4 = (ImageView) rootView.findViewById(R.id.firework4);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startFireWorkAnimation() {
      Trace.d("startFireWorkAnimation");

      fire_work_start.setVisibility(View.VISIBLE);
      showImageDelay();
   }

   int firework_index=0;
   private void showImageDelay() {

      firework_index++;
      Trace.d("firework_index:" + firework_index);
      fire_work_start.postDelayed(new Runnable() {
         @Override public void run() {

            if (firework_index > 10) {
               //开始大动画
               fire_work_start.setVisibility(View.GONE);
               doFireWorkShow();
               return;
            }
            final String imgname = "fireworks_" + firework_index;

            int imgid =
                context.getResources().getIdentifier(imgname, "drawable", context.getPackageName());
            Bitmap bitmap = readBitMap(context, imgid);
            fire_work_start.setImageBitmap(bitmap);

            //加载完第一张之后才显示
            if (firework_index == 1) {
               root_view.setVisibility(View.VISIBLE);
            }

            //下一个图片
            showImageDelay();
         }
      },140);
   }

   /**
    * 以最省内存的方式读取本地资源的图片
    */
   public static Bitmap readBitMap(Context context, int resId) {
      BitmapFactory.Options opt = new BitmapFactory.Options();
      opt.inPreferredConfig = Bitmap.Config.RGB_565;
      opt.inPurgeable = true;
      opt.inInputShareable = true;
// 获取资源图片
      InputStream is = context.getResources().openRawResource(resId);
      return BitmapFactory.decodeStream(is, null, opt);
   }

   public void doFireWorkShow() {
      firework1.postDelayed(new Runnable() {
         @Override public void run() {
            firework1.setVisibility(View.VISIBLE);
            ViewAnimator.animate(firework1)
                .scale(0, (float) 1.0)
                .duration(900)
                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      firework1.setVisibility(View.GONE);
                   }
                }).start();
         }
      }, 200);

      firework2.postDelayed(new Runnable() {
         @Override public void run() {
            firework2.setVisibility(View.VISIBLE);
            ViewAnimator.animate(firework2)
                .scale(0, (float) 0.8)
                .duration(1000)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      firework2.setVisibility(View.GONE);
                   }
                }).start();
         }
      }, 500);

      firework3.postDelayed(new Runnable() {
         @Override public void run() {
            firework3.setVisibility(View.VISIBLE);
            ViewAnimator.animate(firework3)
                .scale(0, (float) 0.6)
                .duration(1200)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      firework3.setVisibility(View.GONE);

                      //总体隐藏
                      root_view.setVisibility(View.GONE);
                      //显示更多豪华礼物
                      LuxuryGiftUtil.is_showing_luxury_gift = false;

                      if (context instanceof AvActivity) {
                         AvActivity temp_activity = (AvActivity) context;
                         //显示礼物动画
                         temp_activity.hasAnyLuxuryGift();
                      }

                   }
                }).start();
         }
      }, 600);

      firework4.postDelayed(new Runnable() {
         @Override public void run() {
            firework4.setVisibility(View.VISIBLE);
            ViewAnimator.animate(firework4)
                .scale(0, (float) 0.7)
                .duration(1300)
                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      firework4.setVisibility(View.GONE);
                   }
                }).start();
         }
      }, 250);
   }
}
