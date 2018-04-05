package com.ilikezhibo.ggzb.userinfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

public class SuggestActivity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText pay_money_et;
//	private RadioGroup mRadioGroup;
//	private int problem_position = -1;

	@Override
	protected void setContentView() {
		setContentView(R.layout.suggest_layout);

	}

	@Override
	protected void initializeViews() {
		pay_money_et = (EditText) findViewById(R.id.pay_money);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("意见反馈");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		Button resetPwdBtn = (Button) findViewById(R.id.send_bt);
		resetPwdBtn.setOnClickListener(this);

//		mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
//		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	protected void initializeData() {
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent intent = new Intent(SuggestActivity.this,
					AULiveHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.send_bt:
			if (TextUtils.isEmpty(pay_money_et.getText().toString())) {
				Utils.showCenterMessage("请写下您宝贵的建议");
				return;
			}
//			if (problem_position == -1) {
//				Utils.showCenterMessage("请选择建议类型");
//				return;
//			}
			postFeedBack("memo", pay_money_et.getText().toString());
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	public static String nickname_key = "nickname_key_key";
	public static String project_id_key = "project_id_key";
	public static String uid_key = "uid_key";

	private void postFeedBack(String name, String value) {
		// url = url.replaceAll("&", "%26");
		value = value.replaceAll(" ", "%20");
		RequestInformation request = new RequestInformation(
				UrlHelper.FEED_BACK, RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams(name, value);
//		request.addPostParams("type", "" + type);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {
					showSuccPrompt("提交成功,我们会尽快处理你的反馈");
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	private CustomDialog promptDialog;

	private void showSuccPrompt(String msg) {
		if (promptDialog == null) {
			promptDialog = new CustomDialog(this, new CustomDialogListener() {

				@Override
				public void onDialogClosed(int closeType) {
					switch (closeType) {
					case CustomDialogListener.BUTTON_POSITIVE:
						pay_money_et.setText("");
//						mRadioGroup.clearCheck();
//						problem_position = -1;
//						SuggestActivity.this.finish();
						break;
					}
				}
			});
		}
		promptDialog.setCustomMessage(msg);
		promptDialog.setCancelable(false);
		promptDialog.setType(CustomDialog.SINGLE_BTN);

		if (null != promptDialog) {
			promptDialog.show();
		}
	}

}
