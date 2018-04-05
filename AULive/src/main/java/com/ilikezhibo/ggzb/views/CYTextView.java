package com.ilikezhibo.ggzb.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.jack.utils.PixelDpHelper;
import com.ilikezhibo.ggzb.R;
import java.util.Vector;

public class CYTextView extends TextView {
    public static int m_iTextHeight; // 文本的高度
    public static int m_iTextWidth;// 文本的宽度
 
    private Paint mPaint = null;   
    private String string = "";
    private float LineSpace = 0;// 行间距
 
    private Context context; 
    public CYTextView(Context context, AttributeSet set) {
        super(context, set);
        this.context = context; 
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.CYTextView);
 
        float textsize = typedArray.getDimension(R.styleable.CYTextView_mytextSize, 20);
        int textcolor = typedArray.getColor(R.styleable.CYTextView_mytextColor, -1442840576);
        float linespace = typedArray.getDimension(R.styleable.CYTextView_lineSpacingExtra, -12);  
        int typeface = typedArray.getColor(R.styleable.CYTextView_typeface, 0);
        linespace = PixelDpHelper.dip2px(context, -5);
         
        typedArray.recycle();
 
        // 设置 CY TextView的宽度和行间距www.linuxidc.com
        LineSpace = linespace;
 
        // 构建paint对象
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(textcolor);
        mPaint.setTextSize(textsize);
        switch (typeface) {
        case 0:
            mPaint.setTypeface(Typeface.DEFAULT);
            break;
        case 1:
            mPaint.setTypeface(Typeface.SANS_SERIF);
            break;
        case 2:
            mPaint.setTypeface(Typeface.SERIF);
            break;
        case 3:
            mPaint.setTypeface(Typeface.MONOSPACE);
            break;
        default:
            mPaint.setTypeface(Typeface.DEFAULT);
            break;
        }
 
    }
     
    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (m_iTextWidth <= 0) {
            return;
        }
        char ch;
        int w = 0;
        int istart = 0;
        int m_iFontHeight;
        int m_iRealLine = 0;
        int x = PixelDpHelper.dip2px(context, 20); //60; //距左的距离
        int y = 10;
 
        Vector m_String = new Vector();
 
        FontMetrics fm = mPaint.getFontMetrics();
        m_iFontHeight = (int) Math.ceil(fm.descent - fm.top) + (int) LineSpace;// 计算字体高度（字体高度＋行间距）
        y = (int) Math.ceil(fm.descent - fm.top);
        for (int i = 0; i < string.length(); i++) {
            ch = string.charAt(i);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            mPaint.getTextWidths(srt, widths);
 
            if (ch == '\n') {
                m_iRealLine++;
                m_String.addElement(string.substring(istart, i));
                istart = i + 1;
                w = 0;
            } else {
                w += (int) (Math.ceil(widths[0]));
                if (w > m_iTextWidth) {
                    m_iRealLine++;
                    m_String.addElement(string.substring(istart, i));
                    istart = i;
                    i--;
                    w = 0;
                } else {
                    if (i == (string.length() - 1)) {
                        m_iRealLine++;
                        m_String.addElement(string.substring(istart, string.length()));
                    }
                }
            }
        }
         
        /**这里是需求要求2行*/
        if (m_iRealLine > 2){
            m_iRealLine = 2;  
        }
         
        m_iTextHeight = m_iRealLine * m_iFontHeight + 2;
        // canvas.setViewport(m_iTextWidth, m_iTextWidth);
        for (int i = 0, j = 0; i < m_iRealLine; i++, j++) {
            canvas.drawText((String) (m_String.elementAt(i)), x, y + m_iFontHeight * j, mPaint);
        }
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measureWidth(widthMeasureSpec);
        m_iTextWidth = measuredWidth;
        /**这里也是需求要求*/
        m_iTextWidth = m_iTextWidth - PixelDpHelper.dip2px(context, 26);
         
        int measuredHeight = measureHeight(heightMeasureSpec);
 
        this.setMeasuredDimension(measuredWidth, measuredHeight);
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
 
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Default size if no limits are specified.
        initHeight();
        int result = m_iTextHeight;
        if (specMode == MeasureSpec.AT_MOST) {
            // Calculate the ideal size of your
            // control within this maximum size.
            // If your control fills the available
            // space return the outer bound.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // If your control can fit within these bounds return that value.
            result = specSize;
        }
        return result;
    }
 
    private void initHeight() {
        // 设置 CY TextView的初始高度为0
        m_iTextHeight = 0;
 
        // 大概计算 CY TextView所需高度
        FontMetrics fm = mPaint.getFontMetrics();
        int m_iFontHeight = (int) Math.ceil(fm.descent - fm.top) + (int) LineSpace;
        int line = 0;
        int istart = 0;
 
        int w = 0;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            mPaint.getTextWidths(srt, widths);
 
            if (ch == '\n') {
                line++;
                istart = i + 1;
                w = 0;
            } else {
                w += (int) (Math.ceil(widths[0]));
                if (w > m_iTextWidth) {
                    line++;
                    istart = i;
                    i--;
                    w = 0;
                } else {
                    if (i == (string.length() - 1)) {
                        line++;
                    }
                }
            }
        }
        /**这里是需求，要求两行*/
        if (line > 2){
            line = 2;  
        }
        m_iTextHeight = (line) * m_iFontHeight + 2;
    }
 
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
 
        // Default size if no limits are specified.
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            // Calculate the ideal size of your control
            // within this maximum size.
            // If your control fills the available space
            // return the outer bound.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // If your control can fit within these bounds return that value.
            result = specSize;
        }
        return result;
    }
 
    public void SetText(String text) {// 注：此函数目前只有在UI线程中调用才可以把文本画出来，在其它线程中<p>
                                        // //无法画文本，找了好久找不到原因，求高手解答)
        string = text;
        requestLayout();
        invalidate();
    }
}