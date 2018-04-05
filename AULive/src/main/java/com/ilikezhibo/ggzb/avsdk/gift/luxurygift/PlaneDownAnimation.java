package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class PlaneDownAnimation {
   private static final String DRAWABLE = "drawable://";

   private AvActivity context;
   ImageView left_airscrew = null;
   ImageView right_airscrew = null;
   RelativeLayout plane_ly = null;

   ImageView plane_shadow_ly = null;

   SendGiftEntity sendGiftEntity;

   public PlaneDownAnimation(AvActivity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;

      left_airscrew = (ImageView) rootView.findViewById(R.id.plane_left_airscrew);
      right_airscrew = (ImageView) rootView.findViewById(R.id.plane_right_airscrew);
      plane_ly = (RelativeLayout) rootView.findViewById(R.id.plane_ly);

      plane_shadow_ly = (ImageView) rootView.findViewById(R.id.plane_shadow);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");

      //startAnimation(car3_up);

      //前轮动画，只加载与写监听
      ImageLoader.getInstance()
          .displayImage(DRAWABLE + R.drawable.plane_airscrew, left_airscrew,
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ViewHelper.setPivotX(left_airscrew, left_airscrew.getWidth() / 2f);
                    ViewHelper.setPivotY(left_airscrew, left_airscrew.getHeight() / 2f);
                    ViewAnimator.animate(left_airscrew)
                        .rotationX(0, -35)
                        .rotationY(0f, -45.0f)
                        .duration(30)

                        .onStop(new AnimationListener.Stop() {
                           @Override public void onStop() {
                              ObjectAnimator objectAnimator =
                                  ObjectAnimator.ofFloat(left_airscrew, "rotation", 0f, -360f);
                              objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
                              objectAnimator.setRepeatMode(ValueAnimator.RESTART);
                              objectAnimator.setDuration(100);
                              objectAnimator.start();
                           }
                        }).start();
                 }

                 @Override public void onLoadingCancelled(String s, View view) {

                 }
              });

      //后轮动画
      ImageLoader.getInstance()
          .displayImage(DRAWABLE + R.drawable.plane_airscrew, right_airscrew,
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    ViewHelper.setPivotX(right_airscrew, right_airscrew.getWidth() / 2f);
                    ViewHelper.setPivotY(right_airscrew, right_airscrew.getHeight() / 2f);

                    ViewAnimator.animate(right_airscrew)
                        .rotationX(0, -35)
                        .rotationY(0f, -42.0f)
                        .duration(30)
                        .onStop(new AnimationListener.Stop() {
                           @Override public void onStop() {

                              ObjectAnimator objectAnimator =
                                  ObjectAnimator.ofFloat(right_airscrew, "rotation", 0f, -360f);
                              objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
                              objectAnimator.setRepeatMode(ValueAnimator.RESTART);
                              objectAnimator.setDuration(100);
                              objectAnimator.start();
                           }
                        }).start();
                 }

                 @Override public void onLoadingCancelled(String s, View view) {

                 }
              });
   }

   public void startAnimation() {
      plane_ly.setVisibility(View.VISIBLE);
      startShadowAnimation();
      //动画阶段1
      final Animation carAnimation = AnimationUtils.loadAnimation(context, R.anim.car_3_down_anim1);
      carAnimation.getFillAfter();
      final Animation carAnimation2 =
          AnimationUtils.loadAnimation(context, R.anim.car_3_down_anim2);
      carAnimation2.getFillAfter();
      final Animation carAnimation3 =
          AnimationUtils.loadAnimation(context, R.anim.car_3_down_anim3);
      carAnimation3.getFillAfter();

      carAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            plane_ly.startAnimation(carAnimation2);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      carAnimation.setInterpolator(new DecelerateInterpolator());
      //动画阶段2

      carAnimation2.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            plane_ly.startAnimation(carAnimation3);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      //动画阶段3

      carAnimation3.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            plane_ly.setVisibility(View.GONE);
            LuxuryGiftUtil.is_showing_luxury_gift = false;
            context.hasAnyLuxuryGift();
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      carAnimation3.setInterpolator(new AccelerateInterpolator());

      plane_ly.startAnimation(carAnimation);
   }

   public void startShadowAnimation() {
      plane_shadow_ly.setVisibility(View.VISIBLE);
      final Animation shadowAnimation = AnimationUtils.loadAnimation(context, R.anim.car_one_show);
      shadowAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            plane_shadow_ly.setVisibility(View.GONE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      shadowAnimation.setInterpolator(new DecelerateAccelerateInterpolator());
      plane_shadow_ly.startAnimation(shadowAnimation);
   }

   //减速，加速器
   public class DecelerateAccelerateInterpolator implements android.view.animation.Interpolator {

      @Override public float getInterpolation(float input) {
         float result;
         if (input <= 0.5) {
            result = (float) (Math.sin(Math.PI * input)) / 2;
         } else {
            result = (float) (2 - Math.sin(Math.PI * input)) / 2;
         }
         return result;
      }
   }
}
