package com.ilikezhibo.ggzb.avsdk.gift;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationBuilder;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.UrlHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;

/**
 * Created by User on 2016/1/6.
 */
public class ContinueGiftView2 extends RelativeLayout {
   private static Handler mHandler = new Handler();
   private ImageView mSimplePhoto;
   private TextView mGiftInfo;
   private TextView mGiftDesc;
   private ImageView mGiftPic;
   private StrokeTextView mCountText;
   private Context mContext;
   private boolean mIsContinue = false;

   //当前要显示的X前面的数字
   private int mCurrentTimes = 1;
   //调用一次一共要显示X的次数，如8次
   private int mContinueTimes = 1;
   //调用一次已经显示了3次X ，必须小于mContinueTies 8
   private int mCurrentContinueTimes = 0;
   private Runnable mRunnable = new Runnable() {
      public final void run() {
         setGoneAnimator();
      }
   };

   public ContinueGiftView2(Context context) {
      this(context, null);
   }

   public ContinueGiftView2(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public ContinueGiftView2(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      mContext = context;
      initView(context);
   }

   protected void initView(Context context) {
      LayoutInflater mInflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //加载布局文件
      mInflater.inflate(R.layout.room_continue_gift, this, true);
      mSimplePhoto = (ImageView) findViewById(R.id.img_creator_icon);
      mGiftInfo = (TextView) findViewById(R.id.txt_gift_info);
      mGiftDesc = (TextView) findViewById(R.id.txt_gift_desc);
      mGiftPic = (ImageView) findViewById(R.id.img_gift_icon);
      mCountText = (StrokeTextView) findViewById(R.id.txt_times);
   }

   public void setHeandImg(String url) {
      if (!url.startsWith("http")) {
         url = UrlHelper.SERVER_URL + url;
      }
      ImageLoader.getInstance()
          .displayImage(url, mSimplePhoto, AULiveApplication.getGlobalImgOptions());
   }

   public void setTimes(int times) {
      this.mCountText.setText("X" + times);
   }

   public void setTimesAnimator() {
      AnimationBuilder builder = ViewAnimator.animate(mCountText);
      builder.scale(3.0f, 1f).interpolator(new BounceInterpolator());
      builder.alpha(0f, 1f)
          .interpolator(new BounceInterpolator())
          .duration(900)

          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {
                if (mIsContinue) {
                   if (mCurrentContinueTimes < mContinueTimes) {
                      setTimes(mCurrentTimes);
                      setTimesAnimator();
                      mCurrentTimes++;
                      mCurrentContinueTimes++;
                   } else {
                      mCurrentContinueTimes = 0;
                   }
                }
             }
          }).start();
      mHandler.removeCallbacks(mRunnable);
      mHandler.postDelayed(mRunnable, 3500L);
   }

   public void setGoneAnimator() {
      AnimationSet set = new AnimationSet(true);
      TranslateAnimation translateAnimation =
          new TranslateAnimation(0, 0, 0, DensityUtils.dip2px(mContext, -100));
      AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
      set.addAnimation(translateAnimation);
      set.addAnimation(alphaAnimation);
      set.setDuration(900L);
      set.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            setVisibility(INVISIBLE);
            mGiftPic.setVisibility(INVISIBLE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      startAnimation(set);
   }

   //参数加入model，初始化个人信息
   public void showGiftView() {
      //TODO 何时继续动画，何时重置次数
      mCountText.setVisibility(VISIBLE);
      final Animation showAnimation =
          AnimationUtils.loadAnimation(mContext, R.anim.continue_gift_show);
      final Animation showGift = AnimationUtils.loadAnimation(mContext, R.anim.continue_gift_show);
      showAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            mGiftPic.startAnimation(showGift);
            setTimes(mCurrentTimes);
            setTimesAnimator();
            mCurrentTimes++;
            mCurrentContinueTimes++;
            mCountText.setVisibility(VISIBLE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      showGift.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            mGiftPic.setVisibility(VISIBLE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      startAnimation(showAnimation);
      if (getVisibility() != VISIBLE) {
         setVisibility(VISIBLE);
      }
   }

   public void showGiftViewOnce() {
      mIsContinue = false;
      mCountText.setVisibility(GONE);
      final Animation showAnimation =
          AnimationUtils.loadAnimation(mContext, R.anim.continue_gift_show);
      final Animation showGift = AnimationUtils.loadAnimation(mContext, R.anim.continue_gift_show);
      showAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            mGiftPic.startAnimation(showGift);
            setTimes(1);
            setTimesAnimator();
            mCountText.setVisibility(VISIBLE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      showGift.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            mGiftPic.setVisibility(VISIBLE);
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      startAnimation(showAnimation);
      if (getVisibility() != VISIBLE) {
         setVisibility(VISIBLE);
      }
   }

   public void setIsContinue(boolean isContinue) {
      this.mIsContinue = isContinue;
   }

   public void setCurrentTimes(int currentTimes) {
      this.mCurrentTimes = currentTimes;
   }

   public void setContinueTimes(int continueTimes) {
      this.mContinueTimes = continueTimes;
   }

   public void resetTimes() {
      mIsContinue = false;
      mCurrentContinueTimes = 0;
      mContinueTimes = 1;
      mCurrentTimes = 1;
   }
}
