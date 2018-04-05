package com.ilikezhibo.ggzb.avsdk.gift;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LoopViewPager extends ViewPager {
   private float rawX;
   private float rawY;
   private boolean isCanScroll = true;

   public LoopViewPager(Context context) {
      super(context);
   }

   public LoopViewPager(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
      if ((Math.abs(this.rawX - paramMotionEvent.getRawX()) > 40.0F)) {
         getParent().requestDisallowInterceptTouchEvent(true);
      } else if ((Math.abs(this.rawY - paramMotionEvent.getRawY()) < 80.0F) && (Math.abs(
          this.rawX - paramMotionEvent.getRawX()) > 40.0F)) {

         getParent().requestDisallowInterceptTouchEvent(true);
      }

      return super.onInterceptTouchEvent(paramMotionEvent);
   }

   public boolean onTouchEvent(MotionEvent paramMotionEvent) {
      this.rawY = paramMotionEvent.getRawY();
      this.rawX = paramMotionEvent.getRawX();
      return super.onTouchEvent(paramMotionEvent);
   }

   @Override public void scrollTo(int paramInt1, int paramInt2) {
      if (this.isCanScroll) {
         super.scrollTo(paramInt1, paramInt2);
      }
   }

   public void setCanScroll(boolean paramBoolean) {
      this.isCanScroll = paramBoolean;
   }
}
