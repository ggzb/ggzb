package com.ilikezhibo.ggzb.avsdk.gift;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationBuilder;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by User on 2016/1/6.
 */
public class ContinueGiftView extends LinearLayout {
   private static Handler mHandler = new Handler();
   private ImageView mSimplePhoto;
   private TextView mGiftInfo;
   private TextView mGiftDesc;
   private ImageView mGiftPic;
   private StrokeTextView mCountText;
   private Context mContext;

   private int mGiftID;
   //当前处理的礼物
   public LinkedList<SendGiftEntity> sendGiftList;
   private AvActivity avActivity;
   //是否在加载中
   public boolean doingGiftShow = false;

   private Runnable mRunnable = new Runnable() {
      public final void run() {
         setGoneAnimator();
      }
   };

   public ContinueGiftView(Context context) {
      this(context, null);
   }

   public ContinueGiftView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public ContinueGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
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

   public void setSendGiftEntity(LinkedList<SendGiftEntity> sendGiftEntity, AvActivity avActivity) {

      this.sendGiftList = sendGiftEntity;
      this.avActivity = avActivity;

      SendGiftEntity tem_entity = sendGiftEntity.getFirst();
      String url = tem_entity.face;
      if (!url.startsWith("http")) {
         url = UrlHelper.SERVER_URL + url;
      }
      ImageLoader.getInstance()
          .displayImage(url, mSimplePhoto, AULiveApplication.getGlobalImgOptions());
      mGiftInfo.setText(tem_entity.nickname);
      mGiftDesc.setText("送一个" + tem_entity.gift_name);

      ImageLoader.getInstance()
          .displayImage(UrlHelper.GIFT_ROOT_URL + tem_entity.gift_id + ".png", mGiftPic,
              AULiveApplication.getGlobalImgOptions());
   }

   public void setTimes(int times) {
      this.mCountText.setText("X" + times);
   }

   public static final int REFRESH_TIME = 111;

   private Handler mHandler_timer = new Handler(new Handler.Callback() {
      @Override public boolean handleMessage(Message msg) {
         switch (msg.what) {
            case REFRESH_TIME:
               setTimesAnimator();
               break;
         }
         return false;
      }
   });

   //上次刷新X时的时间，如果doingGiftShow＝ture,而5秒内没动，则是卡死状态,很小的发生几率
   public long last_reflesh_text_time = 0;

   public void cleanBlockState() {
      long tem_time = System.currentTimeMillis() - last_reflesh_text_time;
      if (last_reflesh_text_time != 0 && doingGiftShow == true && tem_time > 6000) {
         if (BtnClickUtils.isFastClean()) {
            return;
         }
         Trace.d("cleanBlockState Happen end mGiftID:"+mGiftID);

         if (mGiftID == 1) {
            avActivity.playingGifID1 = "";
         } else if (mGiftID == 2) {
            avActivity.playingGifID2 = "";
         }
         //更新礼物显示
         doingGiftShow = false;
      }
   }

   public void setTimesAnimator() {
      last_reflesh_text_time = System.currentTimeMillis();

      //AnimationBuilder builder = ViewAnimator.animate(mCountText);
      //builder.scale(3.0f, 1f).interpolator(new BounceInterpolator());
      //builder.alpha(0f, 1f)
      //    .interpolator(new BounceInterpolator())
      //    .duration(800)
      //    .start()
      //    .onStop(new AnimationListener.Stop() {
      //       @Override public void onStop() {
      //          if (mIsContinue) {
      //             if (sendGiftList.size() > 0) {
      //                int gift_nums = sendGiftList.removeFirst().gift_nums;
      //                setTimes(gift_nums);
      //                setTimesAnimator();
      //             }
      //          }
      //       }
      //    });
      //
      ////删除上个setTimesAnimantor的postDelayed
      //mHandler.removeCallbacks(mRunnable);
      //mHandler.postDelayed(mRunnable, 3500L);

      if (sendGiftList.size() > 0) {
         int gift_nums = sendGiftList.removeFirst().gift_nums;
         setTimes(gift_nums);
         AnimationBuilder builder = ViewAnimator.animate(mCountText);
         builder.scale(3.0f, 1f).interpolator(new BounceInterpolator());
         builder.alpha(0f, 1f)
             .interpolator(new BounceInterpolator())
             .duration(800).onStop(new AnimationListener.Stop() {
            @Override public void onStop() {
               mHandler_timer.sendEmptyMessage(REFRESH_TIME);
            }
         }).start();
      } else {
         //当没有后，循环3秒，如果3秒内还有entity进来再回到上面的循环
         checkEntityLeft();
      }
   }

   private Timer mTimer;
   private int handlerTime = 0;

   public void checkEntityLeft() {
      if (mTimer != null) {
         mTimer.cancel();
         mTimer = null;
      }
      handlerTime = 3;
      mTimer = new Timer();
      mTimer.schedule(new TimerTask() {
         @Override public void run() {

            if (handlerTime > 0) {
               handlerTime = handlerTime - 1;
               if (sendGiftList.size() > 0) {
                  mHandler_timer.sendEmptyMessage(REFRESH_TIME);
                  mTimer.cancel();
                  return;
               }
            } else {
               Trace.d("3秒后没数据进来");
               //让当前礼物没来得紧急显示的缓存起来
               if (mGiftID == 1) {
                  avActivity.playingGifID1 = "";
               } else if (mGiftID == 2) {
                  avActivity.playingGifID2 = "";
               }


               //删除上个setTimesAnimantor的postDelayed
               mHandler.removeCallbacks(mRunnable);
               mHandler.postDelayed(mRunnable, 0L);
               //马上执行
               //setGoneAnimator();

               mTimer.cancel();
               mTimer = null;
            }
         }
      }, 0, 1000);
   }

   public void setGoneAnimator() {

      AnimationSet set = new AnimationSet(true);
      TranslateAnimation translateAnimation =
          new TranslateAnimation(0, 0, 0, DensityUtils.dip2px(mContext, -100));
      AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
      set.addAnimation(translateAnimation);
      set.addAnimation(alphaAnimation);
      set.setDuration(800L);
      set.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            setVisibility(INVISIBLE);
            //礼物的图标
            mGiftPic.setVisibility(INVISIBLE);
            //更新礼物显示
            doingGiftShow = false;
            avActivity.hasAnyCacheGift();
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }
      });
      startAnimation(set);
   }

   //参数加入model，初始化个人信息
   public void showGiftView() {
      Trace.d("ContinueGift showGiftView()");
      //TODO 何时继续动画，何时重置次数
      doingGiftShow = true;
      mCountText.setVisibility(INVISIBLE);
      final Animation showAnimation =
          AnimationUtils.loadAnimation(mContext, R.anim.continue_gift_show);
      final Animation showGift = AnimationUtils.loadAnimation(mContext, R.anim.continue_gift_show);
      showAnimation.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationStart(Animation animation) {

         }

         @Override public void onAnimationEnd(Animation animation) {
            mGiftPic.startAnimation(showGift);
            mCountText.setVisibility(VISIBLE);
            setTimesAnimator();

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

   //public void setCurrentTimes(int currentTimes) {
   //   this.mCurrentTimes = currentTimes;
   //}
   //
   //public void setContinueTimes(int continueTimes) {
   //   this.mContinueTimes = continueTimes;
   //}
   //
   //public void resetTimes() {
   //   mIsContinue = false;
   //   mCurrentContinueTimes = 0;
   //   mContinueTimes = 1;
   //   mCurrentTimes = 1;
   //}

   public int getmGiftID() {
      return mGiftID;
   }

   public void setmGiftID(int mGiftID) {
      this.mGiftID = mGiftID;
   }
}
