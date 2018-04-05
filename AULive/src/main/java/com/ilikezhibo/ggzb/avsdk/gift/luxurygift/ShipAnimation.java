package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
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
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class ShipAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView water1 = null;
   ImageView water2 = null;
   ImageView face_iv = null;
   RelativeLayout ship_body_layout = null;

   ImageView plane_shadow_ly = null;

   SendGiftEntity sendGiftEntity;

   View root_view;

   public ShipAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;
      root_view = rootView;
      root_view.setVisibility(View.VISIBLE);
      water1 = (ImageView) rootView.findViewById(R.id.ship_water1);
      water2 = (ImageView) rootView.findViewById(R.id.ship_water2);
      //船身
      ship_body_layout = (RelativeLayout) rootView.findViewById(R.id.ship_body_layout);
      face_iv = (ImageView) rootView.findViewById(R.id.yacht_user_portrait);
      plane_shadow_ly = (ImageView) rootView.findViewById(R.id.plane_shadow);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");

      ImageLoader.getInstance()
          .displayImage(sendGiftEntity.face, face_iv, AULiveApplication.getGlobalImgOptions());

      //前轮动画，只加载与写监听
      //ImageLoader.getInstance()
      //    .displayImage(DRAWABLE + R.drawable.yacht_water_one, water1, new ImageLoadingListener() {
      //       @Override public void onLoadingStarted(String s, View view) {
      //
      //       }
      //
      //       @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
      //
      //       }
      //
      //       @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
      ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(water1, "alpha", 0f, 1f);
      objectAnimator1.setDuration(200);
      objectAnimator1.start();

      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(water1, "translationX", 0f, 160f, 0f);
      objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
      objectAnimator.setRepeatMode(ValueAnimator.RESTART);
      objectAnimator.setDuration(3000);
      objectAnimator.start();
      //   }
      //
      //   @Override public void onLoadingCancelled(String s, View view) {
      //
      //   }
      //});

      //后轮动画
      //ImageLoader.getInstance()
      //    .displayImage(DRAWABLE + R.drawable.yacht_water_one, water2, new ImageLoadingListener() {
      //       @Override public void onLoadingStarted(String s, View view) {
      //
      //       }
      //
      //       @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
      //
      //       }
      //
      //       @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
      ObjectAnimator alpha_water2 = ObjectAnimator.ofFloat(water2, "alpha", 0f, 1f);
      alpha_water2.setDuration(200);
      alpha_water2.start();

      ObjectAnimator tra_water2 = ObjectAnimator.ofFloat(water2, "translationX", 0f, -160f, 0f);
      tra_water2.setRepeatCount(ValueAnimator.INFINITE);
      tra_water2.setRepeatMode(ValueAnimator.RESTART);
      tra_water2.setDuration(2000);
      tra_water2.start();
      //   }
      //
      //   @Override public void onLoadingCancelled(String s, View view) {
      //
      //   }
      //});
   }

   public void startCarOnAnimation() {

      ship_body_layout.setVisibility(View.VISIBLE);

      //影子动画
      //startShadowAnimation();
      //船随波浪浮动的动画
      ObjectAnimator objectAnimator =
          ObjectAnimator.ofFloat(ship_body_layout, "rotation", -5f, -10f, -5f);
      objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
      objectAnimator.setRepeatMode(ValueAnimator.RESTART);
      objectAnimator.setDuration(2000);
      objectAnimator.start();

      //动画阶段1
      final Animation shipAnimation = AnimationUtils.loadAnimation(context, R.anim.ship_anim1);
      shipAnimation.getFillAfter();
      final Animation shipAnimation2 = AnimationUtils.loadAnimation(context, R.anim.ship_anim2);
      shipAnimation2.getFillAfter();
      final Animation shipAnimation3 = AnimationUtils.loadAnimation(context, R.anim.ship_anim3);
      shipAnimation3.getFillAfter();

      shipAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            ship_body_layout.startAnimation(shipAnimation2);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      shipAnimation.setInterpolator(new DecelerateInterpolator());
      //动画阶段2

      shipAnimation2.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            ship_body_layout.startAnimation(shipAnimation3);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      //动画阶段3

      shipAnimation3.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            ship_body_layout.setVisibility(View.GONE);

            //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(water1, "alpha", 1f, 0f);
            //objectAnimator.setDuration(1000);
            //objectAnimator.start();
            //
            //ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(water2, "alpha", 1f, 0f);
            //objectAnimator2.setDuration(1000);
            //objectAnimator2.start();

            ViewAnimator.animate(water1).alpha(1f, 0f).duration(1000)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {

                   }
                }).start();

            ViewAnimator.animate(water2)
                .alpha(1f, 0f)
                .duration(1000)
                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      LuxuryGiftUtil.is_showing_luxury_gift = false;
                      if (context instanceof AvActivity) {
                         AvActivity temp_activity = (AvActivity) context;
                         //显示礼物动画
                         temp_activity.hasAnyLuxuryGift();
                      }
                   }
                })
                .start();
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      shipAnimation3.setInterpolator(new AccelerateInterpolator());

      ship_body_layout.startAnimation(shipAnimation);
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
