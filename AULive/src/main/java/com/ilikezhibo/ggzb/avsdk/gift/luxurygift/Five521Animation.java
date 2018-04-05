package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class Five521Animation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView shoes_iv = null;
   ImageView right_wing = null;

   RelativeLayout glass_shoes_ly = null;

   SendGiftEntity sendGiftEntity;

   public Five521Animation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;

      shoes_iv = (ImageView) rootView.findViewById(R.id.crown_iv);

      right_wing = (ImageView) rootView.findViewById(R.id.right_wing);

      glass_shoes_ly = (RelativeLayout) rootView.findViewById(R.id.f521_ly);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startAnimation() {
      glass_shoes_ly.setVisibility(View.VISIBLE);

      shoes_iv.setVisibility(View.VISIBLE);
      //显示鞋子
      ViewAnimator.animate(shoes_iv)
          .alpha(0.5f, 1f).scale(0f,1f)
          .duration(1500)
          .onStart(new AnimationListener.Start() {
             @Override public void onStart() {
             }
          })
          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {

                objectAnimator7 = ObjectAnimator.ofFloat(right_wing, "alpha", 0f, 1f, 0f);
                objectAnimator7.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator7.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator7.setDuration(2000);
                objectAnimator7.setStartDelay(1000);
                objectAnimator7.start();
                objectAnimator7.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {
                      right_wing.setVisibility(View.VISIBLE);
                   }

                   @Override public void onAnimationEnd(Animator animation) {

                   }

                   @Override public void onAnimationCancel(Animator animation) {

                   }

                   @Override public void onAnimationRepeat(Animator animation) {

                   }
                });

                ObjectAnimator objectAnimator8 =
                    ObjectAnimator.ofFloat(glass_shoes_ly, "alpha", 1f, 0f);
                objectAnimator8.setDuration(2000);
                objectAnimator8.setStartDelay(4000);
                objectAnimator8.start();
                objectAnimator8.addListener(new Animator.AnimatorListener() {
                   @Override public void onAnimationStart(Animator animation) {

                   }

                   @Override public void onAnimationEnd(Animator animation) {


                      objectAnimator7.cancel();

                      right_wing.setVisibility(View.GONE);


                      ObjectAnimator objectAnimator9 =
                          ObjectAnimator.ofFloat(glass_shoes_ly, "alpha", 0f, 1f);
                      objectAnimator9.setDuration(0);
                      objectAnimator9.setStartDelay(0);
                      objectAnimator9.start();

                      glass_shoes_ly.setVisibility(View.GONE);
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
          }).start()
      ;
   }

   ObjectAnimator objectAnimator;
   ObjectAnimator objectAnimator2;
   ObjectAnimator objectAnimator3;
   ObjectAnimator objectAnimator4;
   ObjectAnimator objectAnimator5;
   ObjectAnimator objectAnimator6;
   ObjectAnimator objectAnimator7;
}
