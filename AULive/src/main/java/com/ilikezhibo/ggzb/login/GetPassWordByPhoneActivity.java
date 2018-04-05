package com.ilikezhibo.ggzb.login;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.MobileConfig;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.SMSBroadcastReceiver;
import com.ilikezhibo.ggzb.SMSBroadcastReceiver.MessageListener;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import java.util.regex.Pattern;

public class GetPassWordByPhoneActivity extends BaseFragmentActivity implements
		OnClickListener, TitleListener {
	private int handlerTime = 60;

	private EditText phone_num;// 手机号
	private EditText passwordEt;// 密码

	private Button getAuthCodeBtn;// 获取验证码
	private EditText authCodeEt;// 验证码输入框

	private Button resetPwdBtn;// 重置密码

	private Handler handler;

	@Override
	protected void setContentView() {
		setContentView(R.layout.login_forgetpassword_phoneway);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.INVISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("重置密码");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("下一步");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		Button backButton = (Button) findViewById(R.id.topLayout).findViewById(
				R.id.back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);

		TextView enterPhoneInstroTv = (TextView) findViewById(R.id.enterPhoneInstroTv);
		enterPhoneInstroTv.setText("请输入注册时填写的手机号或与"
				+ Utils.trans(R.string.app_name) + "帐号绑定的手机号");

		phone_num = (EditText) findViewById(R.id.phone_edittext);
		passwordEt = (EditText) findViewById(R.id.passwordEt);

		getAuthCodeBtn = (Button) findViewById(R.id.getAuthCodeBtn);
		getAuthCodeBtn.setOnClickListener(this);
		authCodeEt = (EditText) findViewById(R.id.authCodeEt);

		resetPwdBtn = (Button) findViewById(R.id.resetPwdBtn);
		resetPwdBtn.setOnClickListener(this);

		handler = new Handler();

		init();
	}

	@Override
	protected void initializeViews() {

	}

	@Override
	protected void initializeData() {
		// 使用缓存的电话
		String phone = SharedPreferenceTool.getInstance().getString(
				SharedPreferenceTool.LOGIN_USER_PHONE, "");
		phone_num.setText(phone);
		if (!phone.equals("")) {
			passwordEt.requestFocus();
		}
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.getAuthCodeBtn:
			if (checkPhone()) {
				Trace.d("点击getAuthCodeBtn");
				getAuthCodeBtn.setEnabled(false);
				sendCode();
			}
			break;
		case R.id.resetPwdBtn:
			if (!checkPhone()) {
				return;
			}
			if (passwordEt.getText().toString().trim().length() < 6) {
				Utils.showMessage("请输入6位新密码");
				return;
			}
			if (authCodeEt.getText().toString().trim().length() < 4) {
				Utils.showMessage("请输入获取的4位验证码");
				return;
			}
			resetPwdBtn.setEnabled(false);
			resetPwd();
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}

	private boolean checkPhone() {
		if (!matchPhone(phone_num.getText().toString())) {
			Utils.showMessage("您输入正确手机号");
			return false;
		} else
			return true;
	}

	private boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	private void sendCode() {
		RequestInformation request = new RequestInformation(
				UrlHelper.URL_HEAD+"/reg/sendpwdsms", RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("phone", phone_num.getText().toString().trim());
		request.addPostParams("udid", MobileConfig.getMobileConfig(this)
				.getIemi());
		// type 1-不是注册时发 2-注册发
		request.addPostParams("type", "2");
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				if (callback == null) {
					getAuthCodeBtn.setEnabled(true);
					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					handlerTime = 60;
					handler.post(timeback);
					getAuthCodeBtn.setEnabled(false);
					Utils.showMessage(callback.getMsg());
				} else {
					getAuthCodeBtn.setEnabled(true);
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				getAuthCodeBtn.setEnabled(true);
				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	private void init() {
		SMSBroadcastReceiver mSMSBroadcastReceiver = new SMSBroadcastReceiver();
		mSMSBroadcastReceiver
				.setOnReceivedMessageListener(new MessageListener() {
					public void OnReceived(String message) {
						if (authCodeEt != null) {
							authCodeEt.setText(Utils
									.getDynamicPassword(message));// 截取6位验证码
						}
					}
				});
	}

	private void resetPwd() {
		showProgressDialog("正在重置密码，请稍候...");
		RequestInformation request = new RequestInformation(
				UrlHelper.RESET_PWD, RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("code", authCodeEt.getText().toString().trim());
		request.addPostParams("pwd",
				Utils.encryption(passwordEt.getText().toString()));
		request.addPostParams("account", phone_num.getText().toString());
		request.addPostParams("udid", MobileConfig.getMobileConfig(this)
				.getIemi());
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				cancelProgressDialog();
				resetPwdBtn.setEnabled(true);
				if (callback == null) {
					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					showLoginDialog();
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				cancelProgressDialog();
				resetPwdBtn.setEnabled(true);
				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	// 提示用户完善资料的对话框
	private void showLoginDialog() {
		CustomDialog customDialog = new CustomDialog(this,
				new CustomDialogListener() {

					@Override
					public void onDialogClosed(int closeType) {
						// TODO Auto-generated method stub
						switch (closeType) {
						case CustomDialogListener.BUTTON_POSITIVE:
							SharedPreferenceTool.getInstance().saveString(
									SharedPreferenceTool.LOGIN_USER_PHONE,
									phone_num.getText().toString());
							Intent intent = new Intent(
									GetPassWordByPhoneActivity.this,
									LoginActivity.class);
							startActivity(intent);
							GetPassWordByPhoneActivity.this.finish();
							break;
						}
					}
				});

		customDialog.setCustomMessage("重置密码成功，请重新登录");
		customDialog.setCancelable(false);
		customDialog.setType(CustomDialog.SINGLE_BTN);
		customDialog.show();
	}

	@Override
	public void onBack() {
		this.finish();
	}

	@Override
	public void onTopRightEvent() {

	}

	Runnable timeback = new Runnable() {

		@Override
		public void run() {
			if (handlerTime > 0) {
				getAuthCodeBtn.setText("验证码(" + handlerTime + ")");
				handler.postDelayed(this, 1000);
				handlerTime--;
			} else {
				getAuthCodeBtn.setText("重新发送");
				getAuthCodeBtn.setEnabled(true);
			}
		}
	};
}
