package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class MoonAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView moon_iv = null;
   ImageView moon_black_ground = null;
   ImageView moon_cloud2_iv = null;
   ImageView moon_cloud1_iv = null;
   ImageView moon_tree_iv = null;
   ImageView moon_street_lamp_iv = null;
   ImageView moon_lotus_iv = null;

   ImageView moon_lotus_light_iv = null;
   ImageView moon_chair_iv = null;

   RelativeLayout moon_ly = null;

   SendGiftEntity sendGiftEntity;

   public MoonAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;

      moon_iv = (ImageView) rootView.findViewById(R.id.moon_iv);
      moon_black_ground = (ImageView) rootView.findViewById(R.id.moon_black_ground);
      moon_cloud2_iv = (ImageView) rootView.findViewById(R.id.moon_cloud2_iv);
      moon_cloud1_iv = (ImageView) rootView.findViewById(R.id.moon_cloud1_iv);
      moon_tree_iv = (ImageView) rootView.findViewById(R.id.moon_tree_iv);

      moon_street_lamp_iv = (ImageView) rootView.findViewById(R.id.moon_street_lamp_iv);
      moon_lotus_iv = (ImageView) rootView.findViewById(R.id.moon_lotus_iv);

      moon_lotus_light_iv = (ImageView) rootView.findViewById(R.id.moon_lotus_light_iv);
      moon_chair_iv = (ImageView) rootView.findViewById(R.id.moon_chair_iv);

      moon_ly = (RelativeLayout) rootView.findViewById(R.id.moon_ly);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startAnimation() {
      moon_ly.setVisibility(View.VISIBLE);

      objectAnimator1 = ObjectAnimator.ofFloat(moon_black_ground, "alpha", 0.1f, 1.0f);
      objectAnimator1.setDuration(1000);
      objectAnimator1.setStartDelay(200);
      objectAnimator1.start();
      objectAnimator1.addListener(new Animator.AnimatorListener() {
         @Override public void onAnimationStart(Animator animation) {
            moon_black_ground.setVisibility(View.VISIBLE);
         }

         @Override public void onAnimationEnd(Animator animation) {

            objectAnimator2 =
                ObjectAnimator.ofFloat(moon_lotus_light_iv, "alpha", 0.1f, 1.0f, 0.1f);
            objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator2.setDuration(1000);
            objectAnimator2.setStartDelay(2500);
            objectAnimator2.start();
            objectAnimator2.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_lotus_light_iv.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator3 = ObjectAnimator.ofFloat(moon_iv, "alpha", 0f, 1.0f);
            objectAnimator3.setDuration(2000);
            objectAnimator3.setStartDelay(100);
            objectAnimator3.start();
            objectAnimator3.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_iv.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator4 = ObjectAnimator.ofFloat(moon_cloud1_iv, "alpha", 0f, 1f);
            objectAnimator4.setDuration(1500);
            objectAnimator4.setStartDelay(400);
            objectAnimator4.start();
            objectAnimator4.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_cloud1_iv.setVisibility(View.VISIBLE);
                  ObjectAnimator objectAnimator =
                      ObjectAnimator.ofFloat(moon_cloud1_iv, "translationX", -400f, 490f);
                  objectAnimator.setDuration(7000);
                  objectAnimator.setStartDelay(600);
                  objectAnimator.start();
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator5 = ObjectAnimator.ofFloat(moon_cloud2_iv, "alpha", 0f, 1f);
            objectAnimator5.setDuration(2000);
            objectAnimator5.setStartDelay(600);
            objectAnimator5.start();
            objectAnimator5.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_cloud2_iv.setVisibility(View.VISIBLE);
                  ObjectAnimator objectAnimator =
                      ObjectAnimator.ofFloat(moon_cloud2_iv, "translationX", -400f, 460f);
                  objectAnimator.setDuration(7000);
                  objectAnimator.setStartDelay(200);
                  objectAnimator.start();
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator6 = ObjectAnimator.ofFloat(moon_tree_iv, "alpha", 0f, 1f);
            objectAnimator6.setDuration(1000);
            objectAnimator6.setStartDelay(300);
            objectAnimator6.start();
            objectAnimator6.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_tree_iv.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            objectAnimator7 = ObjectAnimator.ofFloat(moon_chair_iv, "alpha", 0f, 1f);
            objectAnimator7.setDuration(1000);
            objectAnimator7.setStartDelay(400);
            objectAnimator7.start();
            objectAnimator7.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_chair_iv.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(moon_lotus_iv, "alpha", 0f, 1f);
            objectAnimator8.setDuration(1000);
            objectAnimator8.setStartDelay(400);
            objectAnimator8.start();
            objectAnimator8.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_lotus_iv.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            ObjectAnimator objectAnimator9 = ObjectAnimator.ofFloat(moon_ly, "alpha", 1f, 0f);
            objectAnimator9.setDuration(2000);
            objectAnimator9.setStartDelay(4000);
            objectAnimator9.start();
            objectAnimator9.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  moon_lotus_iv.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

                  objectAnimator1.cancel();
                  objectAnimator2.cancel();
                  objectAnimator3.cancel();
                  objectAnimator4.cancel();
                  objectAnimator5.cancel();
                  objectAnimator6.cancel();
                  objectAnimator7.cancel();

                  moon_iv.setVisibility(View.INVISIBLE);

                  moon_chair_iv.setVisibility(View.GONE);
                  moon_street_lamp_iv.setVisibility(View.GONE);
                  moon_lotus_iv.setVisibility(View.GONE);
                  moon_lotus_light_iv.setVisibility(View.INVISIBLE);
                  moon_black_ground.setVisibility(View.GONE);
                  moon_cloud2_iv.setVisibility(View.GONE);
                  moon_cloud1_iv.setVisibility(View.GONE);
                  moon_tree_iv.setVisibility(View.GONE);

                  ObjectAnimator objectAnimator9 = ObjectAnimator.ofFloat(moon_ly, "alpha", 0f, 1f);
                  objectAnimator9.setDuration(0);
                  objectAnimator9.setStartDelay(0);
                  objectAnimator9.start();

                  moon_ly.setVisibility(View.GONE);
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

         @Override public void onAnimationCancel(Animator animation) {

         }

         @Override public void onAnimationRepeat(Animator animation) {

         }
      });
   }

   ObjectAnimator objectAnimator1;
   ObjectAnimator objectAnimator2;
   ObjectAnimator objectAnimator3;
   ObjectAnimator objectAnimator4;
   ObjectAnimator objectAnimator5;
   ObjectAnimator objectAnimator6;
   ObjectAnimator objectAnimator7;
}
