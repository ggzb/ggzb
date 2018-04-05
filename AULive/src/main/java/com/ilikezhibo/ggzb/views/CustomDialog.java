package com.ilikezhibo.ggzb.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.utils.DisplayUtil;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;

/**
 * 自定义对话框
 * 
 * @author
 * 
 */
public class CustomDialog extends Dialog {

	public static final int SINGLE_BTN = 1;// 一个按钮
	public static final int DOUBLE_BTN = 2;// 两个按钮
	private int resId = R.drawable.dialog_jing_gao;
	private String message = null;
	private String leftButton, rightButton;
	private CustomDialogListener listener = null;
	private ImageView iconImg = null;
	private TextView content = null;
	// >选择框
	private CheckBox cb_accept;
	private int type = DOUBLE_BTN;
	private boolean isCancelable = true;
	private boolean isShowCheckbox;
	private boolean isHCenter;
	private java.lang.String msg;
	private int color;
	private boolean isCancenOutside = true;
	private String toolTipContent;
	private int toolTipColor;
	private boolean hasTitle = true;
	private boolean hasToolTip;
	private String checkboxContent;
	private MyCheckboxListener mCheckboxListener;
	private Button buttonYes;
	private boolean checkboxChecked;

	public CustomDialog(Context context, CustomDialogListener listener) {
		super(context, R.style.Theme_dialog);
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_prompt);
			content = (TextView) findViewById(R.id.msgTv);
			if (hasTitle) {
				initTitle();
			} else {
				disableTitle();
			}
//			iconImg = (ImageView) findViewById(R.id.image);
//			iconImg.setBackgroundResource(resId);
			setTextView();
			if (isShowCheckbox) {
				cb_accept = (CheckBox) findViewById(R.id.cb_accept);
				cb_accept.setVisibility(View.VISIBLE);
				cb_accept.setChecked(checkboxChecked);
				cb_accept.setText(checkboxContent);
				if (mCheckboxListener != null) {
					cb_accept.setOnCheckedChangeListener(mCheckboxListener);
				}
//				iconImg.setVisibility(View.INVISIBLE);
			}
			if (hasToolTip) {
				initToolTip();
			}
			buttonYes = (Button) findViewById(R.id.button_yes);
			if (leftButton != null)
				buttonYes.setText(leftButton);
			buttonYes.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					dismiss();
					if (listener != null) {
						// >如果未设置监听器, 有checkbox, 并且未选中则是取消动作
						if (mCheckboxListener == null && isShowCheckbox && !cb_accept.isChecked()) {
							listener.onDialogClosed(CustomDialogListener.BUTTON_NEUTRAL);
							return;
						}
						listener.onDialogClosed(CustomDialogListener.BUTTON_POSITIVE);
					}
				}
			});
			Button buttonNo = null;
			if (type == DOUBLE_BTN) {
				buttonNo = (Button) findViewById(R.id.button_no);
				if (rightButton != null)
					buttonNo.setText(rightButton);
				buttonNo.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						dismiss();
						if (listener != null)
							listener.onDialogClosed(CustomDialogListener.BUTTON_NEUTRAL);
					}
				});
			} else {
				buttonYes.setBackgroundResource(R.drawable.dialog_btn_left_single);
				View view = findViewById(R.id.dialog_view);
				view.setVisibility(View.GONE);
				buttonNo = (Button) findViewById(R.id.button_no);
				buttonNo.setVisibility(View.GONE);
			}
			setCanceledOnTouchOutside(isCancenOutside);
		} catch (InflateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 去掉标题栏, 设置内容格式
	 */
	private void disableTitle() {
		LinearLayout ll_dialog_title = (LinearLayout) findViewById(R.id.ll_dialog_title);
		ll_dialog_title.setVisibility(View.GONE);
		LinearLayout ll_dialog_content = (LinearLayout) findViewById(R.id.ll_dialog_content);
		ll_dialog_content.setBackgroundResource(R.drawable.shape_first_normal);
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) content.getLayoutParams();
		layoutParams.topMargin = DisplayUtil.dip2px(20, AULiveApplication.mContext.getResources().getDisplayMetrics().density);
		content.setLayoutParams(layoutParams);
	}

	/**
	 * 填充提示条
	 */
	private void initToolTip() {
		TextView toolTip = (TextView) findViewById(R.id.tv_dialog_tooltip);
		toolTip.setVisibility(View.VISIBLE);
		toolTip.setText(toolTipContent);
		toolTip.setTextColor(toolTipColor);
	}

	/**
	 * 填充标题
	 */
	private void initTitle() {
		TextView title1 =  (TextView) findViewById(R.id.text);
		FormatCustomTitle(msg, title1);
	}

	public void setCanceledOutside(boolean isCancenOutside) {
		this.isCancenOutside  = isCancenOutside;
	}

	private void FormatCustomTitle(String msg, TextView title1) {
		if (msg != null) {
			if (!isHCenter) {
				LinearLayout.LayoutParams linearLayout = (LinearLayout.LayoutParams) title1.getLayoutParams();
				linearLayout.gravity = Gravity.NO_GRAVITY;
				title1.setLayoutParams(linearLayout);
			}
			title1.setText(Html.fromHtml(msg));
			title1.setTextColor(color);
		}
	}

	public void setCancelable(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}


	@Override
	public void onBackPressed() {
		if (listener != null) {
			// >取消
			Trace.d("**>>>取消");
			listener.onDialogClosed(CustomDialogListener.BUTTON_NEUTRAL);
		}
		super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (isCancelable) {
					dismiss();
					if (listener != null)
						listener.onDialogClosed(CustomDialogListener.BUTTON_NEGATIVE);
					return false;
				} else {
					return true;
				}
			} else {
				if (isCancelable) {
					return super.onKeyDown(keyCode, event);
				} else {
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 设置对话框的icon图标
	 * 
	 * @param resId
	 */
	public void setCustomIcon(int resId) {
		this.resId = resId;
		if (iconImg == null) {
			return;
		}
		if (resId == -1) {
			iconImg.setVisibility(View.INVISIBLE);
			return;
		} else {
			iconImg.setBackgroundResource(resId);

		}
	}


	// /**
	// * 设置对话框的标题
	// *
	// * @param title
	// */
	// public void setCustomTitle(String title) {
	// this.title = title;
	// }

	/**
	 * 设置自定义对话框的内容
	 * 
	 * @param message
	 */
	public void setCustomMessage(String message) {
		this.message = message;
	}


	/**
	 * 格式化对话框内容
	 */
	private void setTextView() {
		content.setText(message);
		ViewTreeObserver observer = content.getViewTreeObserver(); // textAbstract为TextView控件
		observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				ViewTreeObserver obs = content.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if(content.getLineCount() > 1) {
					content.setText("\u3000\u3000" + message);
					content.setGravity(Gravity.CENTER_VERTICAL);
				}
			}
		});
	}

	/**
	 * 设置button上的字符串
	 * 
	 * @param leftButton
	 * @param rightButton
	 */
	public void setButtonText(String leftButton, String rightButton) {
		this.leftButton = leftButton;
		this.rightButton = rightButton;
	}

   public void setContentTextSize(int size){
      if (content != null) {
      content.setTextSize(size);
      }
   }

   public void setCustomTitle(String title){
      TextView title1  = (TextView) findViewById(R.id.text);
      if(title1!=null) {
         title1.setText(Html.fromHtml(title));
      }
   }

	/**
	 * 格式化标题
	 * @param msg 标题内容
	 * @param isHCenter 是否居中
	 * @param color 颜色
     */
	public void setAndFormatTitle(String msg, boolean isHCenter, int color) {
		this.msg = msg;
		this.isHCenter = isHCenter;
		this.color = color;
	}

	/**
	 * 是否包含标题栏,默认包含
	 * @param hasTitle
     */
	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}

	/**
	 * 是否包含提示条,默认不包含
	 * @param hasToolTip
     */
	public void setHasToolTip(boolean hasToolTip) {
		this.hasToolTip = hasToolTip;
	}

	/**
	 * 设置提示条内容
	 * @param toolTipContent
     */
	public void setToolTipContent(String toolTipContent) {
		this.toolTipContent = toolTipContent;
	}

	/**
	 * 设置提示条颜色
	 * @param toolTipColor
     */
	public void setToolTipColor(int toolTipColor) {
		this.toolTipColor = toolTipColor;
	}

	/**
	 * 选择框设置
	 * @param isShowCheckbox
	 * @param checkboxContent
     */
	public void setCheckboxContent(boolean isShowCheckbox, String checkboxContent) {
		this.isShowCheckbox = isShowCheckbox;
		this.checkboxContent = checkboxContent;
	}

	public void setMyCheckboxListener(MyCheckboxListener mCheckboxListener) {
		this.mCheckboxListener = mCheckboxListener;
	}

	public void setCheckboxChecked(boolean checkboxChecked) {
		this.checkboxChecked = checkboxChecked;
	}


	public interface MyCheckboxListener extends CompoundButton.OnCheckedChangeListener {

		@Override
		void onCheckedChanged(CompoundButton compoundButton, boolean isChecked);
	}


}
