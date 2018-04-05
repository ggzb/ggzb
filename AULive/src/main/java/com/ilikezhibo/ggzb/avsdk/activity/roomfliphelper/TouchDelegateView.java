package com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.jack.utils.Trace;

public class TouchDelegateView extends View {
   private ViewGroup mDelegate;
   private ViewGroup mDelegate2;

   public TouchDelegateView(Context paramContext) {
      super(paramContext);
   }

   public TouchDelegateView(Context paramContext, AttributeSet paramAttributeSet) {
      super(paramContext, paramAttributeSet);
   }

   public TouchDelegateView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
      super(paramContext, paramAttributeSet, paramInt);
   }

   public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
      return super.dispatchTouchEvent(paramMotionEvent);
   }

   public boolean onTouchEvent(MotionEvent paramMotionEvent) {

      if (this.mDelegate != null) {
         if (mCanTouch) {
            this.mDelegate.onTouchEvent(paramMotionEvent);
         }
      }
      if (this.mDelegate2 != null) {
         this.mDelegate2.dispatchTouchEvent(paramMotionEvent);
      }
      if (this.onTouchListener != null) {
         Trace.d("TouchDelegateView onTouchListener");
         onTouchListener.onTouch(null, paramMotionEvent);
      }

      return true;
   }

   boolean mCanTouch = true;

   public void setCanTouch(boolean paramBoolean) {
      this.mCanTouch = paramBoolean;
   }

   public void setDelegate(ViewGroup paramViewGroup) {
      this.mDelegate = paramViewGroup;
   }

   public void setDelegate2(ViewGroup paramViewGroup) {
      this.mDelegate2 = paramViewGroup;
   }

   private OnTouchListener onTouchListener;

   @Override public void setOnTouchListener(OnTouchListener onTouchListener) {
      this.onTouchListener = onTouchListener;
   }
}