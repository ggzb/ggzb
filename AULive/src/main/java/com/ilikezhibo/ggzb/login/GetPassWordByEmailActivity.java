package com.ilikezhibo.ggzb.login;

import android.content.Intent;
import android.net.Uri;
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
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import java.util.regex.Pattern;

public class GetPassWordByEmailActivity extends BaseFragmentActivity implements
		OnClickListener, TitleListener {

	private EditText email_edittext;

	@Override
	protected void setContentView() {
		setContentView(R.layout.login_forgetpassword_mailway);
		
		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.INVISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("忘记密码");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("下一步");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		Button backButton = (Button) findViewById(R.id.topLayout).findViewById(
				R.id.back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);

		Button do_button = (Button) findViewById(R.id.do_button);
		do_button.setOnClickListener(this);

		email_edittext = (EditText) findViewById(R.id.email_edittext);

	}

	@Override
	protected void initializeViews() {

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
		case R.id.do_button:
			if (checkEmail()) {
				MobileConfig config = MobileConfig.getMobileConfig(this);
				postEmail(email_edittext.getText().toString(), config.getIemi());

			}
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}

	private boolean checkEmail() {

		if (!matchEmail(email_edittext.getText().toString())) {
			Utils.showMessage("您输入正确邮箱地址");
			return false;
		} else
			return true;
	}

	/** 根据URL打开界面 **/
	protected void openUrl(String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		startActivity(intent);

	}

	private void postEmail(final String email, String udid) {

		RequestInformation request = new RequestInformation(
				UrlHelper.EMAIL_GET_PWD, RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("email", email);
		request.addPostParams("udid", udid);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("已发验证邮件到你邮箱，请尽快行处理");
					int start = email.indexOf("@");
					String url = "http://mail." + email.substring(++start);
					openUrl(url);
					GetPassWordByEmailActivity.this.finish();
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage(Utils.trans(R.string.get_info_fail));
				GetPassWordByEmailActivity.this.finish();
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	private boolean matchEmail(String text) {
		if (Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
				.matcher(text).matches()) {
			return true;
		}
		return false;
	}

	@Override
	public void onBack() {
		this.finish();
	}

	@Override
	public void onTopRightEvent() {

	}

}
