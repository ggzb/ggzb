package com.ilikezhibo.ggzb.step;

import android.content.Intent;
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
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.userinfo.MyProFileActivity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ShiHuoStep6Activity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText desc_et;
	private boolean back_home = false;

	@Override
	protected void setContentView() {
		setContentView(R.layout.step_shihuo_6_layout);
		back_home = getIntent().getBooleanExtra(
				MyProFileActivity.back_home_key, false);
	}

	@Override
	protected void initializeViews() {
		desc_et = (EditText) findViewById(R.id.desc_et);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("个人简介");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		// topRightBtn.setVisibility(View.GONE);

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);
		resetPwdBtn.setText("完成");
		// 如果为修改
		if (back_home == true) {
			resetPwdBtn.setText("修改");
		}

	}

	@Override
	protected void initializeData() {
		LoginUserEntity loginUserEntity = AULiveApplication.getUserInfo();
		if (loginUserEntity != null) {
			desc_et.setText(loginUserEntity.intro);
		}
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent intent = new Intent(ShiHuoStep6Activity.this,
					AULiveHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.reg_bt:
			if (desc_et.getText() == null || desc_et.getText().equals(""))
				return;
			sendCode();
			// Intent intent = new Intent(ShiHuoStep3Activity.this,
			// RegisterActivity.class);
			// startActivity(intent);
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	private void sendCode() {
		int step = 1;
		if (back_home) {
			step = 2;
		}

		RequestInformation request = null;
		try {
			request = new RequestInformation(UrlHelper.URL_HEAD
					+ "/reg/set_intro?intro="
					+ URLEncoder.encode(desc_et.getText().toString(), "UTF-8")
					+ "&step=" + step, RequestInformation.REQUEST_METHOD_GET);
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}

		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					if (callback.getUserinfo() != null) {
						AULiveApplication.setUserInfo(callback.getUserinfo());
					}

					if (back_home) {
						ShiHuoStep6Activity.this.finish();
					} else {
						Intent intent = new Intent(ShiHuoStep6Activity.this,
								AULiveHomeActivity.class);
						startActivity(intent);
					}
					Utils.showMessage(callback.getMsg());
				} else {

					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(UserInfo.class));
		request.execute();
	}

}
