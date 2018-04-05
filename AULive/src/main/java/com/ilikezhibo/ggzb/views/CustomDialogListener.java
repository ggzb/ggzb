package com.ilikezhibo.ggzb.views;

/**
 * 
 * @author jack.long
 * 
 */
public interface CustomDialogListener {

	/**
	 * 确定按钮
	 */
	public final static int BUTTON_POSITIVE = 1;
	/**
	 * 中立的
	 */
	public final static int BUTTON_NEGATIVE = 2;
	/**
	 * 取消
	 */
	public final static int BUTTON_NEUTRAL = 3;

	/**
	 * 
	 * @param coloseType
	 */
	public void onDialogClosed(int closeType);
}
