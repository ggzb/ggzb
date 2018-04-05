package com.ilikezhibo.ggzb.step.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ClassName: AlwaysMarqueeTextView
 * @Description: TODO
 * @author jack.long
 * @date 2014-12-29 下午4:05:22
 * 
 */
public class AlwaysMarqueeTextView extends TextView {
	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
