package com.ilikezhibo.ggzb.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 
 * @author jack.long
 *
 *         支持复制粘贴的特殊textview
 */
public class MyTextView extends EditText {

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean getDefaultEditable() {// 等同于在布局文件中设置
											// android:editable="false"
		return false;
	}
}
