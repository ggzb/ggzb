package com.ilikezhibo.ggzb.avsdk.gift;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ilikezhibo.ggzb.R;

/**
 * Created by User on 2016/1/6.
 */
public class StrokeTextView extends TextView {
    private TextView target_tv = null;

    public StrokeTextView(Context paramContext) {
        super(paramContext);
        this.target_tv = new TextView(paramContext);
        a();
    }

    public StrokeTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.target_tv = new TextView(paramContext, paramAttributeSet);
        a();
    }

    public StrokeTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.target_tv = new TextView(paramContext, paramAttributeSet, paramInt);
        a();
    }

    private void a() {
        TextPaint localTextPaint = this.target_tv.getPaint();
        localTextPaint.setStrokeWidth(4.0F);
        localTextPaint.setStyle(Paint.Style.STROKE);
        this.target_tv.setTextColor(getResources().getColor(R.color.continue_gift_desc));
        this.target_tv.setGravity(getGravity());
    }

    protected void onDraw(Canvas paramCanvas) {
        this.target_tv.draw(paramCanvas);
        super.onDraw(paramCanvas);
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        this.target_tv.layout(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        CharSequence localCharSequence = this.target_tv.getText();
        if ((localCharSequence == null) || (!localCharSequence.equals(getText()))) {
            this.target_tv.setText(getText());
            postInvalidate();
        }
        super.onMeasure(paramInt1, paramInt2);
        this.target_tv.measure(paramInt1, paramInt2);
    }

    public void setLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
        super.setLayoutParams(paramLayoutParams);
        this.target_tv.setLayoutParams(paramLayoutParams);
    }

    public void setText(CharSequence paramCharSequence, BufferType paramBufferType) {
        super.setText(paramCharSequence, BufferType.NORMAL);
        if (this.target_tv != null)
            this.target_tv.setText(paramCharSequence);
    }

    public void setVisibility(int paramInt) {
        super.setVisibility(paramInt);
        this.target_tv.setVisibility(paramInt);
    }
}