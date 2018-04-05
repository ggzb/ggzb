package com.ilikezhibo.ggzb.avsdk.activity.xiaolaba;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.ilikezhibo.ggzb.tool.SoloRequestListener;

public class CustomTextView extends TextView implements OnClickListener {
    public final static String TAG = CustomTextView.class.getSimpleName();

    private float textLength = 0f;//�ı�����
    private float viewWidth = 0f;
    private float step = 0f;//���ֵĺ�����
    private float y = 0f;//���ֵ�������
    private float temp_view_plus_text_length = 0.0f;//���ڼ������ʱ����
    private float temp_view_plus_two_text_length = 0.0f;//���ڼ������ʱ����
    public boolean isStarting = false;//�Ƿ�ʼ����
    private Paint paint = null;//��ͼ��ʽ
    private String text = "";//�ı�����
    private SoloRequestListener listener;


    public CustomTextView(Context context) {
        super(context);
        initView();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /** */
    /**
     * ��ʼ���ؼ�
     */
    private void initView() {
        setOnClickListener(this);
    }

    /** */
    /**
     * �ı���ʼ����ÿ�θ����ı����ݻ����ı�Ч����֮����Ҫ���³�ʼ��һ��
     */
    public void init(WindowManager windowManager, SoloRequestListener listener) {
        paint = getPaint();
        paint.setColor(Color.parseColor("#E40A0A"));
        text = getText().toString();
        textLength = paint.measureText(text);
        viewWidth = getWidth();
        if (viewWidth == 0) {
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                viewWidth = display.getWidth();
            }
        }
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        y = getTextSize() + getPaddingTop();
        this.listener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;

    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
//            boolean[] b = null;
//            in.readBooleanArray(b);
//            if (b != null && b.length > 0)
//                isStarting = b[0];
            step = in.readFloat();
        }
    }

    /** */
    /**
     * ��ʼ����
     */
    public void startScroll() {
        isStarting = true;
        invalidate();
    }

    /** */
    /**
     * ֹͣ����
     */
    public void stopScroll() {
        isStarting = false;
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        if(paint == null) {
            return;
        }
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if (!isStarting) {
            return;
        }
        step += 5;
        if (step > temp_view_plus_two_text_length) {
            if(listener != null) {
                listener.onSuccess();
            }
            stopScroll();
            return;
        }
        if (step > temp_view_plus_two_text_length)
            step = textLength;
        invalidate();

    }

    @Override
    public void onClick(View v) {
        if (isStarting)
            stopScroll();
        else
            startScroll();
    }



}
