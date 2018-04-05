package com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.jack.utils.Trace;

public class FlipLinearLayout extends LinearLayout {
   private View mDelegate;
   private View mDelegate2;

   public FlipLinearLayout(Context paramContext) {
      super(paramContext);
   }

   public FlipLinearLayout(Context paramContext, AttributeSet paramAttributeSet) {
      super(paramContext, paramAttributeSet);
   }

   public FlipLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
      super(paramContext, paramAttributeSet, paramInt);
   }

   //public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
   //   return super.dispatchTouchEvent(paramMotionEvent);
   //}
   //

   //
   //@Override public boolean onInterceptTouchEvent(MotionEvent ev) {
   //   try {
   //      super.onInterceptTouchEvent(ev);
   //   } catch (Exception e) {
   //
   //   }
   //   return false;
   //}

   public boolean onTouchEvent(MotionEvent paramMotionEvent) {
      try {
         if (this.mDelegate != null) {
            Trace.d("FlipLinearLayout onTouchEvent");
            this.mDelegate.onTouchEvent(paramMotionEvent);
         }
         if (this.mDelegate2 != null) {
            this.mDelegate2.dispatchTouchEvent(paramMotionEvent);
         }
         if (this.onTouchListener != null) {
            Trace.d("FlipLinearLayout onTouchListener");
            onTouchListener.onTouch(null, paramMotionEvent);
         }

      } catch (Exception e) {

      }
      super.onTouchEvent(paramMotionEvent);
        return true;
   }

   public void setDelegate(View paramViewGroup) {
      this.mDelegate = paramViewGroup;
   }

   public void setDelegate2(View paramViewGroup) {
      this.mDelegate2 = paramViewGroup;
   }

   private OnTouchListener onTouchListener;

   //@Override public void setOnTouchListener(OnTouchListener onTouchListener) {
   //   this.onTouchListener = onTouchListener;
   //}
}