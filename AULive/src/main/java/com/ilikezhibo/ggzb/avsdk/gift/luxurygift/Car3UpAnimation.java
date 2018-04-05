package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class Car3UpAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView front_wheel = null;
   ImageView back_wheel = null;
   RelativeLayout car3_up = null;

   RelativeLayout light_layout = null;

   SendGiftEntity sendGiftEntity;

   public Car3UpAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;

      front_wheel = (ImageView) rootView.findViewById(R.id.car_one_front_wheel);
      back_wheel = (ImageView) rootView.findViewById(R.id.car_one_back_wheel);
      car3_up = (RelativeLayout) rootView.findViewById(R.id.car_3_up);

      light_layout = (RelativeLayout) rootView.findViewById(R.id.car_3_light);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
      //startAnimation(car3_up);

      //前轮动画，只加载与写监听
      ImageLoader.getInstance()
          .displayImage(DRAWABLE + R.drawable.gift_common_wheel, front_wheel,
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ViewAnimator.animate(front_wheel)
                        //.rotationX(0, -5)
                        .rotationY(0f, -65.0f).duration(30).onStop(new AnimationListener.Stop() {
                       @Override public void onStop() {
                          ObjectAnimator objectAnimator =
                              ObjectAnimator.ofFloat(front_wheel, "rotation", 0f, -360f);
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
          .displayImage(DRAWABLE + R.drawable.gift_common_wheel, back_wheel,
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ViewAnimator.animate(back_wheel)
                        //.rotationX(0, -5)
                        .rotationY(0f, -65.0f).duration(30).onStop(new AnimationListener.Stop() {
                       @Override public void onStop() {
                          ObjectAnimator objectAnimator =
                              ObjectAnimator.ofFloat(back_wheel, "rotation", 0f, -360f);
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

   public void startCarOnAnimation() {
      car3_up.setVisibility(View.VISIBLE);
      //动画阶段1
      final Animation carAnimation = AnimationUtils.loadAnimation(context, R.anim.car_3_up_anim1);
      carAnimation.getFillAfter();
      final Animation carAnimation2 = AnimationUtils.loadAnimation(context, R.anim.car_3_up_anim2);
      carAnimation2.getFillAfter();
      final Animation carAnimation3 = AnimationUtils.loadAnimation(context, R.anim.car_3_up_anim3);
      carAnimation3.getFillAfter();

      carAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            car3_up.startAnimation(carAnimation2);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      carAnimation.setInterpolator(new DecelerateInterpolator());
      //动画阶段2

      carAnimation2.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

            //灯光动画，可见，不可见，可见，不可见,可见，不可见

            light_layout.setVisibility(View.VISIBLE);
            light_layout.postDelayed(new Runnable() {
               @Override public void run() {
                  light_layout.setVisibility(View.INVISIBLE);
                  light_layout.postDelayed(new Runnable() {
                     @Override public void run() {
                        light_layout.setVisibility(View.VISIBLE);
                        light_layout.postDelayed(new Runnable() {
                           @Override public void run() {
                              light_layout.setVisibility(View.INVISIBLE);
                              light_layout.postDelayed(new Runnable() {
                                 @Override public void run() {
                                    light_layout.setVisibility(View.VISIBLE);
                                    light_layout.postDelayed(new Runnable() {
                                       @Override public void run() {
                                          light_layout.setVisibility(View.INVISIBLE);
                                       }
                                    }, 100);
                                 }
                              }, 100);
                           }
                        }, 100);
                     }
                  }, 100);
               }
            }, 200);
         }

         @Override public void onAnimationEnd(Animation animation) {
            car3_up.startAnimation(carAnimation3);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      //动画阶段3

      carAnimation3.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            car3_up.setVisibility(View.GONE);
            LuxuryGiftUtil.is_showing_luxury_gift = false;
            if (context instanceof AvActivity) {
               AvActivity temp_activity = (AvActivity) context;
               //显示礼物动画
               temp_activity.hasAnyLuxuryGift();
            }
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      carAnimation3.setInterpolator(new AccelerateInterpolator());

      car3_up.startAnimation(carAnimation);
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
