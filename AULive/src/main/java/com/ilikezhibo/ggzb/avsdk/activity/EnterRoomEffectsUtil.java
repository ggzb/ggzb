package com.ilikezhibo.ggzb.avsdk.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.PixelDpHelper;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.EnterEntity;

/**
 * Created by big on 3/28/16.
 */
public class EnterRoomEffectsUtil {
   private LayoutInflater inflater;
   private AvActivity mContext;
   private static EnterRoomEffectsUtil enterRoomEffectsUtil = null;

   //动画的根layout
   private LinearLayout enter_room_effect_ly;
   public static boolean is_showing_enter_room_effects = false;
   private TextView enter_room_effect;

   public static EnterRoomEffectsUtil getInstance(AvActivity avActivity) {

      if (enterRoomEffectsUtil == null) {
         enterRoomEffectsUtil = new EnterRoomEffectsUtil(avActivity);
      }
      enterRoomEffectsUtil.mContext = avActivity;
      return enterRoomEffectsUtil;
   }

   private EnterRoomEffectsUtil(AvActivity mContext) {
      inflater = LayoutInflater.from(mContext);
   }

   public void showEnterRoomEfct(EnterEntity enterEntity) {

      if (mContext == null) {
         return;
      }
      enter_room_effect_ly = (LinearLayout) mContext.findViewById(R.id.enter_room_effect_ly);
      enter_room_effect = (TextView) mContext.findViewById(R.id.enter_room_effect);
      //是否在做豪华礼物动画
      is_showing_enter_room_effects = true;
      //设置背景与文字
      setGradeImg(enterEntity.grade, enter_room_effect, enter_room_effect_ly, enterEntity);
      enter_room_effect_ly.setVisibility(View.VISIBLE);
      //动画阶段1
      final Animation shipAnimation =
          AnimationUtils.loadAnimation(mContext, R.anim.enter_room_anim1);
      shipAnimation.getFillAfter();
      shipAnimation.setDuration(500);
      final Animation shipAnimation2 =
          AnimationUtils.loadAnimation(mContext, R.anim.enter_room_anim2);
      shipAnimation2.getFillAfter();
      shipAnimation2.setDuration(2000);
      final Animation shipAnimation3 =
          AnimationUtils.loadAnimation(mContext, R.anim.enter_room_anim3);
      shipAnimation3.getFillAfter();
      shipAnimation3.setDuration(500);

      shipAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {
            enter_room_effect.setVisibility(View.INVISIBLE);
         }

         @Override public void onAnimationEnd(Animation animation) {
            enter_room_effect.setVisibility(View.VISIBLE);
            ViewAnimator.animate(enter_room_effect)
                .translationX(PixelDpHelper.dip2px(mContext, 200), 0)
                .duration(500)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {
                      enter_room_effect_ly.startAnimation(shipAnimation2);
                   }
                }).start();
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
            enter_room_effect_ly.startAnimation(shipAnimation3);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      //动画阶段3

      shipAnimation3.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {

            enter_room_effect_ly.setVisibility(View.GONE);
            EnterRoomEffectsUtil.is_showing_enter_room_effects = false;
            mContext.hasAnyEnterRoomEffect();
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      shipAnimation3.setInterpolator(new AccelerateInterpolator());

      enter_room_effect_ly.startAnimation(shipAnimation);
   }

   public static void setGradeImg(String grade1, TextView tv, LinearLayout rl,
       EnterEntity enterEntity) {
      if (grade1 == null || grade1.equals("")) {
         grade1 = "1";
      }
      int grade = Integer.parseInt(grade1);
      int res;
      int each_grade = 16;
      if (grade < each_grade) {
         res = R.drawable.enter_room_1;
      } else if (grade < 2 * each_grade && grade >= each_grade) {
         res = R.drawable.enter_room_1;
      } else if (grade < 3 * each_grade && grade >= 2 * each_grade) {
         res = R.drawable.enter_room_1;
      } else if (grade < 4 * each_grade && grade >= 3 * each_grade) {
         res = R.drawable.enter_room_2;
      } else if (grade < 5 * each_grade && grade >= 4 * each_grade) {
         res = R.drawable.enter_room_3;
      } else if (grade < 6 * each_grade && grade >= 5 * each_grade) {
         res = R.drawable.enter_room_4;
      } else if (grade < 7 * each_grade && grade >= 6 * each_grade) {
         res = R.drawable.enter_room_5;
      } else {
         res = R.drawable.enter_room_6;
      }

      rl.setBackgroundResource(res);
      //if (context != null) {
      //   Bitmap bitmap = readBitMap(context, res);
      //   rl.setBackgroundDrawable(new BitmapDrawable(bitmap));
      //}
      tv.setText("'" + enterEntity.nickname + "'进入直播间");
   }
}
