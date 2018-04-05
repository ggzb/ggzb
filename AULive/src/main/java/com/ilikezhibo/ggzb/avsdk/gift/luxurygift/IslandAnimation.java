package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
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

public class IslandAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;

   RelativeLayout island_ly = null;

   ImageView island_moon = null;

   ImageView island_cloud1 = null;
   ImageView island_cloud2 = null;
   ImageView island_cloud3 = null;
   ImageView island_cloud4 = null;

   ImageView island_meteor1 = null;
   ImageView island_meteor2 = null;
   ImageView island_meteor3 = null;
   ImageView island_meteor4 = null;
   ImageView island_meteor5 = null;

   ImageView island_light = null;

   RelativeLayout island_angle_ly = null;

   ImageView island_wings = null;
   AnimationDrawable island_wingsDrawable;
   ImageView island_angle = null;
   ImageView island_star = null;
   ImageView island_star2 = null;

   ImageView island_island1 = null;
   ImageView island_island2 = null;
   ImageView island_island3 = null;

   ImageView island_bg = null;
   SendGiftEntity sendGiftEntity;

   public IslandAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;
      island_ly = (RelativeLayout) rootView.findViewById(R.id.island_ly);

      island_moon = (ImageView) rootView.findViewById(R.id.island_moon);
      island_bg = (ImageView) rootView.findViewById(R.id.island_bg);

      island_cloud1 = (ImageView) rootView.findViewById(R.id.island_cloud1);
      island_cloud2 = (ImageView) rootView.findViewById(R.id.island_cloud2);
      island_cloud3 = (ImageView) rootView.findViewById(R.id.island_cloud3);
      island_cloud4 = (ImageView) rootView.findViewById(R.id.island_cloud4);

      island_meteor1 = (ImageView) rootView.findViewById(R.id.island_meteor1);
      island_meteor2 = (ImageView) rootView.findViewById(R.id.island_meteor2);
      island_meteor3 = (ImageView) rootView.findViewById(R.id.island_meteor3);
      island_meteor4 = (ImageView) rootView.findViewById(R.id.island_meteor4);
      island_meteor5 = (ImageView) rootView.findViewById(R.id.island_meteor5);

      island_light = (ImageView) rootView.findViewById(R.id.island_light);

      island_angle_ly = (RelativeLayout) rootView.findViewById(R.id.island_angle_ly);
      island_wings = (ImageView) rootView.findViewById(R.id.island_wings);
      island_angle = (ImageView) rootView.findViewById(R.id.island_angle);

      island_star = (ImageView) rootView.findViewById(R.id.island_star);
      island_star2 = (ImageView) rootView.findViewById(R.id.island_star2);

      island_island1 = (ImageView) rootView.findViewById(R.id.island_island1);
      island_island2 = (ImageView) rootView.findViewById(R.id.island_island2);
      island_island3 = (ImageView) rootView.findViewById(R.id.island_island3);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startAnimation() {
      island_ly.setVisibility(View.VISIBLE);

      ViewAnimator.animate(island_bg).alpha(1f, 0.5f).duration(0).start();

      //显示背景
      ViewAnimator.animate(island_ly).alpha(0f, 1f).duration(300)

          .onStart(new AnimationListener.Start() {
             @Override public void onStart() {
             }
          }).onStop(new AnimationListener.Stop() {
         @Override public void onStop() {
            //月亮
            objectAnimator = ObjectAnimator.ofFloat(island_moon, "alpha", 0f, 1f);
            objectAnimator.setDuration(1000);
            objectAnimator.setStartDelay(100);
            objectAnimator.start();
            objectAnimator.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_moon.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });
            //云
            ViewAnimator.animate(island_cloud1, island_cloud2, island_cloud3, island_cloud4)
                .alpha(0f, 1f)
                .duration(300)
                .onStart(new AnimationListener.Start() {
                   @Override public void onStart() {

                      ViewAnimator.animate(island_cloud1)
                          .dp()
                          .translationX(-100, 500)
                          .duration(25000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_cloud1.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      ViewAnimator.animate(island_cloud2)
                          .dp()
                          .translationX(-100, 500)
                          .duration(23000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_cloud2.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      ViewAnimator.animate(island_cloud3)
                          .dp()
                          .translationX(-120, 400)
                          .duration(25000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_cloud3.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      ViewAnimator.animate(island_cloud4)
                          .dp()
                          .translationX(-90, 440)
                          .duration(25000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_cloud3.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();
                   }
                })
                .start();

            //流星
            ViewAnimator.animate(island_meteor1, island_meteor2, island_meteor3, island_meteor4,
                island_meteor5).alpha(0f, 1f).duration(300).startDelay(3000)

                .onStart(new AnimationListener.Start() {
                   @Override public void onStart() {

                      ViewAnimator.animate(island_meteor1)
                          .dp()
                          .translationX(500, -500)
                          .translationY(-200, 500)
                          .repeatCount(-1)
                          .repeatMode(android.animation.ValueAnimator.RESTART)
                          .duration(3000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_meteor1.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      island_meteor2.setVisibility(View.VISIBLE);
                      ViewAnimator.animate(island_meteor2)
                          .dp()
                          .translationX(500, -500)
                          .translationY(-300, 400)
                          .repeatCount(-1)
                          .repeatMode(android.animation.ValueAnimator.RESTART)
                          .duration(3500)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_meteor2.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      island_meteor3.setVisibility(View.VISIBLE);
                      ViewAnimator.animate(island_meteor3)
                          .dp()
                          .translationX(500, -500)
                          .translationY(-240, 600)
                          .repeatCount(-1)
                          .repeatMode(android.animation.ValueAnimator.RESTART)
                          .duration(3500)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_meteor3.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      island_meteor4.setVisibility(View.VISIBLE);
                      ViewAnimator.animate(island_meteor4)
                          .dp()
                          .translationX(500, -500)
                          .translationY(-250, 460)
                          .repeatCount(-1)
                          .repeatMode(android.animation.ValueAnimator.RESTART)
                          .duration(4000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_meteor4.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();

                      island_meteor5.setVisibility(View.VISIBLE);
                      ViewAnimator.animate(island_meteor5)
                          .dp()
                          .translationX(500, -500)
                          .translationY(-300, 700)
                          .repeatCount(-1)
                          .repeatMode(android.animation.ValueAnimator.RESTART)
                          .duration(2000)
                          .startDelay(0)
                          .onStart(new AnimationListener.Start() {
                             @Override public void onStart() {
                                island_meteor5.setVisibility(View.VISIBLE);
                             }
                          })
                          .start();
                   }
                }).start();

            //天使
            objectAnimator3 = ObjectAnimator.ofFloat(island_angle_ly, "translationY", -400f, 400f);
            objectAnimator3.setDuration(3000);
            objectAnimator3.setStartDelay(1500);
            objectAnimator3.start();
            objectAnimator3.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_angle_ly.setVisibility(View.VISIBLE);
                  //天使翅膀
                  island_wings.setImageResource(R.drawable.island_wings);
                  island_wingsDrawable = (AnimationDrawable) island_wings.getDrawable();
                  island_wingsDrawable.start();
               }

               @Override public void onAnimationEnd(Animator animation) {
                  //到达目的后反复上下移动
                  ObjectAnimator objectAnimator5 =
                      ObjectAnimator.ofFloat(island_angle_ly, "translationY", 350, 400, 350);
                  objectAnimator5.setRepeatCount(ValueAnimator.INFINITE);
                  objectAnimator5.setRepeatMode(ValueAnimator.RESTART);
                  objectAnimator5.setDuration(1500);
                  objectAnimator5.setStartDelay(200);
                  objectAnimator5.start();
               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //天使背光
            objectAnimator4 = ObjectAnimator.ofFloat(island_light, "alpha", 0f, 1f);
            objectAnimator4.setDuration(1000);
            objectAnimator4.setStartDelay(1000);
            objectAnimator4.start();
            objectAnimator4.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_light.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //星星
            objectAnimator5 = ObjectAnimator.ofFloat(island_star, "alpha", 0f, 1f, 0f);
            objectAnimator5.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator5.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator5.setDuration(2000);
            objectAnimator5.setStartDelay(3000);
            objectAnimator5.start();
            objectAnimator5.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_star.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });
            //星星2
            objectAnimator2 = ObjectAnimator.ofFloat(island_star2, "alpha", 0f, 1f, 0f);
            objectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator2.setDuration(2000);
            objectAnimator2.setStartDelay(4000);
            objectAnimator2.start();
            objectAnimator2.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_star2.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //岛1
            objectAnimator6 = ObjectAnimator.ofFloat(island_island1, "translationY", -60, 50, -60);
            objectAnimator6.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator6.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator6.setDuration(3000);
            objectAnimator6.setStartDelay(1000);
            objectAnimator6.start();
            objectAnimator6.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_island1.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //岛2
            objectAnimator7 = ObjectAnimator.ofFloat(island_island2, "translationY", -50, 50, -50);
            objectAnimator7.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator7.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator7.setDuration(3000);
            objectAnimator7.setStartDelay(900);
            objectAnimator7.start();
            objectAnimator7.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_island2.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            //岛2
            objectAnimator0 = ObjectAnimator.ofFloat(island_island3, "translationY", -70, 70, -70);
            objectAnimator0.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator0.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator0.setDuration(4000);
            objectAnimator0.setStartDelay(1000);
            objectAnimator0.start();
            objectAnimator0.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {
                  island_island3.setVisibility(View.VISIBLE);
               }

               @Override public void onAnimationEnd(Animator animation) {

               }

               @Override public void onAnimationCancel(Animator animation) {

               }

               @Override public void onAnimationRepeat(Animator animation) {

               }
            });

            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(island_ly, "alpha", 1f, 0f);
            objectAnimator8.setDuration(3000);
            objectAnimator8.setStartDelay(15000);
            objectAnimator8.start();
            objectAnimator8.addListener(new Animator.AnimatorListener() {
               @Override public void onAnimationStart(Animator animation) {

               }

               @Override public void onAnimationEnd(Animator animation) {
                  objectAnimator0.cancel();
                  objectAnimator.cancel();
                  objectAnimator2.cancel();
                  objectAnimator3.cancel();
                  objectAnimator4.cancel();
                  objectAnimator5.cancel();
                  objectAnimator6.cancel();
                  objectAnimator7.cancel();

                  island_ly.setVisibility(View.GONE);
                  island_moon.setVisibility(View.GONE);
                  island_cloud1.setVisibility(View.GONE);
                  island_cloud2.setVisibility(View.GONE);
                  island_cloud3.setVisibility(View.GONE);
                  island_cloud4.setVisibility(View.GONE);

                  island_meteor1.setVisibility(View.GONE);
                  island_meteor2.setVisibility(View.GONE);
                  island_meteor3.setVisibility(View.GONE);
                  island_meteor4.setVisibility(View.GONE);
                  island_meteor5.setVisibility(View.GONE);

                  island_light.setVisibility(View.GONE);

                  island_angle_ly.setVisibility(View.GONE);
                  //island_wings.setVisibility(View.GONE);
                  island_wingsDrawable = null;
                  //island_angle.setVisibility(View.GONE);

                  island_star.setVisibility(View.GONE);

                  island_island1.setVisibility(View.GONE);
                  island_island2.setVisibility(View.GONE);
                  island_island3.setVisibility(View.GONE);

                  ObjectAnimator objectAnimator9 =
                      ObjectAnimator.ofFloat(island_ly, "alpha", 0f, 1f);
                  objectAnimator9.setDuration(0);
                  objectAnimator9.setStartDelay(0);
                  objectAnimator9.start();

                  island_ly.setVisibility(View.GONE);
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
}
