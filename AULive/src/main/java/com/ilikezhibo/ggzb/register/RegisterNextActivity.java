package com.ilikezhibo.ggzb.register;

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
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.MobileConfig;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.SMSBroadcastReceiver;
import com.ilikezhibo.ggzb.SMSBroadcastReceiver.MessageListener;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import java.util.regex.Pattern;
import tinker.android.util.TinkerManager;

public class RegisterNextActivity extends BaseFragmentActivity implements
		TitleListener, OnClickListener {

	private EditText editAccount;
	private EditText editpass;

	@Override
	protected void setContentView() {
		setContentView(R.layout.reg_second_layout);
	}

	@Override
	protected void initializeViews() {
		editAccount = (EditText) findViewById(R.id.phoneEt);
		editpass = (EditText) findViewById(R.id.passwordEt);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("注册(1/2)");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("下一步");
		topRightBtn.setOnClickListener(this);
		// topRightBtn.setVisibility(View.GONE);

		getAuthCodeBtn = (Button) findViewById(R.id.getAuthCodeBtn);
		getAuthCodeBtn.setOnClickListener(this);
		authCodeEt = (EditText) findViewById(R.id.authCodeEt);

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);
		resetPwdBtn.setVisibility(View.GONE);

		handler = new Handler();

		setAuthCode();
	}

	@Override
	protected void initializeData() {
		String phone = SharedPreferenceTool.getInstance().getString(
				SharedPreferenceTool.REG_PHONE, "");
		editAccount.setText(phone);
		if (!phone.equals("")) {
			editpass.requestFocus();
		}
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			//finishCount();
			checkField();
			break;
		case R.id.reg_bt:
			//finishCount();
			checkField();
			// Intent intent = new Intent(RegisterNextActivity.this,
			// RegisterActivity.class);
			// startActivity(intent);
			break;
		case R.id.getAuthCodeBtn:
			if (checkSendCode()) {
				sendCode();
			}
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	private void setAuthCode() {
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

	private boolean isFinishing = false;
	private Button getAuthCodeBtn;
	private EditText authCodeEt;

	private void finishCount() {
		if (isFinishing) {
			return;
		}
		isFinishing = true;
		RequestInformation request = new RequestInformation(
				UrlHelper.REG_FINISH_URL, RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new StringCallback() {

			@Override
			public void onFailure(AppException e) {
				isFinishing = false;
			}

			@Override
			public void onCallback(String callback) {
				Trace.d("点了完成");
				isFinishing = false;
			}
		});
		request.execute();
	}

	protected boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	/** 左边事件 */
	@Override
	public void onBack() {
		this.finish();
	}

	/** 右边事件 */
	@Override
	public void onTopRightEvent() {
		checkField();
	}

	private void checkField() {
		// 正则表达匹配手机
		if (editAccount.getText() == null || editAccount.getText().equals("")) {
			Utils.showMessage("请输入手机号");
			editAccount.requestFocus();
			return;
		} else if (!matchPhone(editAccount.getText().toString())) {
			Utils.showMessage("请输入正确的手机号");
			editAccount.requestFocus();
			return;
		} else if (editpass.getText() == null || editpass.getText().equals("")
				|| editpass.getText().length() < 6) {
			Utils.showMessage("请输入6位以上密码");
			editpass.requestFocus();
			return;
		} else if (authCodeEt.getText() == null
				|| authCodeEt.getText().equals("")) {
			Utils.showMessage("请输入正确的验证码");
			editpass.requestFocus();
			return;
		}
		doRegister(editAccount.getText().toString(), editpass.getText()
				.toString(), authCodeEt.getText().toString());
	}

	private boolean checkSendCode() {
		if (editAccount.getText() == null || editAccount.getText().equals("")) {
			Utils.showMessage("请输入手机号");
			editAccount.requestFocus();
			return false;
		} else if (!matchPhone(editAccount.getText().toString())) {
			Utils.showMessage("请输入正确的手机号");
			editAccount.requestFocus();
			return false;
		}
		return true;
	}

	private void doRegister(String account, String pwd, String code) {
		showProgressDialog("正在注册，请稍候...");
		RequestInformation request = new RequestInformation(
				UrlHelper.REG_SIGN_URL, RequestInformation.REQUEST_METHOD_POST);
		MobileConfig config = MobileConfig
				.getMobileConfig(RegisterNextActivity.this);
		request.addPostParams("account", account);
		request.addPostParams("pwd", Utils.encryption(pwd));
		request.addPostParams("udid", config.getIemi());
		request.addPostParams("code", code);
		AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();

		if (application.getLongitude() != 0 || application.getLatitude() != 0) {
			request.addPostParams("lng", application.getLongitude() + "");
			request.addPostParams("lat", application.getLatitude() + "");
		}

		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				cancelProgressDialog();
				if (callback == null) {
					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}

				if (callback.getStat() == 200) {
					int step = callback.getStep();
					Trace.d("reg step:" + step);
					if (callback.getUserinfo() != null) {
						AULiveApplication.setUserInfo(callback.getUserinfo());
					}

					Intent intent = new Intent(RegisterNextActivity.this,
							RegisterActivity.class);

					startActivity(intent);
					RegisterNextActivity.this.finish();
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				cancelProgressDialog();
				Utils.showMessage(Utils.trans(R.string.net_error));
			}

		}.setReturnType(UserInfo.class));
		request.execute();
	}

	private int handlerTime = 60;
	private Handler handler;

	private void sendCode() {
		RequestInformation request = new RequestInformation(
				UrlHelper.GET_AUTH_CODE, RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("phone", editAccount.getText().toString().trim());
		request.addPostParams("udid", MobileConfig.getMobileConfig(this)
				.getIemi());
		// type 1-没注册发 2-注册发
		request.addPostParams("type", "1");

		Trace.d("地址" + UrlHelper.GET_AUTH_CODE);

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
