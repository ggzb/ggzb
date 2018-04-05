package com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.util.ArrayList;
import java.util.List;

public class WatchSwitchViewPager extends VerticalViewPager {
   public static final float mScrollSpeed = 0.60000002384185791016F;
   private boolean mCanTouch = true;
   private Context mContext;
   private List<AVEntity> mData = new ArrayList();
   private int mLastPosition = 0;
   private SwitchAnchorPagerAdapter mPagerAdapter;
   private WatchSwitchListener mWatchSwitchListener;
   WatchSwitchViewPagerScrollListener mWatchSwitchViewPagerScrollListener;

   public static int status_screen_height = 0;

   public WatchSwitchViewPager(Context paramContext) {
      super(paramContext);
      this.mContext = paramContext;
      init(paramContext);
   }

   public WatchSwitchViewPager(Context paramContext, AttributeSet paramAttributeSet) {
      super(paramContext, paramAttributeSet);
      this.mContext = paramContext;
      init(paramContext);
   }

   //三个方法的执行顺序为：用手指拖动翻页时，最先执行一遍onPageScrollStateChanged（1），
   // 然后不断执行onPageScrolled，放手指的时候，直接立即执行一次onPageScrollStateChanged（2），
   // 然后立即执行一次onPageSelected，然后再不断执行onPageScrollStateChanged，
   // 最后执行一次onPageScrollStateChanged（0）。

   private void init(Context paramContext) {

      //setOnTouchListener(new OnTouchListener() {
      //   @Override public boolean onTouch(View v, MotionEvent event) {
      //
      //      if(v instanceof  WatchSwitchViewPager){
      //         Trace.d("WatchSwitchViewPager setOnTouchListener："+v.toString());
      //         return true;
      //      }
      //       return false;
      //}
      //});

      this.mPagerAdapter = new SwitchAnchorPagerAdapter((Activity) paramContext);
      setAdapter(this.mPagerAdapter);
      this.mPagerAdapter.setData(this.mData);
      setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         //其中arg0这个参数有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，
         // arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
         public void onPageScrollStateChanged(int state) {
            //Trace.d("onPageScrollStateChanged state:" + state);
            if (state == 1) {
               Trace.d("**>>正在滑动");
               if (WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener != null) {
                  WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener.drag();
               }
            }

            if (state == 2) {
               Trace.d("**>>滑动结束了");
               if (WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener != null) {
                  WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener.end();
               }
            }

            if (state == 0) {
               if (WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener != null) {
                  WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener.idle();
               }
            }
         }

         //arg0 :当前页面，及你点击滑动的页面,会一直变
         //arg1:当前页面偏移的百分比
         //arg2:当前页面偏移的像素位置

         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //Trace.d("mLastPosition:"
            //    + mLastPosition
            //    + ",position"
            //    + position
            //    + ",onPageScrolled  positionOffset=:  "
            //    + positionOffset
            //    + "  positionOffsetPixels=:"
            //    + positionOffsetPixels);

            int i = 0;
            if (position < mLastPosition) {
               //向下拉要加
               i = status_screen_height - positionOffsetPixels;
            } else {
               //向上拉，要减
               i = positionOffsetPixels * -1;
            }

            if (WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener != null) {
               if (Math.abs(i) >= status_screen_height) {
                  return;
               }
               WatchSwitchViewPager.this.mWatchSwitchViewPagerScrollListener.scroll(i);
            }
         }

         public void onPageSelected(int position) {
            //Trace.d("onPageSelected position:" + position);
            mLastPosition = position;
            View localView =
                WatchSwitchViewPager.this.findViewWithTag(Integer.valueOf(mLastPosition));
            if (localView != null) {
               localView.setVisibility(VISIBLE);
            }
            if (WatchSwitchViewPager.this.mWatchSwitchListener != null) {
               int i = mData.size();
               AVEntity avEntity = mData.get(position % i);
               WatchSwitchViewPager.this.mWatchSwitchListener.switchAnchor(avEntity);
            }
         }
      });
   }

   private void viewHideAnima(final View paramView) {
      Trace.d("viewHideAnima");
      Animation localAnimation =
          AnimationUtils.loadAnimation(this.mContext, R.anim.slide_in_from_bottom);
      localAnimation.setFillEnabled(true);
      localAnimation.setFillAfter(true);
      paramView.startAnimation(localAnimation);
      localAnimation.setAnimationListener(new Animation.AnimationListener() {
         public void onAnimationEnd(Animation animation) {
            paramView.setVisibility(View.VISIBLE);
            paramView.setAlpha(1F);
         }

         public void onAnimationRepeat(Animation animation) {
         }

         public void onAnimationStart(Animation animation) {
         }
      });
   }

   public View getCurrentView() {
      int i = getCurrentItem();
      return findViewWithTag(Integer.valueOf(i));
   }

   public void hideCurrentView() {
      View localView = getCurrentView();
      if (localView != null) {
         localView.setVisibility(View.GONE);
      }
   }

   //public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
   //
   //   Trace.d("WatchSwitchViewPager dispatchTouchEvent");
   //   return false;
   //}
   //
   //public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
   //
   //   Trace.d("WatchSwitchViewPager onInterceptTouchEvent");
   //   return false;
   //}
   //
   //public boolean onTouchEvent(MotionEvent paramMotionEvent) {
   //   Trace.d("WatchSwitchViewPager onTouchEvent");
   //   //if ((!mCanTouch)) {
   //   //   return false;
   //   //}
   //   super.onTouchEvent(paramMotionEvent);
   //   return false;
   //}


   public void setCanTouch(boolean paramBoolean) {
      this.mCanTouch = paramBoolean;
   }

   //public void scrollTo(int x, int y) {
   //
   //   if (mCanTouch) {
   //      super.scrollTo(x, y);
   //   }
   //}

   public void setData(List<AVEntity> avEntities, String current_user_id) {
      this.mData.clear();
      if (avEntities != null) {
         mData = avEntities;
         mPagerAdapter.setData(mData);
         this.mPagerAdapter.notifyDataSetChanged();
         //找到自己的位置以及viewpager滚到相应位置
         int i = 0;
         for (AVEntity avEntity : mData) {
            if (avEntity.uid.equals(current_user_id)) {
               break;
            }
            i++;
         }
         this.mLastPosition = i;
         setCurrentItem(i);
      }
   }

   public void setWatchSwitchListener(WatchSwitchListener paramWatchSwitchListener) {
      this.mWatchSwitchListener = paramWatchSwitchListener;
   }

   public void setWatchSwitchViewPagerScrollListener(
       WatchSwitchViewPagerScrollListener paramWatchSwitchViewPagerScrollListener) {
      this.mWatchSwitchViewPagerScrollListener = paramWatchSwitchViewPagerScrollListener;
   }

   public void showCurrentView() {
      View localView = getCurrentView();
      if (localView != null) {
         localView.setVisibility(View.VISIBLE);
      }
   }

   public static abstract interface WatchSwitchListener {
      public abstract void switchAnchor(AVEntity paramLiveShow);
   }

   public static abstract interface WatchSwitchViewPagerScrollListener {
      public abstract void drag();

      public abstract void end();

      public abstract void idle();

      public abstract void scroll(int paramInt);
   }
}
