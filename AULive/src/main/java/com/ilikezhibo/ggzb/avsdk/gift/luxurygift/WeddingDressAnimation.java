package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.Trace;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import snowdrop.HeartLayout;

public class WeddingDressAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView shoes_iv = null;

   ImageView shine_star_on_shoes_iv = null;
   ImageView shoes_star1 = null;
   ImageView shoes_star2 = null;
   ImageView shoes_star3 = null;

   ImageView angle1 = null;
   ImageView button_light = null;
   ImageView top_light = null;

   RelativeLayout wedding_dress_ly = null;

   SendGiftEntity sendGiftEntity;

   public WeddingDressAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;

      shoes_iv = (ImageView) rootView.findViewById(R.id.crown_iv);
      shine_star_on_shoes_iv = (ImageView) rootView.findViewById(R.id.shine_star_on_shoes_iv);
      shoes_star1 = (ImageView) rootView.findViewById(R.id.shoes_star1);
      shoes_star3 = (ImageView) rootView.findViewById(R.id.shoes_star3);
      shoes_star2 = (ImageView) rootView.findViewById(R.id.shoes_star2);

      wedding_dress_ly = (RelativeLayout) rootView.findViewById(R.id.wedding_dress_ly);

      angle1 = (ImageView) rootView.findViewById(R.id.shoes_angle1);
      button_light = (ImageView) rootView.findViewById(R.id.shoes_angle2);
      top_light = (ImageView) rootView.findViewById(R.id.shoes_angle3);
      mHeartLayout = (HeartLayout) rootView.findViewById(R.id.heart_layout);
      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startAnimation() {
      wedding_dress_ly.setVisibility(View.VISIBLE);

      shoes_iv.setVisibility(View.VISIBLE);
      //显示婚纱
      ViewAnimator.animate(shoes_iv)
          .alpha(0f, 0f)
          .duration(300)

          .onStart(new AnimationListener.Start() {
             @Override public void onStart() {
             }
          })
          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {
                //星星
                objectAnimator =
                    ObjectAnimator.ofFloat(shine_star_on_shoes_iv, "alpha", 0f, 1f, 0f);
                objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator.setDuration(2000);
                objectAnimator.setStartDelay(1000);
                objectAnimator.start();
                objectAnimator.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      Trace.d("objectAnimator");
                      shine_star_on_shoes_iv.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                objectAnimator2 = ObjectAnimator.ofFloat(shoes_star1, "alpha", 0f, 1f, 0f);
                objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator2.setDuration(2000);
                objectAnimator2.setStartDelay(1000);
                objectAnimator2.start();
                objectAnimator2.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      shoes_star1.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                objectAnimator3 = ObjectAnimator.ofFloat(shoes_star2, "alpha", 0f, 1f, 0f);
                objectAnimator3.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator3.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator3.setDuration(2000);
                objectAnimator3.setStartDelay(1500);
                objectAnimator3.start();
                objectAnimator3.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      shoes_star2.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                objectAnimator4 = ObjectAnimator.ofFloat(shoes_star3, "alpha", 0f, 1f, 0f);
                objectAnimator4.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator4.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator4.setDuration(2000);
                objectAnimator4.setStartDelay(1400);
                objectAnimator4.start();
                objectAnimator4.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      shoes_star3.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                //天使
                objectAnimator5 = ObjectAnimator.ofFloat(angle1, "alpha", 0f, 1f, 0f);
                objectAnimator5.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator5.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator5.setDuration(3000);
                objectAnimator5.setStartDelay(1600);
                objectAnimator5.start();
                objectAnimator5.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      angle1.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                objectAnimator6 = ObjectAnimator.ofFloat(button_light, "alpha", 0f, 1f);
                objectAnimator6.setDuration(3000);
                objectAnimator6.setStartDelay(1000);
                objectAnimator6.start();
                objectAnimator6.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      button_light.setVisibility(View.VISIBLE);
                      startHeartShow();
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                objectAnimator7 = ObjectAnimator.ofFloat(top_light, "alpha", 0f, 1f);
                objectAnimator7.setDuration(3000);
                objectAnimator7.setStartDelay(500);
                objectAnimator7.start();
                objectAnimator7.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      top_light.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                objectAnimator0 = ObjectAnimator.ofFloat(shoes_iv, "alpha", 0f, 1f);
                objectAnimator0.setDuration(3000);
                objectAnimator0.setStartDelay(1200);
                objectAnimator0.start();
                objectAnimator0.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {

                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                ObjectAnimator objectAnimator8 =
                    ObjectAnimator.ofFloat(wedding_dress_ly, "alpha", 1f, 0f);
                objectAnimator8.setDuration(3000);
                objectAnimator8.setStartDelay(9000);
                objectAnimator8.start();
                objectAnimator8.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {

                   }

                   @Override public void onAnimationEnd(Animator animation) {

                      objectAnimator.cancel();
                      objectAnimator2.cancel();
                      objectAnimator3.cancel();
                      objectAnimator4.cancel();
                      objectAnimator5.cancel();
                      objectAnimator6.cancel();
                      objectAnimator7.cancel();

                      shine_star_on_shoes_iv.setVisibility(View.GONE);

                      shoes_star1.setVisibility(View.GONE);
                      shoes_star2.setVisibility(View.GONE);
                      shoes_star3.setVisibility(View.GONE);

                      angle1.setVisibility(View.GONE);
                      button_light.setVisibility(View.GONE);
                      top_light.setVisibility(View.GONE);

                      ObjectAnimator objectAnimator9 =
                          ObjectAnimator.ofFloat(wedding_dress_ly, "alpha", 0f, 1f);
                      objectAnimator9.setDuration(0);
                      objectAnimator9.setStartDelay(0);
                      objectAnimator9.start();

                      closeHeartShow();

                      wedding_dress_ly.setVisibility(View.GONE);
                      LuxuryGiftUtil.is_showing_luxury_gift = false;
                      if (context instanceof AvActivity) {
                         AvActivity temp_activity = (AvActivity) context;
                         //显示礼物动画
                         temp_activity.hasAnyLuxuryGift();
                      }
                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });
             }
          }).start();
   }
   ObjectAnimator objectAnimator0;
   ObjectAnimator objectAnimator;
   ObjectAnimator objectAnimator2;
   ObjectAnimator objectAnimator3;
   ObjectAnimator objectAnimator4;
   ObjectAnimator objectAnimator5;
   ObjectAnimator objectAnimator6;
   ObjectAnimator objectAnimator7;

   private void startHeartShow() {

      mHeartTimer = new Timer();
      mHeartTimer.scheduleAtFixedRate(new TimerTask() {
         @Override public void run() {
            if (mHeartLayout != null) {
               mHeartLayout.post(new Runnable() {
                  @Override public void run() {
                     if (mHeartLayout != null) {
                        //mHeartLayout.addHeart(randomColor());
                        //新的图片方法
                        mHeartLayout.addHeart2(HeartLayout.pics_other[new Random().nextInt(6)]);
                     }
                  }
               });
            }
         }
      }, 500, 1500);
   }

   private void closeHeartShow() {
      if (mHeartTimer != null && mHeartLayout != null) {
         mHeartTimer.cancel();
         mHeartTimer = null;
         mHeartLayout.clearAnimation();
         mHeartLayout = null;
      }
   }

   private Timer mHeartTimer = new Timer();
   private HeartLayout mHeartLayout;
}
