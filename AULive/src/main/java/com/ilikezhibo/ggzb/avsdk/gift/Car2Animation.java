package com.ilikezhibo.ggzb.avsdk.gift;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.R;

public class Car2Animation {
   private static final String DRAWABLE = "drawable://";

   private Context context;
   ImageView imageView = null;
   ImageView imageView1 = null;
   RelativeLayout car2 = null;

   public Car2Animation(Context context, View rootView) {
      this.context = context;

      imageView = (ImageView) rootView.findViewById(R.id.car_one_front_wheel);
      imageView1 = (ImageView) rootView.findViewById(R.id.car_one_back_wheel);
      car2 = (RelativeLayout) rootView.findViewById(R.id.car_2);

      //startAnimation(car2);

      //前轮动画，只加载与写监听
      ImageLoader.getInstance()
          .displayImage(DRAWABLE + R.drawable.gift_common_wheel, imageView,
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ViewAnimator.animate(imageView)
                        .rotationX(0, -10)
                        .rotationY(0f, 30.0f)
                        .duration(30)
                        .onStop(new AnimationListener.Stop() {
                           @Override public void onStop() {
                              ObjectAnimator objectAnimator =
                                  ObjectAnimator.ofFloat(imageView, "rotation", 0f, -360f);
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
          .displayImage(DRAWABLE + R.drawable.gift_common_wheel, imageView1,
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ViewAnimator.animate(imageView1)
                        .rotationX(0, -16)
                        .rotationY(0f, 40.0f)
                        .duration(30)
                        .onStop(new AnimationListener.Stop() {
                           @Override public void onStop() {
                              ObjectAnimator objectAnimator =
                                  ObjectAnimator.ofFloat(imageView1, "rotation", 0f, -360f);
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
      car2.setVisibility(View.VISIBLE);
      final Animation carAnimation = AnimationUtils.loadAnimation(context, R.anim.car_one_show);
      carAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            car2.setVisibility(View.GONE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      carAnimation.setInterpolator(new DecelerateAccelerateInterpolator());
      car2.startAnimation(carAnimation);
   }

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
