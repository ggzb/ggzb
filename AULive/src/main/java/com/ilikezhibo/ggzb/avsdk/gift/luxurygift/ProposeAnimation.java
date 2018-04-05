package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.PixelDpHelper;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;
import java.io.InputStream;

public class ProposeAnimation {
   private static final String DRAWABLE = "drawable://";

   private Activity context;
   ImageView top_light = null;
   ImageView fllowr_wall = null;
   ImageView fllower_heart_bg = null;
   ImageView fllower_heart = null;
   ImageView fllower_human = null;
   ImageView fllower_shine_start = null;
   ImageView left_light = null;
   ImageView right_light = null;
   SendGiftEntity sendGiftEntity;

   RelativeLayout propose_ly;
   View root_view;

   public ProposeAnimation(Activity context, View rootView, SendGiftEntity sendGiftEntity1) {
      this.context = context;
      sendGiftEntity = sendGiftEntity1;
      root_view = rootView;

      propose_ly = (RelativeLayout) rootView.findViewById(R.id.propose_ly);

      top_light = (ImageView) rootView.findViewById(R.id.top_light);
      fllowr_wall = (ImageView) rootView.findViewById(R.id.fllowr_wall);
      fllower_heart_bg = (ImageView) rootView.findViewById(R.id.fllower_heart_bg);
      fllower_heart = (ImageView) rootView.findViewById(R.id.fllower_heart);
      fllower_human = (ImageView) rootView.findViewById(R.id.fllower_human);
      fllower_shine_start = (ImageView) rootView.findViewById(R.id.fllower_shine_start);
      left_light = (ImageView) rootView.findViewById(R.id.left_light);
      right_light = (ImageView) rootView.findViewById(R.id.right_light);

      TextView room_gift_car_one_send_person =
          (TextView) rootView.findViewById(R.id.room_gift_car_one_send_person);
      room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");
   }

   public void startAnimation() {
      propose_ly.setVisibility(View.VISIBLE);
      fllower_heart_bg.setVisibility(View.INVISIBLE);
      left_light.setVisibility(View.INVISIBLE);
      right_light.setVisibility(View.INVISIBLE);

      ViewAnimator.animate(top_light).scale(1, (float) 1.5).duration(0).start();

      //头顶灯光动画
      ObjectAnimator top_light_Animator =
          ObjectAnimator.ofFloat(top_light, "rotation", 0f, 100f, 0f);
      top_light_Animator.setRepeatCount(50);
      top_light_Animator.setRepeatMode(ValueAnimator.RESTART);
      top_light_Animator.setDuration(7000);
      top_light_Animator.start();

      //花墙动画
      fllowr_wall.postDelayed(new Runnable() {
         @Override public void run() {
            fllowr_wall.setVisibility(View.VISIBLE);
            ViewAnimator.animate(fllowr_wall)
                .translationY(-2500, PixelDpHelper.dip2px(context, 1100))
                .duration(8000)
                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {

                   }
                })
                .start();
         }
      }, 1000);

      fllower_heart.postDelayed(new Runnable() {
         @Override public void run() {
            fllower_heart.setVisibility(View.VISIBLE);
            //花心的大小
            ViewAnimator.animate(fllower_heart).scale(0, (float) 1.2).duration(1000)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      //花心背景
                      showFllowerHeartBGDelay();

                      //人
                      fllower_human.setVisibility(View.VISIBLE);
                      ViewAnimator.animate(fllower_human).scale(0, (float) 1.0).duration(2000)

                          .onStop(new AnimationListener.Stop() {
                             @Override public void onStop() {
                                //人身上的闪星星

                                fllower_shine_start.setVisibility(View.VISIBLE);
                                fllower_shine_start.postDelayed(new Runnable() {
                                   @Override public void run() {
                                      fllower_shine_start.setVisibility(View.INVISIBLE);
                                      fllower_shine_start.postDelayed(new Runnable() {
                                         @Override public void run() {
                                            fllower_shine_start.setVisibility(View.VISIBLE);
                                            fllower_shine_start.postDelayed(new Runnable() {
                                               @Override public void run() {
                                                  fllower_shine_start.setVisibility(View.INVISIBLE);
                                                  fllower_shine_start.postDelayed(new Runnable() {
                                                     @Override public void run() {
                                                        fllower_shine_start.setVisibility(
                                                            View.VISIBLE);
                                                        fllower_shine_start.postDelayed(
                                                            new Runnable() {
                                                               @Override public void run() {
                                                                  fllower_shine_start.setVisibility(
                                                                      View.INVISIBLE);
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
                          }).start();

                      left_light.postDelayed(new Runnable() {
                         @Override public void run() {
                            ViewHelper.setPivotX(left_light, 0);
                            ViewHelper.setPivotY(left_light, left_light.getHeight());
                            //左灯
                            left_light.setVisibility(View.VISIBLE);
                            left_light_Animator =
                                ObjectAnimator.ofFloat(left_light, "rotation", 12f, 45f, 12f);
                            left_light_Animator.setRepeatCount(20);
                            left_light_Animator.setRepeatMode(ValueAnimator.RESTART);
                            left_light_Animator.setDuration(2000);
                            left_light_Animator.start();
                         }
                      }, 2000);

                      right_light.postDelayed(new Runnable() {
                         @Override public void run() {
                            ViewHelper.setPivotX(right_light, right_light.getWidth());
                            ViewHelper.setPivotY(right_light, right_light.getHeight());
                            //右灯
                            right_light.setVisibility(View.VISIBLE);
                            right_light_Animator =
                                ObjectAnimator.ofFloat(right_light, "rotation", -15f, -50f, -15f);
                            right_light_Animator.setRepeatCount(20);
                            right_light_Animator.setRepeatMode(ValueAnimator.RESTART);
                            right_light_Animator.setDuration(2000);
                            right_light_Animator.start();
                         }
                      }, 2500);

                      propose_ly.postDelayed(new Runnable() {
                         @Override public void run() {
                            ViewAnimator.animate(propose_ly)
                                .alpha(1, 0)
                                .duration(2000)
                                .onStop(new AnimationListener.Stop() {
                                   @Override public void onStop() {
                                      ViewAnimator.animate(propose_ly)
                                          .alpha(0, 1)
                                          .duration(0)
                                          .start();
                                      propose_ly.setVisibility(View.GONE);
                                      fllower_human.setVisibility(View.GONE);
                                      fllower_heart.setVisibility(View.GONE);
                                      fllower_heart_bg.setVisibility(View.GONE);
                                      left_light.setVisibility(View.GONE);
                                      right_light.setVisibility(View.GONE);
                                      //下一个礼物
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
                      }, 8000);
                   }
                }).start();
         }
      }, 3000);
   }

   ObjectAnimator left_light_Animator;
   ObjectAnimator right_light_Animator;

   /**
    * 以最省内存的方式读取本地资源的图片
    */

   public static Bitmap readBitMap(Context context, int resId) {
      BitmapFactory.Options opt = new BitmapFactory.Options();
      opt.inPreferredConfig = Bitmap.Config.RGB_565;
      opt.inPurgeable = true;
      opt.inInputShareable = true;
      // 获取资源图片
      InputStream is = context.getResources().openRawResource(resId);
      return BitmapFactory.decodeStream(is, null, opt);
   }

   int firework_index = 0;

   //花心背景
   private void showFllowerHeartBGDelay() {

      ViewAnimator.animate(fllower_heart_bg).scale(1, (float) 1.3).duration(0).start();

      firework_index++;
      fllower_heart_bg.setVisibility(View.VISIBLE);
      fllower_heart_bg.postDelayed(new Runnable() {
         @Override public void run() {

            if (firework_index > 6) {
               //50次返回
               return;
            }
            int index = firework_index % 3;
            final String imgname = "fllower_heart_" + index;

            int imgid =
                context.getResources().getIdentifier(imgname, "drawable", context.getPackageName());
            Bitmap bitmap = readBitMap(context, imgid);
            fllower_heart_bg.setImageBitmap(bitmap);

            //加载完第一张之后才显示
            if (firework_index == 1) {
               root_view.setVisibility(View.VISIBLE);
            }

            //下一个图片
            showFllowerHeartBGDelay();
         }
      }, 900);
   }
}
