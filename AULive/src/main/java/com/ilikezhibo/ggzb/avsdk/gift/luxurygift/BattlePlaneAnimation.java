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
import com.jack.utils.PixelDpHelper;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

public class BattlePlaneAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;

   ImageView battleplane_body = null;
   ImageView right_rocket_fire = null;
   //整体
   RelativeLayout plane_ly = null;
   //右导弹
   RelativeLayout right_rocket_ly;

   //左导弹
   RelativeLayout left_rocket_ly;

   SendGiftEntity sendGiftEntity;

   public BattlePlaneAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;

      battleplane_body = (ImageView) rootView.findViewById(R.id.battleplane_body);

      plane_ly = (RelativeLayout) rootView.findViewById(R.id.battleplane_ly);

      right_rocket_ly = (RelativeLayout) rootView.findViewById(R.id.right_rocket_ly);
      right_rocket_fire = (ImageView) rootView.findViewById(R.id.right_rocket_fire);

      left_rocket_ly = (RelativeLayout) rootView.findViewById(R.id.left_rocket_ly);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startBattlePlaneAnimation() {
      plane_ly.setVisibility(View.VISIBLE);
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
            right_rocket_ly.setVisibility(View.VISIBLE);
            left_rocket_ly.setVisibility(View.VISIBLE);
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

            //开始导弹动画
            //右导弹
            right_rocket_fire.setVisibility(View.VISIBLE);
            //下落
            ViewAnimator.animate(right_rocket_ly)
                .translationX(0, PixelDpHelper.dip2px(context, 5))
                .translationY(0, PixelDpHelper.dip2px(context, 20))
                .duration(400)
                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      //移动
                      ViewAnimator.animate(right_rocket_ly)
                          .translationX(5, PixelDpHelper.dip2px(context, -400))
                          .translationY(40, PixelDpHelper.dip2px(context, 200))
                          .duration(2000)

                          .onStop(new AnimationListener.Stop() {
                             @Override public void onStop() {
                                //右导弹隐藏
                                right_rocket_ly.setVisibility(View.GONE);
                                right_rocket_fire.setVisibility(View.GONE);

                                //修复导弹
                                ViewAnimator.animate(right_rocket_ly)
                                    .translationX(PixelDpHelper.dip2px(context, -400), 0)
                                    .translationY(PixelDpHelper.dip2px(context, 200), 0)
                                    .duration(0)
                                    .start();
                             }
                          }).start();
                   }
                }).start();

            //左导弹
            //下落
            ViewAnimator.animate(left_rocket_ly)
                .translationX(0, PixelDpHelper.dip2px(context, 5))
                .translationY(0, PixelDpHelper.dip2px(context, 30))
                .duration(400)
                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {

                      ViewAnimator.animate(left_rocket_ly)
                          .translationX(5, PixelDpHelper.dip2px(context, -400))
                          .translationY(30, PixelDpHelper.dip2px(context, 200))
                          .duration(2000)
                          .onStop(new AnimationListener.Stop() {
                             @Override public void onStop() {
                                left_rocket_ly.setVisibility(View.GONE);
                                ViewAnimator.animate(left_rocket_ly)
                                    .translationX(PixelDpHelper.dip2px(context, -400), 0)
                                    .translationY(PixelDpHelper.dip2px(context, 200), 0)
                                    .duration(0)
                                    .start();
                             }
                          }).start();
                   }
                }).start();
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

      plane_ly.startAnimation(carAnimation);
   }
}
