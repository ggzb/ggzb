package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class CrownAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView crown_iv = null;
   ImageView crown_bg_light = null;
   ImageView crown_light0 = null;
   ImageView crown_light1 = null;
   ImageView crown_light2 = null;
   ImageView crown_light3 = null;

   ImageView left_wing = null;
   ImageView right_wing = null;

   RelativeLayout crown_ly = null;

   SendGiftEntity sendGiftEntity;

   public CrownAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;
      crown_bg_light = (ImageView) rootView.findViewById(R.id.crown_bg_light);

      crown_iv = (ImageView) rootView.findViewById(R.id.crown_iv);
      crown_light0 = (ImageView) rootView.findViewById(R.id.crown_light0);
      crown_light1 = (ImageView) rootView.findViewById(R.id.crown_light1);
      crown_light2 = (ImageView) rootView.findViewById(R.id.crown_light2);
      crown_light3 = (ImageView) rootView.findViewById(R.id.crown_light3);

      left_wing = (ImageView) rootView.findViewById(R.id.left_wing);
      right_wing = (ImageView) rootView.findViewById(R.id.right_wing);

      crown_ly = (RelativeLayout) rootView.findViewById(R.id.crown_ly);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startAnimation() {
      crown_ly.setVisibility(View.VISIBLE);
      crown_iv.setVisibility(View.VISIBLE);

      //移动皇冠
      final Animation carAnimation = AnimationUtils.loadAnimation(context, R.anim.crown_down_anim);
      carAnimation.getFillAfter();
      carAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {

            objectAnimator1 = ObjectAnimator.ofFloat(left_wing, "alpha", 0.1f, 1.0f,0.1f);
            objectAnimator1.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator1.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator1.setDuration(2000);
            objectAnimator1.setStartDelay(200);
            objectAnimator1.start();
            objectAnimator1.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  left_wing.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator2 = ObjectAnimator.ofFloat(right_wing, "alpha", 0.1f, 1.0f,0.1f);
            objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator2.setDuration(2000);
            objectAnimator2.setStartDelay(300);
            objectAnimator2.start();
            objectAnimator2.addListener(new Animator.AnimatorListener() {
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

            objectAnimator3 = ObjectAnimator.ofFloat(crown_bg_light, "scale", 0.5f, 1.3f, 0.5f);
            objectAnimator3.setDuration(2000);
            objectAnimator3.setStartDelay(100);
            objectAnimator3.start();
            objectAnimator3.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  crown_bg_light.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //星星
            objectAnimator4 = ObjectAnimator.ofFloat(crown_light0, "alpha", 0f, 1f, 0f);
            objectAnimator4.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator4.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator4.setDuration(2000);
            objectAnimator4.setStartDelay(400);
            objectAnimator4.start();
            objectAnimator4.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  crown_light0.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //天使
            objectAnimator5 = ObjectAnimator.ofFloat(crown_light1, "alpha", 0f, 1f, 0f);
            objectAnimator5.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator5.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator5.setDuration(3000);
            objectAnimator5.setStartDelay(600);
            objectAnimator5.start();
            objectAnimator5.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  crown_light1.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator6 = ObjectAnimator.ofFloat(crown_light2, "alpha", 0f, 1f, 0f);
            objectAnimator6.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator6.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator6.setDuration(3000);
            objectAnimator6.setStartDelay(1000);
            objectAnimator6.start();
            objectAnimator6.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  crown_light2.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator7 = ObjectAnimator.ofFloat(crown_light3, "alpha", 0f, 1f, 0f);
            objectAnimator7.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator7.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator7.setDuration(3000);
            objectAnimator7.setStartDelay(1200);
            objectAnimator7.start();
            objectAnimator7.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  crown_light3.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(crown_ly, "alpha", 1f, 0f);
            objectAnimator8.setDuration(2000);
            objectAnimator8.setStartDelay(5000);
            objectAnimator8.start();
            objectAnimator8.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {

               }

               @Override public void onAnimationEnd(Animator animation) {

                  objectAnimator1.cancel();
                  objectAnimator2.cancel();
                  objectAnimator3.cancel();
                  objectAnimator4.cancel();
                  objectAnimator5.cancel();
                  objectAnimator6.cancel();
                  objectAnimator7.cancel();

                  crown_iv.setVisibility(View.GONE);

                  crown_bg_light.setVisibility(View.GONE);
                  left_wing.setVisibility(View.GONE);
                  right_wing.setVisibility(View.GONE);

                  crown_light0.setVisibility(View.GONE);
                  crown_light1.setVisibility(View.GONE);
                  crown_light2.setVisibility(View.GONE);
                  crown_light3.setVisibility(View.GONE);

                  ObjectAnimator objectAnimator9 =
                      ObjectAnimator.ofFloat(crown_ly, "alpha", 0f, 1f);
                  objectAnimator9.setDuration(0);
                  objectAnimator9.setStartDelay(0);
                  objectAnimator9.start();

                  crown_ly.setVisibility(View.GONE);
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

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });

      crown_ly.startAnimation(carAnimation);
   }

   ObjectAnimator objectAnimator1;
   ObjectAnimator objectAnimator2;
   ObjectAnimator objectAnimator3;
   ObjectAnimator objectAnimator4;
   ObjectAnimator objectAnimator5;
   ObjectAnimator objectAnimator6;
   ObjectAnimator objectAnimator7;
}
