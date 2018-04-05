package com.ilikezhibo.ggzb.avsdk.activity.xiaolaba;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;

import java.util.LinkedList;

/**
 * Created by big on 11/2/16.
 */

public class XiaoLaBaHelper {

   private AvActivity mActivity;
   private CustomTextView tv_marquee;

   public XiaoLaBaHelper(AvActivity mActivity1) {
      mActivity = mActivity1;
      init();
   }

   private void init() {
      ll_contain = (ViewGroup) mActivity.findViewById(R.id.marqueeView_contain);

//      mv = (MarqueeView) mActivity.findViewById(R.id.marqueeView100);
   }

   private LinkedList<XiaoLaBaEntity> cache_xiaolaba_List = new LinkedList<XiaoLaBaEntity>();
   public static boolean is_showing_enter_room_effects = false;

   public synchronized void doXiaoLaBaEffects(XiaoLaBaEntity xiaoLaBaEntity) {
      if (!is_showing_enter_room_effects) {
         Trace.d("***" +  "!showing");
         sendAnimator(xiaoLaBaEntity.system_content, xiaoLaBaEntity.sender, xiaoLaBaEntity.target);
      } else {
         Trace.d("***" +  "showing");
         addCacheXiaoLaBa(xiaoLaBaEntity);
      }
   }

   public void hasAnyXiaoLaBa() {
      if (cache_xiaolaba_List.size() > 0) {
         Trace.d("***" +  "da" + cache_xiaolaba_List.size());
         XiaoLaBaEntity xiaoLaBaEntity = cache_xiaolaba_List.removeFirst();
         sendAnimator(xiaoLaBaEntity.system_content, xiaoLaBaEntity.sender, xiaoLaBaEntity.target);
      }
   }

   public void addCacheXiaoLaBa(XiaoLaBaEntity XiaoLaBaEntity) {
      Trace.d("***" +  "addlaba");
      cache_xiaolaba_List.addLast(XiaoLaBaEntity);
   }

//   private MarqueeView mv;
   private ViewGroup ll_contain;


   private void Marquee(final String system_content) {
      Trace.d("***" +  "marquee");
      // Marquee #1: Configuration using code.
      // ***设置下一个动画之间的间隔
      /*mv.setPauseBetweenAnimations(0);
      // ***设置滚动速度
      mv.setSpeed(10);
      final TextView textView1 = (TextView) ll_contain.findViewById(R.id.textView1);
      Trace.d("***" +  "" + textView1);*/
      mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
         @Override public void run() {
            Trace.d("***" +  system_content);
            tv_marquee = (CustomTextView) ll_contain.findViewById(R.id.tv_marquee);
            tv_marquee.setText(system_content);
            tv_marquee.setVisibility(View.VISIBLE);
            tv_marquee.init(mActivity.getWindowManager(), new SoloRequestListener() {
               @Override
               public void onSuccess() {
                  Trace.d("***" +  "success");
                  tv_marquee.setVisibility(View.INVISIBLE);
                  AlphaAnimation aa = new AlphaAnimation(1f, 0f);
                  aa.setDuration(300);
                  ll_contain.startAnimation(aa);
                  aa.setAnimationListener(new Animation.AnimationListener() {
                     @Override
                     public void onAnimationStart(Animation animation) {
                        
                     }

                     @Override
                     public void onAnimationEnd(Animation animation) {
                        
                        ll_contain.setVisibility(View.INVISIBLE);
                        is_showing_enter_room_effects = false;
                        hasAnyXiaoLaBa();
                     }

                     @Override
                     public void onAnimationRepeat(Animation animation) {

                     }
                  });
               }

               @Override
               public void onFailure() {

               }
            });
            tv_marquee.startScroll();
//            tv_marquee.setEnabled(false);
           /* textView1.setText(system_content);
            textView1.setVisibility(View.VISIBLE);*/
            // 送出一份豪华大礼, 将顶上热门第一！
            // 设置textview可以超链接
           /* mv.setmMarqueeViewListener(new MarqueeView.MarqueeViewListener() {
               @Override
               public void onAnimationEnd() {
                  AlphaAnimation aa = new AlphaAnimation(1f, 0f);
                  aa.setDuration(300);
                  ll_contain.startAnimation(aa);
                  aa.setAnimationListener(new Animation.AnimationListener() {
                     @Override
                     public void onAnimationStart(Animation animation) {

                     }

                     @Override
                     public void onAnimationEnd(Animation animation) {
                        ll_contain.setVisibility(View.INVISIBLE);
                        is_showing_enter_room_effects = false;
                        hasAnyXiaoLaBa();
                     }

                     @Override
                     public void onAnimationRepeat(Animation animation) {

                     }
                  });
               }

               @Override public void onAnimationStart() {

               }
            });
            mv.startMarquee();*/
         }
      }, 0);
   }

   private void sendAnimator(final String system_content, final String send, final String rece) {
      Trace.d("***" +  "sendanimator");
      is_showing_enter_room_effects = true;
      if (ll_contain == null) {
         ll_contain = (ViewGroup) mActivity.findViewById(R.id.marqueeView_contain);
      }
      AlphaAnimation ta = new AlphaAnimation(0, 1f);
      ta.setDuration(300);
      ll_contain.startAnimation(ta);
      ta.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {
            ll_contain.setVisibility(View.VISIBLE);
         }

         @Override public void onAnimationEnd(Animation animation) {
            Trace.d("***" +  "zhixingdaolezheli");
            Marquee(system_content);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
   }
}
