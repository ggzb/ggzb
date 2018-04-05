package com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.jack.utils.Trace;

public class FlipRelativelayout extends RelativeLayout {
   private View mDelegate;
   private View mDelegate2;

   public FlipRelativelayout(Context paramContext) {
      super(paramContext);
   }

   public FlipRelativelayout(Context paramContext, AttributeSet paramAttributeSet) {
      super(paramContext, paramAttributeSet);
   }

   public FlipRelativelayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
      super(paramContext, paramAttributeSet, paramInt);
   }

   //public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
   //   return super.dispatchTouchEvent(paramMotionEvent);
   //}
   //
   public boolean onTouchEvent(MotionEvent paramMotionEvent) {
      Trace.d("FlipRelativeLayout onTouchEvent0");
      if (this.mDelegate != null) {
         Trace.d("FlipRelativeLayout onTouchEvent1");
         this.mDelegate.onTouchEvent(paramMotionEvent);
      }
      if (this.mDelegate2 != null) {
         this.mDelegate2.onTouchEvent(paramMotionEvent);
      }
      if (this.onTouchListener != null) {
         Trace.d("FlipRelativeLayout onTouchListener");
         onTouchListener.onTouch(null, paramMotionEvent);
      }

      return super.onTouchEvent(paramMotionEvent);
   }

   public void setDelegate(View paramViewGroup) {
      this.mDelegate = paramViewGroup;
   }

   public void setDelegate2(View paramViewGroup) {
      this.mDelegate2 = paramViewGroup;
   }

   private OnTouchListener onTouchListener;

   @Override public void setOnTouchListener(OnTouchListener onTouchListener) {
      this.onTouchListener = onTouchListener;
   }
}